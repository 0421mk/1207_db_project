DROP DATABASE IF EXISTS text_board;

CREATE DATABASE text_board;
USE text_board;

CREATE TABLE article (
    id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    PRIMARY KEY(id),
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    title CHAR(100) NOT NULL,
    `body` TEXT NOT NULL
);

UPDATE article
SET updateDate = NOW(),
title = '안녕1',
`body` = '반가워1'
WHERE id = 6;

DESC article;
SELECT * FROM article;

SELECT COUNT(*)
FROM article
WHERE id = 3;

CREATE TABLE `member` (
    id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    PRIMARY KEY(id),
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    loginId CHAR(100) NOT NULL,
    loginPw CHAR(100) NOT NULL,
    `name` CHAR(100) NOT NULL
);

DESC `member`;

SELECT COUNT(*) FROM `member`
WHERE loginId = 'admin';

SELECT * FROM `member`;
ALTER TABLE article ADD COLUMN memberId INT(10) UNSIGNED NOT NULL AFTER updateDate;

DESC article;
SELECT * FROM article;

SELECT a.*, m.name AS extra_writer
FROM article AS a
LEFT JOIN `member` AS m
ON a.memberId = m.id
WHERE a.id = 9;

SELECT a.*, m.name AS extra_writer
FROM article AS a
LEFT JOIN `member` AS m
ON a.memberId = m.id;

ALTER TABLE article ADD COLUMN hit INT(10) UNSIGNED NOT NULL;
SELECT * FROM article;