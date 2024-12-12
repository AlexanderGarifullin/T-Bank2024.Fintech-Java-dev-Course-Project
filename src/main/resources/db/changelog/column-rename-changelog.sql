--liquibase formatted sql

--changeset alexandergarifullin:6
ALTER TABLE security.t_users RENAME COLUMN "с_time" TO "c_time";

--changeset alexandergarifullin:7
ALTER TABLE security.t_tokens RENAME COLUMN "с_time" TO "c_time";

--changeset alexandergarifullin:8
ALTER TABLE codeforces.t_cf_users_teams_groups RENAME COLUMN "с_time" TO "c_time";

--changeset alexandergarifullin:9
ALTER TABLE codeforces.t_cf_teams RENAME COLUMN "с_time" TO "c_time";

--changeset alexandergarifullin:10
ALTER TABLE codeforces.t_cf_users_groups RENAME COLUMN "с_time" TO "c_time";

--changeset alexandergarifullin:11
ALTER TABLE codeforces.cf_users RENAME COLUMN "с_time" TO "c_time";
