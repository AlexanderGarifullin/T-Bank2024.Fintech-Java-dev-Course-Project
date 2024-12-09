--liquibase formatted sql

--changeset alexandergarifullin:1
CREATE SCHEMA IF NOT EXISTS codeforces;

--changeset alexandergarifullin:2
CREATE TABLE codeforces.t_cf_users_teams_groups
(
    id            BIGSERIAL PRIMARY KEY,
    c_name        TEXT      NOT NULL,
    c_description TEXT,
    с_time        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    c_user_id     BIGINT    NOT NULL,
    CONSTRAINT fk_team_group_user FOREIGN KEY (c_user_id) REFERENCES security.t_users (id)
);

--changeset alexandergarifullin:3
CREATE TABLE codeforces.t_cf_teams
(
    id                  BIGSERIAL PRIMARY KEY,
    c_name              TEXT      NOT NULL,
    c_description       TEXT,
    c_first_user_login  TEXT,
    c_second_user_login TEXT,
    c_third_user_login  TEXT,
    с_time              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    c_group_id          BIGINT    NOT NULL,
    CONSTRAINT fk_cf_team_team_group FOREIGN KEY (c_group_id ) REFERENCES codeforces.t_cf_users_teams_groups (id)
);

--changeset alexandergarifullin:4
CREATE TABLE codeforces.t_cf_users_groups
(
    id            BIGSERIAL PRIMARY KEY,
    c_name        TEXT      NOT NULL,
    c_description TEXT,
    с_time        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    c_user_id     BIGINT    NOT NULL,
    CONSTRAINT fk_group_user FOREIGN KEY (c_user_id) REFERENCES security.t_users (id)
);

--changeset alexandergarifullin:5
CREATE TABLE codeforces.cf_users
(
    id                  BIGSERIAL PRIMARY KEY,
    c_name              TEXT      NOT NULL,
    c_description       TEXT,
    с_time              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    c_group_id          BIGINT    NOT NULL,
    CONSTRAINT fk_cf_user_group FOREIGN KEY (c_group_id) REFERENCES codeforces.t_cf_users_groups (id)
);

