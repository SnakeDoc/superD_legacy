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

package net.snakedoc.superd.filescan;

import net.snakedoc.jutils.Config;
import net.snakedoc.jutils.ConfigException;
import net.snakedoc.jutils.io.Hasher;
import net.snakedoc.jutils.io.HasherException;
import net.snakedoc.superd.data.DedupeSQL;
import net.snakedoc.superd.launcher.DedupeR;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class Walker {
    private int BUFFER;
    private final Logger log = Logger.getLogger(DedupeR.class);
    private final Hasher hasher = new Hasher();
    private final DedupeSQL sql = new DedupeSQL();
    private final Config config = new Config("props/superD.properties");
    private String hashAlgo = "";
    
    private static boolean terminate = false;

    public Walker(int buffer){
        BUFFER = buffer;
        try {
            this.hashAlgo = config.getConfig("HASH_ALGO");
        } catch (ConfigException e) {
            log.error("Failed to read config!", e);
        }
    }
    
    public static void setTerminate(boolean bol) {
        terminate = bol;
    }
    
    public static boolean getTerminate() {
        return terminate;
    }

    public void walk(File path){
        File[] contents = path.listFiles();

        for (File curFile : contents){
            if (! Walker.getTerminate()) { 
                try{
                    if (isValidDirectory(curFile)){
                        walk(curFile);
                    } else if (isValidFile(curFile)){
                        hashFile(curFile);
                    }
                } catch (Exception e) {
                    log.warn("Failed to access file!", e);
                    log.debug("path: " + curFile.getPath() +
                            "  |  pathcalled: " + path.getPath());
                }
            } else {
                break;
            }
            try {
                Thread.sleep(25); // short timeout to not block
            } catch (InterruptedException e) {
                if (Walker.getTerminate()) {
                    break;
                }
            }
        }
    }

    private boolean isValidDirectory(File curFile){
        return curFile.isDirectory() && curFile != null && !curFile.isHidden();
    }

    private boolean isValidFile(File curFile){
        return !curFile.isDirectory() && curFile != null && !curFile.isHidden();
    }

    private void hashFile(File curFile){
        try{
        String file = "";
        String hash = "";
        try {
            file = curFile.getPath();
            log.debug("File: " + file);
            hash = hasher.getHash(curFile.getPath(), this.getHashAlgo(), BUFFER);
        } catch (IOException | HasherException e1) {
            log.error("Failed to access and/or hash file!", e1);
        }
        sql.writeRecord(file, hash);
        log.debug("\n\t Hash: " + hash);
        }catch( Exception e){
            log.warn("Something went wrong!", e);
        }
    }
    
    private String getHashAlgo() {
        return this.hashAlgo;
    }
}
