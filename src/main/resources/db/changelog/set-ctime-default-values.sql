--liquibase formatted sql

--changeset alexandergarifullin:19
ALTER TABLE security.t_users ALTER COLUMN c_time SET DEFAULT CURRENT_TIMESTAMP;

--changeset alexandergarifullin:20
ALTER TABLE security.t_tokens ALTER COLUMN c_time SET DEFAULT CURRENT_TIMESTAMP;

--changeset alexandergarifullin:21
ALTER TABLE codeforces.t_cf_users_teams_groups ALTER COLUMN c_time SET DEFAULT CURRENT_TIMESTAMP;

--changeset alexandergarifullin:22
ALTER TABLE codeforces.t_cf_teams ALTER COLUMN c_time SET DEFAULT CURRENT_TIMESTAMP;

--changeset alexandergarifullin:23
ALTER TABLE codeforces.t_cf_users_groups ALTER COLUMN c_time SET DEFAULT CURRENT_TIMESTAMP;

--changeset alexandergarifullin:24
ALTER TABLE codeforces.cf_users ALTER COLUMN c_time SET DEFAULT CURRENT_TIMESTAMP;

