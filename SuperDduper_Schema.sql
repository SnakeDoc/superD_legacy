/*SuperDduper Database Schema and Common Statements*/

/*database name: superDduper.db*/

CREATE TABLE files
(
 record_id INTEGER PRIMARY KEY AUTOINCREMENT,
 file_path VARCHAR2 UNIQUE,
 file_hash char(64)
);

CREATE TABLE duplicates
(
 dupe1_id INTEGER,
 dupe2_id INTEGER,
 FOREIGN KEY (dupe1_id) REFERENCES files(record_id),
 FOREIGN KEY (dupe2_id) REFERENCES files(record_id)
);

/* Utility Statements */
/*
INSERT INTO files (file_path, file_hash) VALUES (? , ?);

DROP TABLE files;

DELETE FROM files;
*/
