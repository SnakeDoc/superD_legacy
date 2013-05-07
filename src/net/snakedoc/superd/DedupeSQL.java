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
import java.sql.SQLException;

import org.apache.log4j.Logger;

import net.snakedoc.jutils.Config;
import net.snakedoc.jutils.ConfigException;
import net.snakedoc.jutils.database.H2;

public class DedupeSQL {
    
    private static final Logger log = Logger.getLogger(DedupeSQL.class);
	
	public void writeRecord(String file, String hash) {
		
	    Config cfg = new Config("props/superD.properties"); // TODO fix like the db connection issue, 'this' keyword and instance vars
	    cfg.loadConfig("props/log4j.properties");  // TODO fix same as above
		H2 db = null;
		try {
            db = Database.getInstance();  // TODO this is a possible problem. see todo tag on db.openConnection() below for explanation
        } catch (ConfigException e2) {
            log.fatal("Failed to read config file!", e2);
        }
		
        try {
            db.openConnection();  // TODO this is a problem. causes new connection every iteration, solve with instance variables and using 'this' keyword.
        } catch (ClassNotFoundException | SQLException e1) {
            log.fatal("Failed to open database connection!", e1);
        }
		String sqlInsert = "INSERT INTO files (file_path, file_hash, file_size) VALUES (? , ? , ?)";
		PreparedStatement psInsert = null;
		try {
			psInsert = db.getConnection().prepareStatement(sqlInsert); // TODO fix like db connections
		} catch (SQLException e) {
			log.error("Failed to set databse query!", e);
		}
		
		try {
			psInsert.setString(1, file);
			psInsert.setString(2, hash);
			psInsert.setLong(3, (new File(file).length()));
			log.debug("Writing record to database! \n File: " + file + " | Hash: " + hash);
			psInsert.executeUpdate();	
		} catch (SQLException e) {
			log.error("Failed to query database!", e);
		}
	}
}
