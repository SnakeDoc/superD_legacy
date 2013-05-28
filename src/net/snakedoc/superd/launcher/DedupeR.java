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

package net.snakedoc.superd.launcher;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import org.apache.log4j.Logger;

import net.snakedoc.jutils.Config;
import net.snakedoc.jutils.ConfigException;
import net.snakedoc.jutils.timer.MilliTimer;
import net.snakedoc.superd.Notice;
import net.snakedoc.superd.data.Database;
import net.snakedoc.superd.dedupe.CheckDupes;
import net.snakedoc.superd.filescan.Walker;
import net.snakedoc.superd.javafx.gui.ApplicationWindow;

public class DedupeR {

    private final static int BUFFER = 16384; // Buffer size in Bytes to use in hashing algorithm.
                                             // Has the potential to directly impact performance.
    
    private static final Logger log = Logger.getLogger(DedupeR.class);

	public static void main (String[] args) {
        /**
         * Initialize Database & Open Connection
         */
        try {
            Database.getInstance().openConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

		DedupeR d = new DedupeR();
		d.driver(args);

        /**
         * Close Database Connection
         */
        try {
            Database.getInstance().closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}

	
	public void driver(String[] args) {
	    // get instance of MilliTimer() for benchmarking
        MilliTimer timer = new MilliTimer();
        
        ApplicationWindow.clearData();
        
        // Open Database Connection
        try {
            Database.getInstance().openConnection();
        } catch (ClassNotFoundException | SQLException e1) {
            log.fatal("Failed to open database connection!", e1);
        }
        
        Notice notice = new Notice();
        
        // start timer
        timer.startTimer();
        
        // get instance of other helper objects
        //@ToDo: The Config object can be static. No need to instantiate.
        Config config = new Config("props/superD.properties");
        config.loadConfig("props/log4j.properties");
        log.info("\n\n");
        log.info("Starting program!");
        
        System.out.println(notice.get_superD_ascii());

        /**
         * Load Database Tables
         */
        try {
            CheckDupes check = new CheckDupes();
            Schema s = new Schema();
            String sqlSchema = s.getSchema();
            PreparedStatement psSchema = Database.getInstance().getConnection().prepareStatement(sqlSchema);
            try {
                log.info("Running schema update on db: " + Database.getInstance().getDbPath());
                psSchema.execute();
                log.info("Schema update complete!");
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (args.length<1){
                Scanner in = new Scanner(System.in);
                System.out.println("Would you like to read from prop file or enter options now?");
                System.out.print("Enter 1 to enter configuration now, 2 to use existing prop file: ");
                int choice=2;
                choice = Integer.parseInt(in.next());
                if (choice == 1){
                    readSetup();
                }
                in.close(); // close resource
            }

            //Run Setup() to do main logic, make calls to Walk()
            setup();

            //DATABASE now filled with all file hashes; time to look for duplicates
            check.checkDupes();
            // ALL DONE! stop timer
            timer.stopTimer();
            log.info("Total Runtime: " + timer.getTime());
        } catch (Exception e) { // TODO get rid of this to narrow try/catch scope for improved exception handling/recovery. log out
            e.printStackTrace();
        }
        
        // Close database connection
        try {
            Database.getInstance().closeConnection();
        } catch (SQLException e) {
            log.warn("Failed to close database connection!", e);
        }
        
	}

	public static void setup() {
		//CREATE WALKER OBJECT
        Walker walker = new Walker(BUFFER);
	    // load program properties
	    Config config = new Config("props/superD.properties");
	    
        //list of directories to scan
	    List<File> rootDirs = new ArrayList<File>();
		
        //Load in all directories to scan from properties file into rootDirs ArrayList
		try {
            String del = config.getConfig("ROOT_DEL");
            String rootDirList = config.getConfig("ROOT");
            List<String> rootDirListArr = Arrays.asList(rootDirList.split(del));

            for (int i=0; i < rootDirListArr.size(); i++){
                rootDirs.add(new File(rootDirListArr.get(i)));
            }
        } catch (ConfigException e) {
            log.fatal("Failed to read config file!", e);
        }
        //Call Walker.walk() on all the directories in rootDirs
        try{
            for (int i=0; i < rootDirs.size(); i++){
                //make sure that it is a directory
                if (rootDirs.get(i).isDirectory()){
                    walker.walk(rootDirs.get(i));
                } else {
                    log.debug(rootDirs.get(i).toString() + "  Appears to not be a directory; skipping to next in list");
                }
            }
        //TODO replace generic Exception with specific exception(s)
	    }catch(Exception e){
            e.printStackTrace();
        }
    }


    /* Process command line arguments and store into properties file */
    /* CONFIG class needs to be fixed.        */
    /*  TODO config.setConfig overwrites entire file rather than just the specific key */

    public void readSetup(){

        Config config = new Config("props/superD.properties");

        // nifty ARM block
        try (Scanner in = new Scanner(System.in);) {
            
            //prompt for algorithm
            System.out.println("Which hashing algorithm would you like to use?");
            System.out.println("We recommend SHA-256 for 32-bit machines and SHA-512 for 64-bit machines");
            System.out.print("Enter your choice: ");
            String input;
            input = in.nextLine();
            config.setConfig("HASH_ALGO", input, false);

            //prompt for ROOT_DEL
            System.out.println("Please enter a delimiter for root paths");
            System.out.println("Try to use something that won't appear in the path");
            System.out.println("We recommend ;;");
            System.out.print("Enter choice: ");
            input = in.nextLine();
            config.setConfig("ROOT_DEL", input, false);

            //PROMPT FOR DIRECTORIES
            System.out.println("Please enter all directories you would like scanned on one line separated by " + input);
            System.out.print("Please enter: ");
            input = in.nextLine();
            config.setConfig("ROOT", input, false);
        }
    }

    public void shutdown() {
        // shutdown routine. move this to a GUI class i think... 
    }
}
