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

//TODO is it possible to make these non-static without breakage?
//TODO so if we were to thread we would require separate instances of these methods i think.
public class Walker {
    private int BUFFER;
    private final Logger log = Logger.getLogger(DedupeR.class);
    private final Hasher hasher = new Hasher();
    private final DedupeSQL sql = new DedupeSQL();
    private final Config config = new Config("props/superD.properties");

    public Walker(int buffer){
        BUFFER = buffer;
    }

    public void walk(File path){
        File[] contents = path.listFiles();

        for (File curFile : contents){
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
        String hashAlgo = config.getConfig("HASH_ALGO");
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
        }catch( Exception e){
            log.warn(e.getStackTrace());
        }
    }
}
