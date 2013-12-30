package DupesReport;

/**
 * Created by tracehagan on 12/27/13.
 */

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.*;
import java.io.*;
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
                log.debug("Opened connection");
            } catch (ClassNotFoundException | SQLException e) {
                log.fatal("Failed to open database connection! Check config file!", e);
            }
            String sqlReport = "SELECT file_path, file_size FROM files INNER JOIN duplicates ON files.record_id = duplicates.dupe1_id ORDER BY file_size DESC";
            PreparedStatement psReport = null;
            ResultSet rsReport = null;
            try {
                psReport = db.getConnection().prepareStatement(sqlReport);
            } catch (Exception e){
                e.printStackTrace();
            }
            try {
                rsReport = psReport.executeQuery();
                log.debug("executed query");
                PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("report.txt"), "utf-8")));
                //TODO MAKE REPORT

                while (rsReport != null && rsReport.next()){
                    log.debug(rsReport.getString("file_size"));
                    writer.write(rsReport.getString("file_path") + " | " + rsReport.getString("file_size") + "\n");

                }
                writer.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        }
}

