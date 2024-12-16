--liquibase formatted sql

--changeset alexandergarifullin:33
ALTER TABLE codeforces.t_team_player_match DROP COLUMN c_description;

--changeset alexandergarifullin:34
ALTER TABLE codeforces.t_team_player_match RENAME TO t_team_player;

--changeset alexandergarifullin:35
CREATE INDEX idx_players_c_login ON codeforces.t_players(c_login);

