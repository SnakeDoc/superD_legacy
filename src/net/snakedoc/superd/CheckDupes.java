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

import net.snakedoc.jutils.Config;
import net.snakedoc.jutils.ConfigException;
import net.snakedoc.jutils.database.H2;

public class CheckDupes {
	public static void main(String[] args) {
//	    Config config = new Config("props/superD.properties");
//		H2 db = null;
 //       try {
  //          db = new H2(config.getConfig("H2_dbURL"), config.getConfig("H2_dbUser"), config.getConfig("H2_dbPass"));
   //     } catch (ConfigException e2) {
    //        // TODO log out (error)
     //       e2.printStackTrace();
      //  }
		CheckDupes cd = new CheckDupes();
//		try {
 //           db.openConnection();
  //      } catch (ClassNotFoundException e1) {
   //         // TODO log out (fatal) - means driver for database is not found
    //        e1.printStackTrace();
     //   } catch (SQLException e1) {
       //     // TODO log out (fatal)
        //    e1.printStackTrace();
   //     }
		cd.checkDupes();
//		try {
 //           db.closeConnection();
  //      } catch (SQLException e) {
    //        // TODO log out (warning)
     //       e.printStackTrace();
      //  }
	}
	public void checkDupes() {
	    Config config = new Config("props/superD.properties");
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
            // TODO log out (error)
            e2.printStackTrace();
        }
        try {
            db.openConnection();
        } catch (ClassNotFoundException e1) {
            // TODO log out (fatal) - means driver for database is not found
            e1.printStackTrace();
        } catch (SQLException e1) {
            // TODO log out (fatal)
            e1.printStackTrace();
        }
		
		// let's get to business...
		
        // initalize our prepared statements
		try {
			psCount = db.getConnection().prepareStatement(sqlCount);
			psGetHashes = db.getConnection().prepareStatement(sqlGetHashes);
			psCompare = db.getConnection().prepareStatement(sqlCompare);
		} catch (SQLException e) {
		    // TODO log out (error)
			e.printStackTrace();
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
		    // TODO log out (error)
			e.printStackTrace();
		}
		
		// set deDupeObj array to size of hash_count (number of hashes in database)
		deDupeObj = new DeDupeObj[hash_count];
		
		try {
		    rsGetHashes = psGetHashes.executeQuery();
		} catch (SQLException e) {
		    // TODO log out (error)
		    e.printStackTrace();
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
		    // TODO log out (error)
			e.printStackTrace();
		}
		for (int i = 0; i < deDupeObj.length; i++) {
			try {
				psCompare.setString(1, deDupeObj[i].filehash);
				psCompare.setString(2, deDupeObj[i].filepath);
				
				rsCompare = psCompare.executeQuery();
				
				//if(psCompare.execute()) {
				    
				  //  rsCompare = psCompare.getResultSet();
				    
				    while(rsCompare != null && rsCompare.next()) {
				    
				    //TODO all sys out's change to log out (info)
					System.out.println("DUPLICATE FOUND!");
					duplicateCounter++;
					System.out.println(deDupeObj[i].filepath + " | " + deDupeObj[i].filehash);
					System.out.print(rsCompare.getString(1));
					System.out.println("");
					System.out.println(deDupeObj[i].filepath + " | " + rsCompare.getString(2));
					
					rsCompare.close();
					rsCompare = null;
				}
				psCompare.clearParameters();
			} catch (SQLException e) {
			    // log out (warning)
				e.printStackTrace();
				// continue running and find next dupe
				continue;
			}
		}
		try {
			psCompare.clearParameters();
			psCompare.close();
		} catch (SQLException e) {
		    //TODO log out (warning)
			e.printStackTrace();
		}
		System.out.print("Number of Duplicates Found: " + duplicateCounter);
		System.out.print(" out of " + hash_count + " files");
		System.out.printf("\nThat's means %.2f%% of your files are duplicates!\n", (((double)duplicateCounter / (double)hash_count) * 100));
		try {
            db.closeConnection();
        } catch (SQLException e) {
            // TODO log out (warning)
            e.printStackTrace();
        }
	}
}
