--liquibase formatted sql

--changeset alexandergarifullin:25
ALTER TABLE codeforces.t_cf_users_teams_groups RENAME TO t_groups;

--changeset alexandergarifullin:26
DROP TABLE IF EXISTS codeforces.t_cf_teams CASCADE;

--changeset alexandergarifullin:27
DROP TABLE IF EXISTS codeforces.t_cf_users_groups CASCADE;

--changeset alexandergarifullin:28
DROP TABLE IF EXISTS codeforces.cf_users CASCADE;

--changeset alexandergarifullin:29
CREATE TABLE codeforces.t_players
(
    id                  BIGSERIAL PRIMARY KEY,
    c_login             TEXT      NOT NULL UNIQUE
);

--changeset alexandergarifullin:31
CREATE TABLE codeforces.t_teams
(
    id                  BIGSERIAL PRIMARY KEY,
    c_name              TEXT      NOT NULL,
    c_description       TEXT      NULL,
    c_group_id          BIGINT    NOT NULL,
    CONSTRAINT fk_team_group FOREIGN KEY (c_group_id ) REFERENCES codeforces.t_groups (id)
);

--changeset alexandergarifullin:32
CREATE TABLE codeforces.t_team_player_match
(
    id                  BIGSERIAL PRIMARY KEY,
    c_description       TEXT      NOT NULL,
    c_team_id           BIGINT    NOT NULL,
    c_player_id         BIGINT    NOT NULL,
    CONSTRAINT fk_match_team FOREIGN KEY (c_team_id) REFERENCES codeforces.t_teams (id),
    CONSTRAINT fk_match_player FOREIGN KEY (c_player_id) REFERENCES codeforces.t_players (id)
);




