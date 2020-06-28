DROP DATABASE IF EXISTS test;
CREATE DATABASE IF NOT EXISTS test;

USE test;

CREATE TABLE IF NOT EXISTS user (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL
);

DROP USER IF EXISTS 'basic'@'%';
CREATE USER IF NOT EXISTS 'basic'@'%' IDENTIFIED BY 'basic';
GRANT INSERT, SELECT ON test.user TO 'basic'@'%';

FLUSH PRIVILEGES;
