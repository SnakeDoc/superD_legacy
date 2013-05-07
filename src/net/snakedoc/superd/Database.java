package net.snakedoc.superd;

import net.snakedoc.jutils.Config;
import net.snakedoc.jutils.ConfigException;
import net.snakedoc.jutils.database.H2;

import java.io.File;

/**
 * Singleton Pattern
 *
 */
public class Database {
    private volatile static H2 uniqueInstance;

    public static H2 getInstance() throws ConfigException {
        if (Database.uniqueInstance == null) {
            synchronized (Database.class) {
                if (Database.uniqueInstance == null) {

                    /**
                     * Load configuration
                     */
                    Config config = new Config("props/superD.properties");
                    config.loadConfig("props/log4j.properties");
                    File h2Url = new File(config.getConfig("H2_dbURL"));

                    /**
                     * Instantiate First and Only instance of H2
                     */
                    try {
                        Database.uniqueInstance = new H2(h2Url.getAbsolutePath(),
                                config.getConfig("H2_dbUser"),
                                config.getConfig("H2_dbPass"));
                    } catch (ConfigException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }
        }
        return Database.uniqueInstance;
    }

    private Database() {}
}
