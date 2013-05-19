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
