--liquibase formatted sql

--changeset alexandergarifullin:25
ALTER TABLE security.t_users DROP COLUMN IF EXISTS c_time;

--changeset alexandergarifullin:26
ALTER TABLE security.t_tokens DROP COLUMN IF EXISTS c_time;

--changeset alexandergarifullin:27
ALTER TABLE codeforces.t_cf_users_teams_groups DROP COLUMN IF EXISTS c_time;

--changeset alexandergarifullin:28
ALTER TABLE codeforces.t_cf_teams DROP COLUMN IF EXISTS c_time;

--changeset alexandergarifullin:29
ALTER TABLE codeforces.t_cf_users_groups DROP COLUMN IF EXISTS c_time;

--changeset alexandergarifullin:30
ALTER TABLE codeforces.cf_users DROP COLUMN IF EXISTS c_time;
