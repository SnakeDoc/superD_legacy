/*******************************************************************************
 *  Copyright 2013 Jason Sipula and Trace Hagan                                *
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.snakedoc.jutils.database.H2;

public class DedupeSQL {
	
	public void writeRecord(String file, String hash) {
		
		H2 db = null;
		try {
			db = new H2();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sqlInsert = "INSERT INTO files VALUES (? , ?)";
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
			psInsert.executeQuery();	
		} catch (SQLException e) {
			// convert to log out (error)
			e.printStackTrace();
		}
	}
}
