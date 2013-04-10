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

import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.snakedoc.jutils.Config;
import net.snakedoc.jutils.ConfigException;
import net.snakedoc.jutils.database.H2;

public class DedupeSQL {
	
	public void writeRecord(String file, String hash) {
		
	    Config cfg = new Config("props/superD.properties");
		H2 db = null;
		try {
            db = new H2(cfg.getConfig("H2_dbURL"), cfg.getConfig("H2_dbUser"), cfg.getConfig("H2_dbPass"));
        } catch (ConfigException e2) {
            // TODO log out
            e2.printStackTrace();
        }
		
       // db.setTargetCfg("props/supderD.properties");
        try {
            db.openConnection();
        } catch (ClassNotFoundException | SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
		String sqlInsert = "INSERT INTO files (file_path, file_hash) VALUES (? , ?)";
		PreparedStatement psInsert = null;
		try {
			psInsert = db.getConnection().prepareStatement(sqlInsert);
		} catch (SQLException e) {
			// convert to log out (fatal)
			e.printStackTrace();
		}
		
		try {
			psInsert.setString(1, file);
			psInsert.setString(2, hash);
			// debug log here saying about to write record
			psInsert.executeUpdate();	
		} catch (SQLException e) {
			// convert to log out (error)
			e.printStackTrace();
		}
	}
}
