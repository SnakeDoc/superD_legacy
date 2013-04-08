DROP TABLE IF EXISTS duplicates;
DROP TABLE IF EXISTS files;

CREATE TABLE files
(
 record_id INTEGER PRIMARY KEY AUTO_INCREMENT,
 file_path VARCHAR(767) UNIQUE,
 file_hash char(64)
);

CREATE TABLE duplicates
(
 dupe1_id INTEGER,
 dupe2_id INTEGER,
 FOREIGN KEY (dupe1_id) REFERENCES files(record_id),
 FOREIGN KEY (dupe2_id) REFERENCES files(record_id)
);
