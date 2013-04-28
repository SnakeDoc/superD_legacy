/*******************************************************************************
 *  Copyright 2013 Jason Sipula, Trace Hagan                                   *
 *                                                                             *
 *  Licensed under the Apache License, Version 2.0 (the "License");            *
 *  you may not use this file except in compliance with the License.           *
 *  You may obtain a copy of the License at                                    *
 *                                                                             *
 *      http://www.apache.org/licenses/LICENSE-2.0                             *
 *                                                                             *
 *  Unless required by applicable law or agreed to in writing, software        *
 *  distributed under the License is distributed on an "AS IS" BASIS,          *
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   *
 *  See the License for the specific language governing permissions and        *
 *  limitations under the License.                                             *
 *******************************************************************************/

package net.snakedoc.superd;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import net.snakedoc.jutils.Config;
import net.snakedoc.jutils.ConfigException;
import net.snakedoc.jutils.database.H2;

public class CheckDupes {
    
    private static final Logger log = Logger.getLogger(CheckDupes.class);
    
	public static void main(String[] args) {
		CheckDupes cd = new CheckDupes();
		cd.checkDupes();
	}
	public void checkDupes() {
	    Config config = new Config("props/superD.properties");
	    config.loadConfig("props/log4j.properties");
	    // SQL statements
	    String sqlCount = "SELECT COUNT(*) FROM files";
		String sqlGetHashes = "SELECT file_hash, file_path FROM files";
		String sqlCompare = "SELECT file_hash, file_path FROM files " +
				"WHERE file_hash = ? AND file_path != ?";
		
		// Prepared Statements (NULL)
		PreparedStatement psCount = null;
		PreparedStatement psGetHashes = null;
		PreparedStatement psCompare = null;
		
		// Result Sets (NULL)
		ResultSet rsCount = null;
		ResultSet rsGetHashes = null;
		ResultSet rsCompare = null;
		
		// Object to hold duplicate data
		DeDupeObj[] deDupeObj = null;
		
		// setup some counters
		int hash_count = 0;
		int loopCounter = 0;
		int duplicateCounter = 0;
		
		// setup database object
		H2 db = null;
        try {
            db = new H2(new File(config.getConfig("H2_dbURL")).getAbsolutePath(),
                            config.getConfig("H2_dbUser"), config.getConfig("H2_dbPass"));
        } catch (ConfigException e2) {
            log.error("Failed to read config file!", e2);
        }
        try {
            db.openConnection();
        } catch (ClassNotFoundException e1) {
            // means driver for database is not found
            log.fatal("Failed to read the database!", e1);
        } catch (SQLException e1) {
            log.fatal("Failed to open database!", e1);
        }
		
		// let's get to business...
		
        // initalize our prepared statements
		try {
			psCount = db.getConnection().prepareStatement(sqlCount);
			psGetHashes = db.getConnection().prepareStatement(sqlGetHashes);
			psCompare = db.getConnection().prepareStatement(sqlCompare);
		} catch (SQLException e) {
			log.error("Error setting database statements!", e);
		}
		try {
			rsCount = psCount.executeQuery();
			rsCount.next();
			// REPLACE WITH LIST<> or HASHMAP //
			hash_count = rsCount.getInt(1);
			psCount.clearParameters();
			rsCount.close();
			psCount.close();
		} catch (SQLException e) {
			log.error("Error running database queries!", e);
		}
		
		// set deDupeObj array to size of hash_count (number of hashes in database)
		deDupeObj = new DeDupeObj[hash_count];
		
		try {
		    rsGetHashes = psGetHashes.executeQuery();
		} catch (SQLException e) {
		    log.error("Error running database queries!", e);
		}
		    
		try {
			while(rsGetHashes.next()) {
				deDupeObj[loopCounter] = new DeDupeObj();
				deDupeObj[loopCounter].filehash = rsGetHashes.getString(1);
				deDupeObj[loopCounter].filepath = rsGetHashes.getString(2);
				
				loopCounter++;
			}
			rsGetHashes.close();
			psGetHashes.clearParameters();
			psGetHashes.close();
		} catch (SQLException e) {
			log.error("Error running database queries!", e);
		}
		for (int i = 0; i < deDupeObj.length; i++) {
			try {
				psCompare.setString(1, deDupeObj[i].filehash);
				psCompare.setString(2, deDupeObj[i].filepath);
				
				rsCompare = psCompare.executeQuery();
				    
				while(rsCompare != null && rsCompare.next()) {
				    
					log.info("DUPLICATE FOUND!");
					duplicateCounter++;
					log.debug(deDupeObj[i].filepath + " | " + deDupeObj[i].filehash);
					log.debug(rsCompare.getString(1));
					log.debug("");
					log.info(deDupeObj[i].filepath + " | " + rsCompare.getString(2));
					
					rsCompare.close();
					rsCompare = null;
				}
				psCompare.clearParameters();
			} catch (SQLException e) {
				log.warn("Failed to query database!", e);
				// continue running and find next dupe
				continue;
			}
		}
		try {
			psCompare.clearParameters();
			psCompare.close();
		} catch (SQLException e) {
			log.warn("Failed to close resource!", e);
		}
		log.info("\n\n\t\t~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
		log.info("Number of Duplicates Found: " + duplicateCounter);
		log.info(" out of " + hash_count + " files");
		log.info(String.format("\nThat means %.2f%% of your files are duplicates!\n", (((double)duplicateCounter / (double)hash_count) * 100)));
		try {
            db.closeConnection();
        } catch (SQLException e) {
            log.warn("Failed to close resource!", e);
        }
	}
}
