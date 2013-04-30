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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import org.apache.log4j.Logger;

import net.snakedoc.jutils.Config;
import net.snakedoc.jutils.ConfigException;
import net.snakedoc.jutils.timer.MilliTimer;
import net.snakedoc.jutils.database.H2;
import net.snakedoc.jutils.io.Hasher;
import net.snakedoc.jutils.io.HasherException;
import net.snakedoc.jutils.system.SysInfo;

public class DedupeR {

    private final static int BUFFER = 16384; // Buffer size in Bytes to use in hashing algorithm.
                                             // Has the potential to directly impact performance.
    
    private static final Logger log = Logger.getLogger(DedupeR.class);
    
	/* DEGIN DEBUG MAIN() */
	public static void main (String[] args) {
		DedupeR d = new DedupeR();
		d.driver();
	}
	/* END DEBUG MAIN() */
	
	public void driver() {
	    // get instance of MilliTimer()
        MilliTimer timer = new MilliTimer();
        
        // start timer
        timer.startTimer();
        
        // get instance of other helper objects
        Config config = new Config("props/superD.properties");
        config.loadConfig("props/log4j.properties");
        log.info("\n\n");
        log.info("Starting program!");
        H2 db = null;
        try {
            log.debug(new File(config.getConfig("H2_dbURL")).getAbsolutePath());
        } catch (ConfigException e1) {
            log.error("Failed to read config file!", e1);
        }
        try {
            try {
                db = new H2(new File(config.getConfig("H2_dbURL")).getAbsolutePath(), config.getConfig("H2_dbUser"), config.getConfig("H2_dbPass"));
            } catch (ConfigException e) {
                log.fatal("Failed to read config file!", e);
            }
            
        SysInfo sys = new SysInfo();
    //  DedupeSQL sql = new DedupeSQL();
        CheckDupes check = new CheckDupes();
        
        // this needs to be fixed so that the user passes
        // in the argument for hashVer...
        //
        // also, a better name for hashVer is probably hashAlgo
        // since it's not a version, its an algorithm we are selecting
        //
        // options being MD2, MD5, SHA1, SHA-256, SHA-384, SHA-512
        // there is a method in Hasher (from jutils) that will validate
        // the user input to ensure it's a supported hash algorithm.


        /*NOT IN USE AS IT READS FROM PROPERTY FILE
        String hashVer = "SHA-";

        if (sys.getCPUArch().contains("64")) {
            hashVer += "512";
        } else {
            hashVer += "256";
        }
        */

        try {
            db.openConnection();
        } catch (ClassNotFoundException | SQLException e) {
            log.fatal("Failed to open database connection! Check config file!", e);
        }
        
        Schema s = new Schema();
        String sqlSchema = s.getSchema();
        PreparedStatement psSchema = db.getConnection().prepareStatement(sqlSchema);
        try {
            log.info("Running schema update on db: " + db.getDbPath());
            psSchema.execute();
            log.info("Schema update complete!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            db.closeConnection();
        } catch (SQLException e) {
            log.warn("Failed to close database connection!", e);
        }

        //INSTEAD OF COMMAND LINE ARGUMENTS,
        // PROMPT FOR VARIOUS SETTINGS IF
        // USER WANTS TO DO THIS INSTEAD OF
        // PROP FILE

        /*Scanner in = new Scanner(System.in);
        System.out.println("Would you like to read from prop file or enter options now?");
        System.out.print("Enter 1 to enter configuration now, 2 to use existing prop file: ");
            int choice=2;
        try{
            choice = in.nextInt();
        } catch(Exception e){
            System.out.println("Invalid input! Proceeding using property file");
        }
        if (choice == 1){
            readSetup();
        }*/

        setup();
        check.checkDupes();
        // stop timer
        timer.stopTimer();
        log.info("Total Runtime: " + timer.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	
	public static void setup() {
		
	    // load program properties
	    Config config = new Config("props/superD.properties");
	    
		/** Allow multiple root directories to scan
		 */
		ArrayList<File> rootDirs = new ArrayList<File>(1);
		
		// TODO possible add feature to have cmd line input at runtime to override ROOT dir
		try {
            String dil = new String(config.getConfig("ROOT_DIL"));
            String rootDirList = new String(config.getConfig("ROOT"));
            List<String> rootDirListArr = Arrays.asList(rootDirList.split(dil));

            for (int i=0; i < rootDirListArr.size(); i++){
                rootDirs.add(new File(rootDirListArr.get(i)));
            }
        } catch (ConfigException e) {
            log.fatal("Failed to read config file!", e);
        }
        try{
            for (int i=0; i < rootDirs.size(); i++){
                //make sure that it is a directory
                if (rootDirs.get(i).isDirectory()){
                    walk(rootDirs.get(i));
                } else {
                    log.debug(rootDirs.get(i).toString() + "  Appears to not be a directory; skipping to next in list");
                }
            }
        //TODO replace generic Exception with specific exception(s)
        //lets make sure we didn't accidentally put in an invalid path
	    }catch(Exception e){
            e.printStackTrace();
        }
    }


    /* Process command line arguments and store into properties file */
    /* CONFIG class needs to be fixed. config.setConfig overwrites entire file rather than just the specific key */

    public void readSetup(){
        //TODO read in configuration from user
        Config config = new Config("props/superD.properties");
        Scanner in = new Scanner(System.in);

        //prompt for algorithm
        System.out.println("Which hashing algorithm would you like to use?");
        System.out.println("We recommend SHA-256 for 32-bit machines and SHA-512 for 64-bit machines");
        System.out.print("Enter your choice: ");
        String input;
        input = in.nextLine();
        config.setConfig("HASH_ALGO", input);

        //prompt for ROOT_DIL
        System.out.println("Please enter a delimiter for root paths");
        System.out.println("Try to use something that won't appear in the path");
        System.out.println("We recommend ;;");
        System.out.print("Enter choice: ");
        input = in.nextLine();
        config.setConfig("ROOT_DIL", input);

        //PROMPT FOR DIRECTORIES
        System.out.println("Please enter all directories you would like scanned on one line separated by " + input);
        System.out.print("Please enter: ");
        input = in.nextLine();
        config.setConfig("ROOT", input);
    }

	/*proof of concept walker, notifies of nullpointers when occurred. Seems to work fully now */
	// Possible TODO move walk() to ScanFiles if possible if we want to?

	public static void walk(File path){
		
	    Hasher hasher = new Hasher();
	    DedupeSQL sql = new DedupeSQL();
        Config config = new Config("props/superD.properties");
		int i=0;

		File[] contents = path.listFiles();
		
		for (File curFile : contents){
			try{
				if (curFile.isDirectory() && (curFile != null) && !curFile.isHidden()){
					walk(curFile);
				} else if (!curFile.isDirectory() && !curFile.isHidden() && curFile != null ){

                    //reads in HASH_ALGO from Prop File to decide which algorithm to use
                    String hashAlgo = new String(config.getConfig("HASH_ALGO"));
				    String file = "";
				    String hash = "";
				    try {
				        file = curFile.getPath();
				        log.debug("File: " + file);
                        hash = hasher.getHash(curFile.getPath(), hashAlgo, BUFFER);
                    } catch (IOException | HasherException e1) {
                        log.error("Failed to access and/or hash file!", e1);
                    }
				    
				    sql.writeRecord(file, hash);
				    
					log.debug("\n\t Hash: " + hash);
				}	
			} catch (Exception e) {
				log.warn("Failed to access file!", e);
				log.debug("i: " + i + "  |  path: " + contents[i].getPath() + 
						"  |  pathcalled: " + path.getPath());
			}
		}
	}
}
