DROP TABLE IF EXISTS url_checks CASCADE;
DROP TABLE IF EXISTS urls CASCADE;

CREATE TABLE urls
(id        BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
address    VARCHAR(255),
created_at TIMESTAMP NOT NULL DEFAULT NOW());

CREATE TABLE url_checks
(id         BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
urlId       BIGINT REFERENCES urls(id) ON DELETE CASCADE NOT NULL,
statusCode  INT,
title       VARCHAR(255),
h1          VARCHAR(255),
description TEXT,
created_at  TIMESTAMP NOT NULL DEFAULT NOW());