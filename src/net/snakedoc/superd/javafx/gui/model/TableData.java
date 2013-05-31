/*******************************************************************************
 *  Copyright (c) 2013 superD contributors, snakedoc.net and others            *
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

package net.snakedoc.superd.javafx.gui.model;

import javafx.beans.property.SimpleStringProperty;

public class TableData {
    
    private SimpleStringProperty fileName;
    private SimpleStringProperty directory;
    private SimpleStringProperty size;
    private SimpleStringProperty hashAlgo;
    private SimpleStringProperty fileHash;
    
    /**
     * Default Constructor
     */
    public TableData() {   
    }
    
    /**
     * Argument Constructor
     * 
     * @param fileName File Name
     * @param directory Directory File is located
     * @param size Format: FileSize UnitOfMeasurement
     *  , Example: 408 MB , 453 B , 8584 KB, 5 GB
     * @param hashAlgo Hash algorithm used
     * @param fileHash File's Hashed Output
     */
    public TableData(String fileName, String directory,
            String size, String hashAlgo, String fileHash) {
        
        this.fileName = new SimpleStringProperty(fileName);
        this.directory = new SimpleStringProperty(directory);
        this.size = new SimpleStringProperty(size);
        this.hashAlgo = new SimpleStringProperty(hashAlgo);
        this.fileHash = new SimpleStringProperty(fileHash);
        
    }
    
    public String getFileName() {
        return this.fileName.get();
    }
    
    public void setFileName(String fileName) {
        this.fileName.set(fileName);
    }
    
    public String getDirectory() {
        return this.directory.get();
    }
    
    public void setDirectory(String directory) {
        this.directory.set(directory);
    }
    
    public String getSize() {
        return this.size.get();
    }
    
    public void setSize(String size) {
        this.size.set(size);
    }
    
    public String getHashAlgo() {
        return this.hashAlgo.get();
    }
    
    public void setHashAlgo(String hashAlgo) {
        this.hashAlgo.set(hashAlgo);
    }
    
    public String getFileHash() {
        return this.fileHash.get();
    }
    
    public void setFileHash(String fileHash) {
        this.fileHash.set(fileHash);
    }
    
}
