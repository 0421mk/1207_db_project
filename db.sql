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

alter table article add column hit int(10) unsigned not null;
select * from article;

SELECT a.*, m.name AS extra_writer
FROM article AS a
LEFT JOIN `member` AS m
ON a.memberId = m.id
where a.title like '%d%';

select * from `member`;
desc article;

insert into article
set regDate = now(),
updateDate = now(),
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

select * from article;

## getArticles 페이징
SELECT a.*, m.name AS extra_writer
FROM article AS a
LEFT JOIN `member` AS m
ON a.memberId = m.id
ORDER BY a.id DESC
limit 15, 5;

select count(*)
from article
WHERE title LIKE '%d%';

## like 테이블 생성
## liketype 1 = 추천, 2 = 반대
create table `like` (
    id int(10) unsigned not null auto_increment,
    primary key(id),
    regDate datetime not null,
    updateDate datetime not null,
    articleId int(10) unsigned not null,
    memberId int(10) unsigned not null,
    likeType tinyint(1) not null
);

drop table `like`;

desc `like`;
select * from `like`;
select * from article;
select * from `member`;

## like 했는지 체크
## 추천/비추천 했다면 해당하는 likeType 값 리턴
## 추천/비추천 안했다면 0 리턴
select
case when count(*) != 0
then likeType else 0 end
from `like`
where articleId = 69 and memberId = 1;

select COUNT(*)
from `like`
where articleId = 69 and likeType = 2;

SELECT *
FROM `like`
WHERE articleId = 6;

## comment 테이블 생성
create table `comment` (
    id int(10) unsigned not null auto_increment,
    primary key(id),
    regDate datetime not null,
    updateDate datetime not null,
    articleId int(10) unsigned not null,
    memberId int(10) unsigned not null,
    title char(100) not null,
    `body` char(100) not null
);

desc `comment`;
select * from `comment`;

select count(*)
from `comment`
where id = 3 and articleId = 69;

select c.*, m.name as extra_writer
from `comment` c
inner join `member` m
on c.memberId = m.id
where articleId = 69
limit 5, 5;