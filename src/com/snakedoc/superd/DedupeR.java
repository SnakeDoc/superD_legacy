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

import java.io.File;
//import java.io.FileNotFoundException;
import java.io.IOException;

import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;
import com.vanomaly.jutils.*;

public class DedupeR {
	static int count = 0;
	static int batchSize = 500;
	public static void main (String[] args) throws IOException {
		MilliTimer timer = new MilliTimer();
		timer.startTimer();
		DeDupeSQL sql = new DeDupeSQL();
		CheckDupes check = new CheckDupes();
		String hashVer = "SHA-";
		if (DetectCPU.getCPUArch().contains("64")) {
			hashVer += "512";
		} else {
			hashVer += "256";
		}
		sql.connectDB();
		setup();
		check.checkDupes();
		sql.connectDB();
		setup();
		sql.closeDB();
		timer.stopTimer();
		System.out.println("Total Runtime: " + timer.getTime());
	}
	public static void setup() {
		//File[] rootDirs = DetectMachineInfo.getRoot();
		File[] rootDirs = new File[1];
		rootDirs[0] = new File("/home/jason");
		DeDupeObj[] deDupeObj = new DeDupeObj[DedupeR.batchSize];
		String ps1 = "INSERT INTO files VALUES (? , ?);";
		SQLiteStatement st = null;
		try {
			st = com.vanomaly.jutils.SQLiteUtils.db.prepare(ps1);
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
		walk(rootDirs, deDupeObj, st);
	}
	
	/*proof of concept walker, notifies of nullpointers when occurred. Seems to work fully now */
	public static void walk(File path){
		
		int i=0;

		File[] contents = path.listFiles();
		
		for (File curFile : contents){
		try{
			if (curFile.isDirectory() && (curFile != null)){
				walk(curFile);
			}else if(!curFile.isDirectory() && !curFile.isHidden() && curFile != null ){
				System.out.println("Touched: " + curFile.getPath());
			}	
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			System.out.println("i: " + i + "  |  path: " + contents[i].getPath() + "  |  pathcalled: " + path.getPath());
			continue;
		}
		}
	}
}
