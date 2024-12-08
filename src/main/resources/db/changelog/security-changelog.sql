--liquibase formatted sql

--changeset alexandergarifullin:1
CREATE SCHEMA IF NOT EXISTS security;

--changeset alexandergarifullin:2
CREATE TABLE security.t_users
(
    id                  BIGSERIAL   PRIMARY KEY,
    c_name              TEXT        NOT NULL,
    c_login             TEXT        UNIQUE NOT NULL,
    c_hashed_password   TEXT        NOT NULL,
    c_role              TEXT        NOT NULL,
    с_time              TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

--changeset alexandergarifullin:3
CREATE INDEX IF NOT EXISTS idx_users_login ON security.t_users (c_login);

--changeset alexandergarifullin:4
CREATE TABLE security.t_tokens
(
    id            BIGSERIAL     PRIMARY KEY,
    c_token       TEXT          NOT NULL,
    c_revoked     BOOLEAN       NOT NULL,
    с_time        TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    c_user_id     BIGINT        NOT NULL,
    CONSTRAINT fk_token_user FOREIGN KEY (c_user_id) REFERENCES security.t_users (id)
);