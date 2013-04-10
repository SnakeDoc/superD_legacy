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
import java.io.IOException;
import java.sql.SQLException;

import net.snakedoc.jutils.Config;
import net.snakedoc.jutils.ConfigException;
import net.snakedoc.jutils.timer.MilliTimer;
import net.snakedoc.jutils.database.H2;
import net.snakedoc.jutils.io.Hasher;
import net.snakedoc.jutils.io.HasherException;
import net.snakedoc.jutils.system.SysInfo;;

public class DedupeR {

	/* THIS IS DEBUG MAIN()
	 * This should not be left in for deployment, only for development
	 * Logic in main() should be moved to it's own driver method so it can be invoked
	 * via scripts.
	 */
	public static void main (String[] args) {
		
		// get instance of MilliTimer()
		MilliTimer timer = new MilliTimer();
		
		// start timer
		timer.startTimer();
		
		// get instance of other helper objects
		Config config = new Config("props/superD.properties");
		H2 db = null;
		try {
			try {
                db = new H2(config.getConfig("H2_dbURL"), config.getConfig("H2_dbUser"), config.getConfig("H2_dbPort"));
            } catch (ConfigException e) {
                // TODO log out (fatal)
                e.printStackTrace();
            }
		
		SysInfo sys = new SysInfo();
	//	DedupeSQL sql = new DedupeSQL();
		// TODO fix CheckDedupes class
//		CheckDupes check = new CheckDupes();
		
		// this needs to be fixed so that the user passes
		// in the argument for hashVer...
		//
		// also, a better name for hashVer is probably hashAlgo
		// since it's not a version, its an algorithm we are selecting
		//
		// options being MD2, MD5, SHA1, SHA-256, SHA-384, SHA-512
		// there is a method in Hasher (from jutils) that will validate
		// the user input to ensure it's a supported hash algorithm.
		String hashVer = "SHA-";
		if (sys.getCPUArch().contains("64")) {
			hashVer += "512";
		} else {
			hashVer += "256";
		}
		
		try {
			db.openConnection();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO change to log out (fatal)
			e.printStackTrace();
		}
		setup();
		// TODO fix checkDedupes()
//		check.checkDupes();
		try {
			db.closeConnection();
		} catch (SQLException e) {
			// TODO change to log out (warning)
			e.printStackTrace();
		}
		
		// stop timer
		timer.stopTimer();
		// TODO change to log out (info)
		System.out.println("Total Runtime: " + timer.getTime());
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	/* END DEBUG MAIN() */
	
	
	public static void setup() {
		
	    // load program propterties
	    Config config = new Config("props/superD.properties");
	    
		/* TODO should not be limited to 1 directory to scan
		 * should allow deduping multiple root directories.
		 * this would be useful for deduping data sets that are
		 * on two or more drives.
		 */
		File[] rootDirs = new File[1];
		
		// TODO change to user specified root directory
		try {
            rootDirs[0] = new File(config.getConfig("ROOT"));
        } catch (ConfigException e) {
            // TODO log out (fatal)
            e.printStackTrace();
        }
		walk(rootDirs[0]);
	}
	
	/*proof of concept walker, notifies of nullpointers when occurred. Seems to work fully now */
	public static void walk(File path){
		
		int i=0;

		File[] contents = path.listFiles();
		
		for (File curFile : contents){
			try{
				if (curFile.isDirectory() && (curFile != null) && !curFile.isHidden()){
					walk(curFile);
				} else if (!curFile.isDirectory() && !curFile.isHidden() && curFile != null ){
					/*hash file here and store to SQL database*/
					/*String hash = Hasher.hash(curFile.getPath());*/
					/*saveHash(hash, curFile.getPatch()); */
					System.out.println("Touched: " + curFile.getPath());
					Hasher hasher = new Hasher();
					try {
                        System.out.println("Hash: " + hasher.getHash(curFile.getPath(), "SHA-512"));
                    } catch (IOException | HasherException e) {
                        // log out (warning)
                        e.printStackTrace();
                    }
				}	
			} catch (NullPointerException e) {
				// TODO change to log out (warning)
				e.printStackTrace();
				System.out.println("i: " + i + "  |  path: " + contents[i].getPath() + 
						"  |  pathcalled: " + path.getPath());
			}
		}
	}
}
