/*******************************************************************************
 *  Copyright (c) 2013 superD contributors, snakedoc.net and others            *
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

package net.snakedoc.superd.data;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.application.Platform;

import org.apache.log4j.Logger;

import net.snakedoc.jutils.Config;
import net.snakedoc.jutils.ConfigException;
import net.snakedoc.superd.filescan.Walker;
import net.snakedoc.superd.javafx.gui.ApplicationWindow;
import net.snakedoc.superd.javafx.gui.model.TableData;

public class DedupeSQL {
    
    private final Logger log = Logger.getLogger(DedupeSQL.class);
    Config cfg = new Config("props/superD.properties");
    private File fl;
    private String hash;

    public DedupeSQL(){
        cfg.loadConfig("props/log4j.properties");
    }

	public void writeRecord(String file, String hash) {
	    this.setHash(hash);
		String sqlInsert = "INSERT INTO files (file_path, file_hash, file_size) VALUES (? , ? , ?)";
		PreparedStatement psInsert = null;
		try {
			psInsert = Database.getInstance().getConnection().prepareStatement(sqlInsert);
		} catch (SQLException e) {
			log.error("Failed to set databse query!", e);
		}
		this.fl = new File(file);
		try {
			psInsert.setString(1, file);
			psInsert.setString(2, this.hash);
			psInsert.setLong(3, (fl.length()));
			log.debug("Writing record to database! \n File: " + file + " | Hash: " + this.hash);
			psInsert.executeUpdate();
			Platform.runLater(new Runnable() {
			    @Override
			    public void run() {
			        try {
                        ApplicationWindow.addData(new TableData(fl.getName(), (fl.getAbsoluteFile().toString().split(fl.getName())[0]), 
                                ((new BigDecimal(fl.length()).divide(new BigDecimal(1024))).divide(
                                            new BigDecimal(1024))).setScale(4, BigDecimal.ROUND_HALF_UP).toString(), 
                                        cfg.getConfig("HASH_ALGO"), getHash()));
                    } catch (ConfigException e) {
                        log.error("Failed to read config!", e);
                    }
			    }
			});
			if (Thread.currentThread().isInterrupted()) {
                    Walker.setTerminate(true);
			}
		} catch (SQLException e) {
			log.error("Failed to query database!", e);
		}
	}
	
	private void setHash(String hash) {
	    this.hash = hash;
	}
	
	private String getHash() {
	    return this.hash;
	}
}
