DROP TABLE IF EXISTS duplicates;
DROP TABLE IF EXISTS files;

CREATE TABLE files
(
 record_id INT PRIMARY KEY AUTO_INCREMENT,
 file_path VARCHAR(2000) UNIQUE,
 file_hash VARCHAR(128)
);

CREATE TABLE duplicates
(
 dupe1_id INT,
 dupe2_id INT,
 FOREIGN KEY (dupe1_id) REFERENCES files(record_id),
 FOREIGN KEY (dupe2_id) REFERENCES files(record_id)
);
