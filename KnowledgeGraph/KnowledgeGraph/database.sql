DROP DATABASE IF EXISTS KG;

CREATE DATABASE IF NOT EXISTS KG;

USE KG;

DROP TABLE IF EXISTS user;

CREATE TABLE IF NOT EXISTS user
(
    id         INT(11)      NOT NULL AUTO_INCREMENT,
    username   VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    createTime TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updateTime TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);
