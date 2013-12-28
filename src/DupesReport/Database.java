/*******************************************************************************
 *  Copyright 2013 Brian Zitzow                                                *
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

package DupesReport;

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
