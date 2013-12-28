package DupesReport;

/**
 * Created by tracehagan on 12/27/13.
 */

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import org.apache.log4j.Logger;

import net.snakedoc.jutils.Config;
import net.snakedoc.jutils.ConfigException;
import net.snakedoc.jutils.timer.MilliTimer;
import net.snakedoc.jutils.database.H2;

public class Main {
    private static final Logger log = Logger.getLogger(Main.class);

    public static void main( String args[]){
        Config config = new Config("props/superD.properties");
        config.loadConfig("props/log4j.properties");
        log.info("\n\n");
        log.info("Starting program!");
        H2 db = null;
        try{
            db = Database.getInstance();
            try {
                db.openConnection();
            } catch (ClassNotFoundException | SQLException e) {
                log.fatal("Failed to open database connection! Check config file!", e);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        }
}

