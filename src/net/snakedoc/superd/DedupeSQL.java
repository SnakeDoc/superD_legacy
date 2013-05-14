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

public class DedupeSQL {
    
    private final Logger log = Logger.getLogger(DedupeSQL.class);
    Config cfg = new Config("props/superD.properties");

    public DedupeSQL(){
        cfg.loadConfig("props/log4j.properties");
    }

	public void writeRecord(String file, String hash) {
		String sqlInsert = "INSERT INTO files (file_path, file_hash, file_size) VALUES (? , ? , ?)";
		PreparedStatement psInsert = null;
		try {
			psInsert = Database.getInstance().getConnection().prepareStatement(sqlInsert);
		} catch (SQLException e) {
			log.error("Failed to set databse query!", e);
		}
		File fl = new File(file);
		try {
			psInsert.setString(1, file);
			psInsert.setString(2, hash);
			psInsert.setLong(3, (fl.length()));
			log.debug("Writing record to database! \n File: " + file + " | Hash: " + hash);
			psInsert.executeUpdate();
		} catch (SQLException e) {
			log.error("Failed to query database!", e);
		}
	}
}
