DROP TABLE IF EXISTS duplicates;
DROP TABLE IF EXISTS files;

CREATE TABLE files
(
 record_id BIGINT(19) PRIMARY KEY AUTO_INCREMENT,
 file_path VARCHAR(2000) UNIQUE,
 file_hash VARCHAR(128),
 file_size BIGINT(19)
);

CREATE TABLE duplicates
(
 record_id BIGINT(10) PRIMARY KEY AUTO_INCREMENT,
 dupe1_id BIGINT(19),
 dupe2_id BIGINT(19),
 FOREIGN KEY (dupe1_id) REFERENCES files(record_id),
 FOREIGN KEY (dupe2_id) REFERENCES files(record_id)
);
