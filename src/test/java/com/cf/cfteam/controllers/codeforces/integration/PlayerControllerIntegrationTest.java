package com.cf.cfteam.controllers.codeforces.integration;

import com.cf.cfteam.BaseIntegrationTest;
import com.cf.cfteam.models.entities.codeforces.Group;
import com.cf.cfteam.models.entities.codeforces.Player;
import com.cf.cfteam.models.entities.codeforces.Team;
import com.cf.cfteam.models.entities.security.Role;
import com.cf.cfteam.models.entities.security.User;
import com.cf.cfteam.repositories.jpa.codeforces.GroupRepository;
import com.cf.cfteam.repositories.jpa.codeforces.PlayerRepository;
import com.cf.cfteam.repositories.jpa.codeforces.TeamRepository;
import com.cf.cfteam.repositories.jpa.security.UserRepository;
import com.cf.cfteam.transfer.payloads.codeforces.PlayerPayload;
import com.cf.cfteam.transfer.responses.codeforces.PlayerResponse;
import com.cf.cfteam.utils.codeforces.mappers.PlayerMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.benmanes.caffeine.cache.LoadingCache;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@ActiveProfiles("test")
class PlayerControllerIntegrationTest extends BaseIntegrationTest {

    private static final String URI = "/api/cf/players";

    private static final Long BAD_TEAM_ID = 9999L;
    private static final Long BAD_PLAYER_ID = 9999L;


    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlayerMapper playerMapper;

    @MockBean
    private LoadingCache<String, Double> ratingCache;

    @Test
    @SneakyThrows
    public void getAllPlayersByTeam_notEmpty_success() {
        User user = createUser("getAllPlayersByTeam_notEmpty_success");
        user = userRepository.save(user);

        Group group = createGroup(user);
        group = groupRepository.save(group);

        Team team = createTeam(group);
        team = teamRepository.save(team);

        Player player = createPlayer();
        player = playerRepository.save(player);

        team.setPlayers(List.of(player));
        team = teamRepository.save(team);

        when(ratingCache.get(player.getLogin())).thenReturn(0.);

        var mvcResponse = mockMvc.perform(get(URI + "/team/" + team.getId())
                        .header("Authorization", userBearerToken))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        var players = objectMapper.readValue(
                mvcResponse.getContentAsString(),
                new TypeReference<List<PlayerResponse>>() {
                }
        );

        assertThat(players).hasSize(1)
                .contains(playerMapper.fromEntityToResponse(player));

        deletePlayer(player);
        deleteTeamFromDb(team);
        deleteGroupFromDb(group);
        deleteUserFromDb(user);
    }

    @Test
    @SneakyThrows
    void getAllPlayersByTeam_shouldThrowTeamNotFoundException_whenTeamNotFoundException() {
        mockMvc.perform(get(URI + "/team/" + BAD_TEAM_ID)
                        .header("Authorization", userBearerToken))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.details.id").value(BAD_TEAM_ID),
                        jsonPath("$.message").value("id.not_found"),
                        jsonPath("$.error").value("Not Found"),
                        jsonPath("$.status").value(404)
                );
    }

    @Test
    @SneakyThrows
    public void getPlayerById_notEmpty() {
        Player player = createPlayer();
        player = playerRepository.save(player);
        when(ratingCache.get(player.getLogin())).thenReturn(0.);

        var mvcResponse = mockMvc.perform(get(URI + "/" + player.getId())
                        .header("Authorization", userBearerToken))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        var response = objectMapper.readValue(mvcResponse.getContentAsString(), PlayerResponse.class);

        assertThat(response).isEqualTo(playerMapper.fromEntityToResponse(player));

        deletePlayer(player);
    }

    @Test
    @SneakyThrows
    void getPlayerById_shouldThrowPlayerNotFoundException_whenPlayerNotFound() {
        mockMvc.perform(get(URI + "/" + BAD_PLAYER_ID)
                        .header("Authorization", userBearerToken))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.details.id").value(BAD_TEAM_ID),
                        jsonPath("$.message").value("id.not_found"),
                        jsonPath("$.error").value("Not Found"),
                        jsonPath("$.status").value(404)
                );
    }

    @Test
    @SneakyThrows
    @Transactional
    public void addPlayerToTeam_success() {

        User user = createUser("addPlayerToTeam_success");
        user = userRepository.save(user);

        Group group = createGroup(user);
        group = groupRepository.save(group);

        Team team = createTeam(group);
        team = teamRepository.save(team);

        var payload = createPlayerPayload();

        when(ratingCache.get(payload.login())).thenReturn(0.);

        var mvcResponse = mockMvc.perform(post(URI + "/team/" + team.getId())
                        .header("Authorization", userBearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        var response = objectMapper.readValue(mvcResponse.getContentAsString(), PlayerResponse.class);
        var playerFromDb = playerRepository.findById(response.id());

        assertAll(
                () -> assertThat(playerFromDb).isPresent(),
                () -> assertThat(response.login()).isEqualTo(payload.login()),
                () -> assertThat(response.rating()).isEqualTo(0.),
                () -> assertThat(playerFromDb.get().getTeams()).hasSize(1)
        );

        deletePlayer(playerFromDb.get());
        deleteTeamFromDb(team);
        deleteGroupFromDb(group);
        deleteUserFromDb(user);
    }

    @Test
    @SneakyThrows
    void addPlayerToTeam_shouldThrowTeamNotFoundException_whenTeamNotFound() {
        var payload = createPlayerPayload();

        mockMvc.perform(post(URI + "/team/" + BAD_TEAM_ID)
                        .header("Authorization", userBearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.details.id").value(BAD_TEAM_ID),
                        jsonPath("$.message").value("id.not_found"),
                        jsonPath("$.error").value("Not Found"),
                        jsonPath("$.status").value(404)
                );
    }

    @Test
    @SneakyThrows
    void addPlayerToTeam_shouldThrowPlayerAlreadyInTeamException_whenPlayerAlreadyInTeam() {
        User user = createUser("addPlayerToTeam_shouldThrowPlayerAlreadyInTeamException_whenPlayerAlreadyInTeam");
        user = userRepository.save(user);

        Group group = createGroup(user);
        group = groupRepository.save(group);

        Team team = createTeam(group);
        team = teamRepository.save(team);

        Player player = createPlayer();
        player = playerRepository.save(player);

        team.setPlayers(List.of(player));
        team = teamRepository.save(team);

        var payload = createPlayerPayload();

        mockMvc.perform(post(URI + "/team/" + team.getId())
                        .header("Authorization", userBearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpectAll(
                        status().isConflict(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.details.login").value(player.getLogin()),
                        jsonPath("$.message").value("player.already_in_team"),
                        jsonPath("$.error").value("Conflict"),
                        jsonPath("$.status").value(409)
                );

        deletePlayer(player);
        deleteTeamFromDb(team);
        deleteGroupFromDb(group);
        deleteUserFromDb(user);
    }

    @Test
    @SneakyThrows
    @Transactional
    public void updatePlayerInTeam_success() {
        User user = createUser("updatePlayerInTeam_success");
        user = userRepository.save(user);

        Group group = createGroup(user);
        group = groupRepository.save(group);

        Team team = createTeam(group);
        team = teamRepository.save(team);

        Player player = createPlayer();
        player = playerRepository.save(player);

        team.setPlayers(new ArrayList<>(List.of(player)));
        team = teamRepository.save(team);

        var payload = createPlayerPayload("awoo");

        when(ratingCache.get(payload.login())).thenReturn(0.);

        var mvcResponse = mockMvc.perform(put(URI + "/players/" + player.getId() +
                        "/teams/" + team.getId())
                        .header("Authorization", userBearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();


        var response = objectMapper.readValue(mvcResponse.getContentAsString(), PlayerResponse.class);
        var playerFromDb = playerRepository.findById(response.id());

        assertAll(
                () -> assertThat(playerFromDb).isPresent(),
                () -> assertThat(response.login()).isEqualTo(payload.login()),
                () -> assertThat(response.rating()).isEqualTo(0.),
                () -> assertThat(playerFromDb.get().getTeams()).hasSize(1)
        );

        deletePlayer(player);
        deletePlayer(playerFromDb.get());
        deleteTeamFromDb(team);
        deleteGroupFromDb(group);
        deleteUserFromDb(user);
    }

    @Test
    @SneakyThrows
    void updatePlayerInTeam_shouldThrowPlayerNotFoundException_whenPlayerNotFound() {
        var payload = createPlayerPayload();

        var mvcResponse = mockMvc.perform(put(URI + "/players/" + BAD_PLAYER_ID +
                        "/teams/" + 1L)
                        .header("Authorization", userBearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.details.id").value(BAD_PLAYER_ID),
                        jsonPath("$.message").value("id.not_found"),
                        jsonPath("$.error").value("Not Found"),
                        jsonPath("$.status").value(404)
                );
    }


    @Test
    @SneakyThrows
    void updatePlayerInTeam_shouldThrowTeamNotFoundException_whenTeamNotFound() {
        Player player = createPlayer();
        player = playerRepository.save(player);

        var payload = createPlayerPayload();

        var mvcResponse = mockMvc.perform(put(URI + "/players/" + player.getId() +
                        "/teams/" + BAD_TEAM_ID)
                        .header("Authorization", userBearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.details.id").value(BAD_TEAM_ID),
                        jsonPath("$.message").value("id.not_found"),
                        jsonPath("$.error").value("Not Found"),
                        jsonPath("$.status").value(404)
                );

        deletePlayer(player);
    }

    @Test
    @SneakyThrows
    void updatePlayerInTeam_shouldThrowPlayerNotFromTeamException_whenPlayerNotFromTeam() {
        User user = createUser("updatePlayerInTeam_shouldThrowPlayerNotFromTeamException_whenPlayerNotFromTeam");
        user = userRepository.save(user);

        Group group = createGroup(user);
        group = groupRepository.save(group);

        Team team = createTeam(group);
        team = teamRepository.save(team);

        Player player = createPlayer();
        player = playerRepository.save(player);

        var payload = createPlayerPayload();

        var mvcResponse = mockMvc.perform(put(URI + "/players/" + player.getId() +
                        "/teams/" + team.getId())
                        .header("Authorization", userBearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpectAll(
                        status().isConflict(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.details.playerId").value(player.getId()),
                        jsonPath("$.details.teamId").value(team.getId()),
                        jsonPath("$.message").value("player.not_from_team"),
                        jsonPath("$.error").value("Conflict"),
                        jsonPath("$.status").value(409)
                );

        deletePlayer(player);
        deleteTeamFromDb(team);
        deleteGroupFromDb(group);
        deleteUserFromDb(user);
    }

    @Test
    @SneakyThrows
    @Transactional
    public void deletePlayerFromTeam_success() {
        User user = createUser("deletePlayerFromTeam");
        user = userRepository.save(user);

        Group group = createGroup(user);
        group = groupRepository.save(group);

        Team team = createTeam(group);
        team = teamRepository.save(team);

        Player player = createPlayer();
        player = playerRepository.save(player);

        team.setPlayers(new ArrayList<>(List.of(player)));
        team = teamRepository.save(team);

        mockMvc.perform(delete(URI + "/players/" + player.getId() +
                        "/teams/" + team.getId())
                        .header("Authorization", userBearerToken))
                .andExpectAll(
                        status().isNoContent(),
                        content().string("")
                );

        var teamFromDb = teamRepository.findById(team.getId());
        var playerFromDb = playerRepository.findById(player.getId());

        assertAll(
                () -> assertThat(teamFromDb).isPresent(),
                () -> assertThat(teamFromDb.get().getPlayers()).isEmpty(),
                () -> assertThat(playerFromDb).isPresent(),
                () -> assertThat(playerFromDb.get().getTeams()).isEmpty()
        );

        deletePlayer(player);
        deleteTeamFromDb(team);
        deleteGroupFromDb(group);
        deleteUserFromDb(user);
    }

    @Test
    @SneakyThrows
    void deletePlayerFromTeam_shouldThrowPlayerNotFoundException_whenPlayerNotFound() {
        mockMvc.perform(delete(URI + "/players/" + BAD_PLAYER_ID+
                        "/teams/" + 1L)
                        .header("Authorization", userBearerToken))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.details.id").value(BAD_TEAM_ID),
                        jsonPath("$.message").value("id.not_found"),
                        jsonPath("$.error").value("Not Found"),
                        jsonPath("$.status").value(404)
                );
    }

    @Test
    @SneakyThrows
    void deletePlayerFromTeam_shouldThrowTeamNotFoundException_whenTeamNotFound() {
        Player player = createPlayer();
        player = playerRepository.save(player);


        mockMvc.perform(delete(URI + "/players/" + BAD_PLAYER_ID+
                        "/teams/" + BAD_TEAM_ID)
                        .header("Authorization", userBearerToken))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.details.id").value(BAD_TEAM_ID),
                        jsonPath("$.message").value("id.not_found"),
                        jsonPath("$.error").value("Not Found"),
                        jsonPath("$.status").value(404)
                );

        deletePlayer(player);
    }

    @Test
    @SneakyThrows
    @Transactional
    public void deleteAllPlayersFromTeam_success() {
        User user = createUser("deleteAllTeamsByGroup_success");
        user = userRepository.save(user);

        Group group = createGroup(user);
        group = groupRepository.save(group);

        Team team = createTeam(group);

        Player player1 = createPlayer("p1");
        Player player2 = createPlayer("p2");

        player1.setTeams(new ArrayList<>(List.of(team)));
        player2.setTeams(new ArrayList<>(List.of(team)));

        player1 = playerRepository.save(player1);
        player2 = playerRepository.save(player2);

        mockMvc.perform(delete(URI + "/team/" + team.getId())
                        .header("Authorization", userBearerToken))
                .andExpectAll(
                        status().isNoContent(),
                        content().string("")
                );

        var teamFromDb = teamRepository.findById(team.getId());
        var playerFromDb1 = playerRepository.findById(player1.getId());
        var playerFromDb2 = playerRepository.findById(player2.getId());

        assertAll(
                () -> assertThat(teamFromDb).isPresent(),
                () -> assertThat(playerFromDb1).isPresent(),
                () -> assertThat(playerFromDb2).isPresent(),
                () -> assertThat(teamFromDb.get().getPlayers()).isEmpty(),
                () -> assertThat(playerFromDb1.get().getTeams()).hasSize(1),
                () -> assertThat(playerFromDb2.get().getTeams()).hasSize(1)
        );

        var mvcResponse = mockMvc.perform(get(URI + "/team/" + team.getId())
                        .header("Authorization", userBearerToken))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        var players = objectMapper.readValue(
                mvcResponse.getContentAsString(),
                new TypeReference<List<PlayerResponse>>() {
                }
        );

        assertThat(players).isEmpty();

        deletePlayer(player1);
        deletePlayer(player2);
        deleteTeamFromDb(team);
        deleteGroupFromDb(group);
        deleteUserFromDb(user);;
    }

    @Test
    @SneakyThrows
    void deleteAllPlayersFromTeam_shouldThrowTeamNotFoundException_whenTeamNotFound() {
        mockMvc.perform(delete(URI + "/team/" + BAD_TEAM_ID)
                        .header("Authorization", userBearerToken))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.details.id").value(BAD_TEAM_ID),
                        jsonPath("$.message").value("id.not_found"),
                        jsonPath("$.error").value("Not Found"),
                        jsonPath("$.status").value(404)
                );
    }


    private PlayerPayload createPlayerPayload() {
        return PlayerPayload.builder()
                .login("tourist")
                .build();
    }

    private PlayerPayload createPlayerPayload(String login) {
        return PlayerPayload.builder()
                .login(login)
                .build();
    }

    private User createUser(String login) {
        return User.builder()
                .name("Team controller User name")
                .login(login)
                .hashedPassword("Password")
                .role(Role.USER)
                .build();
    }

    private Group createGroup(User user) {
        return Group.builder()
                .name("Test Group")
                .description("Test description")
                .user(user)
                .build();
    }

    private Team createTeam(Group group) {
        return Team.builder()
                .name("team")
                .description("team description")
                .group(group)
                .build();
    }

    private Player createPlayer() {
        return Player.builder()
                .login("tourist")
                .build();
    }

    private Player createPlayer(String login) {
        return Player.builder()
                .login(login)
                .build();
    }

    private void deleteTeamFromDb(Team team) {
        teamRepository.delete(team);
    }

    private void deleteGroupFromDb(Group group) {
        groupRepository.delete(group);
    }

    private void deleteUserFromDb(User user) {
        userRepository.delete(user);
    }

    private void deletePlayer(Player player) {
        for (Team team : player.getTeams()) {
            team.getPlayers().remove(player);
        }

        var teams = List.copyOf(player.getTeams());
        player.setTeams(List.of());

        teamRepository.saveAll(teams);
        playerRepository.delete(player);
    }
}