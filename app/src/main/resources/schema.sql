ALTER TABLE IF EXISTS urlcheck DROP CONSTRAINT urlId IF EXISTS;
DROP TABLE IF EXISTS urlcheck;
DROP TABLE IF EXISTS urls;

CREATE TABLE urls
(id        BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
address    VARCHAR(255),
created_at TIMESTAMP NOT NULL DEFAULT NOW());

CREATE TABLE UrlCheck
(id         BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
urlId       BIGINT REFERENCES urls(id) ON DELETE CASCADE NOT NULL,
statusCode  INT,
title       VARCHAR(255),
h1          VARCHAR(255),
description TEXT,
created_at  TIMESTAMP NOT NULL DEFAULT NOW());

