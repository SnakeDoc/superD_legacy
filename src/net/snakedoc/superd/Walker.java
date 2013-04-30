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

import net.snakedoc.jutils.Config;
import net.snakedoc.jutils.io.Hasher;
import net.snakedoc.jutils.io.HasherException;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class Walker {
    private static int BUFFER;
    private static final Logger log = Logger.getLogger(DedupeR.class);

    public Walker(int buffer){
        this.BUFFER = buffer;
    }

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
