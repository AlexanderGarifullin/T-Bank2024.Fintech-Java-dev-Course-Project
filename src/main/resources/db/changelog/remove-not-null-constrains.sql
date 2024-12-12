--liquibase formatted sql

--changeset alexandergarifullin:12
ALTER TABLE security.t_users
    ALTER COLUMN c_time DROP NOT NULL;

--changeset alexandergarifullin:13
ALTER TABLE security.t_tokens
    ALTER COLUMN c_time DROP NOT NULL;

--changeset alexandergarifullin:14
ALTER TABLE codeforces.t_cf_users_teams_groups
    ALTER COLUMN c_time DROP NOT NULL;

--changeset alexandergarifullin:15
ALTER TABLE codeforces.t_cf_teams
    ALTER COLUMN c_time DROP NOT NULL;

--changeset alexandergarifullin:16
ALTER TABLE codeforces.t_cf_users_groups
    ALTER COLUMN c_time DROP NOT NULL;

--changeset alexandergarifullin:17
ALTER TABLE codeforces.cf_users
    ALTER COLUMN c_time DROP NOT NULL;

