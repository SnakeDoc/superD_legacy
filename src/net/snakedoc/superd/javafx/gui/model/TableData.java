package net.snakedoc.superd.javafx.gui.model;

public class TableData {
    
    private String fileName;
    private String directory;
    private String size;
    private String hashAlgo;
    private String fileHash;
    
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
        
        this.fileName = fileName;
        this.directory = directory;
        this.size = size;
        this.hashAlgo = hashAlgo;
        this.fileHash = fileHash;
        
    }
    
    public String getFileName() {
        return this.fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getDirectory() {
        return this.directory;
    }
    
    public void setDirectory(String directory) {
        this.directory = directory;
    }
    
    public String getSize() {
        return this.size;
    }
    
    public void setSize(String size) {
        this.size = size;
    }
    
    public String getHashAlgo() {
        return this.hashAlgo;
    }
    
    public void setHashAlgo(String hashAlgo) {
        this.hashAlgo = hashAlgo;
    }
    
    public String getFileHash() {
        return this.fileHash;
    }
    
    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }
    
}
