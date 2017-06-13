CREATE USER 'gdis_db_user'@'localhost' IDENTIFIED BY 'abcd1234';
GRANT ALL PRIVILEGES ON * . * TO 'gdis_db_user'@'localhost';
CREATE DATABASE gdis_db;
USE gdis_db;
