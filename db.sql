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
ON a.memberId = m.id
ORDER BY a.id DESC;

ALTER TABLE article ADD COLUMN hit INT(10) UNSIGNED NOT NULL;
SELECT * FROM article;

SELECT a.*, m.name AS extra_writer
FROM article AS a
LEFT JOIN `member` AS m
ON a.memberId = m.id
WHERE a.title LIKE '%d%';

SELECT * FROM `member`;
DESC article;

INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
memberId = 1,
title = '제목1',
`body` = '내용1',
hit = 30;

INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
memberId = 1,
title = '제목2',
`body` = '내용2',
hit = 50;

INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
memberId = 1,
title = '제목3',
`body` = '내용3',
hit = 10;

INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
memberId = 1,
title = '제목4',
`body` = '내용4',
hit = 305;

INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
memberId = 1,
title = '제목5',
`body` = '내용5',
hit = 60;

SELECT * FROM article;

## getArticles 페이징
SELECT a.*, m.name AS extra_writer
FROM article AS a
LEFT JOIN `member` AS m
ON a.memberId = m.id
ORDER BY a.id DESC
LIMIT 15, 5;

SELECT COUNT(*)
FROM article
WHERE title LIKE '%d%';

## like 테이블 생성
## liketype 1 = 추천, 2 = 반대
CREATE TABLE `like` (
    id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    PRIMARY KEY(id),
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    articleId INT(10) UNSIGNED NOT NULL,
    memberId INT(10) UNSIGNED NOT NULL,
    likeType TINYINT(1) NOT NULL
);

DROP TABLE `like`;

DESC `like`;
SELECT * FROM `like`;
SELECT * FROM article;
SELECT * FROM `member`;

## like 했는지 체크
## 추천/비추천 했다면 해당하는 likeType 값 리턴
## 추천/비추천 안했다면 0 리턴
SELECT
CASE WHEN COUNT(*) != 0
THEN likeType ELSE 0 END
FROM `like`
WHERE articleId = 69 AND memberId = 1;

SELECT COUNT(*)
FROM `like`
WHERE articleId = 69 AND likeType = 2;

SELECT *
FROM `like`
WHERE articleId = 6;

## comment 테이블 생성
CREATE TABLE `comment` (
    id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    PRIMARY KEY(id),
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    articleId INT(10) UNSIGNED NOT NULL,
    memberId INT(10) UNSIGNED NOT NULL,
    title CHAR(100) NOT NULL,
    `body` CHAR(100) NOT NULL
);

DESC `comment`;
SELECT * FROM `comment`;

SELECT COUNT(*)
FROM `comment`
WHERE id = 3 AND articleId = 69;

