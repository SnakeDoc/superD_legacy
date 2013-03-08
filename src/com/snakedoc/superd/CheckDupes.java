/*******************************************************************************
 *  Copyright 2013 Jason Sipula                                                *
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

package com.snakedoc.superd;

import java.sql.ResultSet;

import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;
import com.vanomaly.jutils.SQLiteUtils;

public class CheckDupes {
	public static void main(String[] args) {
		SQLiteUtils sqlA = new SQLiteUtils();
		CheckDupes cd = new CheckDupes();
		sqlA.connectDB();
		cd.checkDupes();
		sqlA.closeDB();
	}
	public void checkDupes() {
		String sqlCount = "SELECT COUNT(file_hash) FROM files;";
		String sqlGetHashes = "SELECT file_hash, file_path FROM files;";
		String sqlCompare = "SELECT file_hash, file_path FROM files " +
				"WHERE file_hash = ? AND file_path NOT LIKE ? ;";
		SQLiteStatement psCount = null;
		SQLiteStatement psGetHashes = null;
		SQLiteStatement psCompare = null;
		ResultSet rsCount = null;
		ResultSet rsGetHashes = null;
		ResultSet rsCompare = null;
		DeDupeObj[] deDupeObj = null;
		int hash_count = 0;
//		String[] hashes = null;
		int loopCounter = 0;
		int duplicateCounter = 0;
		try {
			psCount = com.vanomaly.jutils.SQLiteUtils.db.prepare(sqlCount);
			psGetHashes = com.vanomaly.jutils.SQLiteUtils.db.prepare(sqlGetHashes);
			psCompare = com.vanomaly.jutils.SQLiteUtils.db.prepare(sqlCompare);
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
		try {
			psCount.step();
			hash_count = psCount.columnInt(0);
			psCount.reset();
			psCount.dispose();
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
//		hashes = new String[hash_count];
		deDupeObj = new DeDupeObj[hash_count];
		try {
			while(psGetHashes.step()) {
				deDupeObj[loopCounter] = new DeDupeObj();
				deDupeObj[loopCounter].filehash = psGetHashes.columnString(0);
				deDupeObj[loopCounter].filepath = psGetHashes.columnString(1);
				//hashes[loopCounter] = psGetHashes.columnString(0);
				loopCounter++;
			}
			psGetHashes.reset();
			psGetHashes.dispose();
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < deDupeObj.length; i++) {
			try {
				psCompare.bind(1, deDupeObj[i].filehash);
				psCompare.bind(2, deDupeObj[i].filepath);
				//psCompare.step();
				if(psCompare.step()) {
					System.out.println("DUPLICATE FOUND!");
					duplicateCounter++;
					System.out.println(deDupeObj[i].filepath + " | " + deDupeObj[i].filehash);
					System.out.print(psCompare.columnString(1));
					System.out.print(" | ");
					System.out.println(psCompare.columnString(0));
				}
				psCompare.reset();
			} catch (SQLiteException e) {
				e.printStackTrace();
				continue;
			}
		}
		try {
			psCompare.reset();
			psCompare.dispose();
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
		System.out.println("Number of Duplicates Found: " + duplicateCounter);
	}
}
