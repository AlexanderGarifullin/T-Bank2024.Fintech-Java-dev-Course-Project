package com.cf.cfteam.controllers.codeforces;

import com.cf.cfteam.models.entities.codeforces.Team;
import com.cf.cfteam.repositories.jpa.codeforces.TeamRepository;
import com.cf.cfteam.transfer.payloads.codeforces.TeamPayload;
import com.cf.cfteam.transfer.responses.codeforces.TeamResponse;
import com.cf.cfteam.utils.codeforces.mappers.TeamMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.cf.cfteam.BaseIntegrationTest;
import com.cf.cfteam.models.entities.codeforces.Group;
import com.cf.cfteam.models.entities.security.Role;
import com.cf.cfteam.models.entities.security.User;
import com.cf.cfteam.repositories.jpa.codeforces.GroupRepository;
import com.cf.cfteam.repositories.jpa.security.UserRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ActiveProfiles("test")
public class TeamControllerIntegrationTest extends BaseIntegrationTest {

    private static final String URI = "/api/cf/teams";

    private static final Long BAD_TEAM_ID = 9999L;
    private static final Long BAD_GROUP_ID = 9999L;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamMapper teamMapper;

    @Test
    @SneakyThrows
    public void getAllGroupsByUser_notEmpty_success() {
        User user = createUser("getAllGroupsByUser_notEmpty_success");
        user = userRepository.save(user);

        Group group = createGroup(user);
        group = groupRepository.save(group);

        Team team = createTeam(group);
        team = teamRepository.save(team);

        var mvcResponse = mockMvc.perform(get(URI + "/group/" + group.getId())
                        .header("Authorization", userBearerToken))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        var teams = objectMapper.readValue(
                mvcResponse.getContentAsString(),
                new TypeReference<List<TeamResponse>>() {
                }
        );

        assertThat(teams).hasSize(1)
                .contains(teamMapper.fromEntityToResponse(team));

        deleteTeamFromDb(team);
        deleteGroupFromDb(group);
    }

    @Test
    @SneakyThrows
    void getAllTeamsByGroup_shouldThrowGroupNotFoundException_whenGroupNotFound() {
        mockMvc.perform(get(URI + "/group/" + BAD_GROUP_ID)
                        .header("Authorization", userBearerToken))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.details.id").value(BAD_GROUP_ID),
                        jsonPath("$.message").value("id.not_found"),
                        jsonPath("$.error").value("Not Found"),
                        jsonPath("$.status").value(404)
                );
    }

    @Test
    @SneakyThrows
    public void getTeamById_notEmpty() {
        User user = createUser("getTeamById_notEmpty");
        user = userRepository.save(user);

        Group group = createGroup(user);
        group = groupRepository.save(group);

        Team team = createTeam(group);
        team = teamRepository.save(team);

        var mvcResponse = mockMvc.perform(get(URI + "/" + team.getId())
                        .header("Authorization", userBearerToken))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        var response = objectMapper.readValue(mvcResponse.getContentAsString(), TeamResponse.class);

        assertThat(response).isEqualTo(teamMapper.fromEntityToResponse(team));

        deleteTeamFromDb(team);
        deleteGroupFromDb(group);
    }

    @Test
    @SneakyThrows
    void getTeamById_shouldThrowTeamNotFoundException_whenTeamNotFoundE() {
        mockMvc.perform(get(URI + "/" + BAD_TEAM_ID)
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
    public void addTeamToGroup_success() {
        User user = createUser("addTeamToGroup_success");
        user = userRepository.save(user);

        Group group = createGroup(user);
        group = groupRepository.save(group);

        var payload = createTeamPayload();

        var mvcResponse = mockMvc.perform(post(URI + "/group/" + group.getId())
                        .header("Authorization", userBearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        var response = objectMapper.readValue(mvcResponse.getContentAsString(), TeamResponse.class);
        var teamFromDb = teamRepository.findById(response.id());

        assertAll(
                () -> assertThat(teamFromDb).isPresent(),
                () -> assertThat(response.description()).isEqualTo(payload.description()),
                () -> assertThat(response.name()).isEqualTo(payload.name())
        );

        deleteTeamFromDb(teamFromDb.get());
        deleteGroupFromDb(group);
    }

    @Test
    @SneakyThrows
    void addTeamToGroup_shouldThrowGroupNotFoundException_whenGroupNotFound() {
        var payload = createTeamPayload();

        mockMvc.perform(post(URI + "/group/" + BAD_GROUP_ID)
                        .header("Authorization", userBearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.details.id").value(BAD_GROUP_ID),
                        jsonPath("$.message").value("id.not_found"),
                        jsonPath("$.error").value("Not Found"),
                        jsonPath("$.status").value(404)
                );
    }

    @Test
    @SneakyThrows
    public void updateTeam_success() {
        User user = createUser("updateTeam_success");
        user = userRepository.save(user);

        Group group = createGroup(user);
        group = groupRepository.save(group);

        var payload = createTeamPayload("new name", "new description");

        Team team = createTeam(group);
        team = teamRepository.save(team);


        var mvcResponse = mockMvc.perform(put(URI + "/" + team.getId())
                        .header("Authorization", userBearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();


        var response = objectMapper.readValue(mvcResponse.getContentAsString(), TeamResponse.class);
        var teamFromDb = teamRepository.findById(response.id());

        assertAll(
                () -> assertThat(teamFromDb).isPresent(),
                () -> assertThat(teamFromDb.get().getDescription()).isEqualTo(payload.description()),
                () -> assertThat(teamFromDb.get().getName()).isEqualTo(payload.name())
        );

        deleteTeamFromDb(teamFromDb.get());
        deleteGroupFromDb(group);
    }

    @Test
    @SneakyThrows
    void updateTeam_shouldThrowTeamNotFoundException_whenTeamNotFound() {
        var payload = createTeamPayload();

        mockMvc.perform(put(URI + "/" + BAD_TEAM_ID)
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
    public void deleteTeam_success() {
        User user = createUser("deleteTeam_success");
        user = userRepository.save(user);

        Group group = createGroup(user);
        group = groupRepository.save(group);

        Team team = createTeam(group);
        team = teamRepository.save(team);

        mockMvc.perform(delete(URI + "/" + group.getId())
                        .header("Authorization", userBearerToken))
                .andExpectAll(
                        status().isNoContent(),
                        content().string("")
                );

        var teamFromDb = teamRepository.findById(team.getId());

        assertThat(teamFromDb).isNotPresent();

        deleteGroupFromDb(group);
    }

    @Test
    @SneakyThrows
    public void deleteAllTeamsByGroup_success() {
        User user = createUser("deleteAllTeamsByGroup_success");
        user = userRepository.save(user);

        Group group = createGroup(user);
        group = groupRepository.save(group);

        Team team1 = createTeam(group);
        team1 = teamRepository.save(team1);

        Team team2 = createTeam(group);
        team2 = teamRepository.save(team2);

        mockMvc.perform(delete(URI + "/group/" + group.getId())
                        .header("Authorization", userBearerToken))
                .andExpectAll(
                        status().isNoContent(),
                        content().string("")
                );

        var teamFromDb1 = teamRepository.findById(team1.getId());
        var teamFromDb2 = teamRepository.findById(team2.getId());

        assertAll(
                () -> assertThat(teamFromDb1).isNotPresent(),
                () -> assertThat(teamFromDb2).isNotPresent()
        );

        var mvcResponse = mockMvc.perform(get(URI + "/group/" + group.getId())
                        .header("Authorization", userBearerToken))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        var teams = objectMapper.readValue(
                mvcResponse.getContentAsString(),
                new TypeReference<List<TeamResponse>>() {
                }
        );

        assertThat(teams).isEmpty();

        deleteGroupFromDb(group);
    }


    private TeamPayload createTeamPayload() {
        return TeamPayload.builder()
                .name("name")
                .description("description")
                .build();
    }

    private TeamPayload createTeamPayload(String name, String description) {
        return TeamPayload.builder()
                .name(name)
                .description(description)
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


    private void deleteTeamFromDb(Team team) {
        teamRepository.delete(team);
    }

    private void deleteGroupFromDb(Group group) {
        groupRepository.delete(group);
    }

    private void deleteUserFromDb(User user) {
        userRepository.delete(user);
    }
}
