package com.cf.cfteam.controllers.codeforces;

import com.fasterxml.jackson.core.type.TypeReference;
import com.cf.cfteam.BaseIntegrationTest;
import com.cf.cfteam.models.entities.codeforces.Group;
import com.cf.cfteam.models.entities.security.Role;
import com.cf.cfteam.models.entities.security.User;
import com.cf.cfteam.repositories.jpa.codeforces.GroupRepository;
import com.cf.cfteam.repositories.jpa.security.UserRepository;
import com.cf.cfteam.transfer.payloads.codeforces.GroupPayload;
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
public class GroupControllerIntegrationTest extends BaseIntegrationTest {

    private static final String URI = "/api/cf/groups";

    private static final Long BAD_USER_ID = 9999L;
    private static final Long BAD_GROUP_ID = 9999L;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @SneakyThrows
    public void getAllGroupsByUser_notEmpty_success() {
        User user = createUser();
        user = userRepository.save(user);

        Group group = createGroup(user);
        group = groupRepository.save(group);


        var mvcResponse = mockMvc.perform(get(URI + "/user/" + user.getId())
                        .header("Authorization", userBearerToken))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        var groups = objectMapper.readValue(
                mvcResponse.getContentAsString(),
                new TypeReference<List<Group>>() {
                }
        );

        assertThat(groups).hasSize(1)
                        .contains(group);

        deleteGroupFromDb(group);
        deleteUserFromDb(user);
    }

    @Test
    @SneakyThrows
    void getAllGroupsByUser_shouldThrowUserNotFound_whenUserNotFound() {
        mockMvc.perform(get(URI + "/user/" + BAD_USER_ID)
                        .header("Authorization", userBearerToken))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.details.id").value(BAD_USER_ID),
                        jsonPath("$.message").value("id.not_found"),
                        jsonPath("$.error").value("Not Found"),
                        jsonPath("$.status").value(404)
                );
    }

    @Test
    @SneakyThrows
    public void getGroupById_notEmpty() {
        User user = createUser();
        user = userRepository.save(user);

        Group group = createGroup(user);
        group = groupRepository.save(group);

        var mvcResponse = mockMvc.perform(get(URI + "/" + group.getId())
                        .header("Authorization", userBearerToken))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        var responseGroupe = objectMapper.readValue(mvcResponse.getContentAsString(), Group.class);

        assertThat(responseGroupe).isEqualTo(group);

        deleteGroupFromDb(group);;
    }

    @Test
    @SneakyThrows
    void getGroupById_shouldThrowGroupNotFound_whenGroupNotFound() {
        mockMvc.perform(get(URI + "/" + BAD_USER_ID)
                        .header("Authorization", userBearerToken))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.details.id").value(BAD_USER_ID),
                        jsonPath("$.message").value("id.not_found"),
                        jsonPath("$.error").value("Not Found"),
                        jsonPath("$.status").value(404)
                );
    }

    @Test
    @SneakyThrows
    public void addGroupToUser_success() {
        User user = createUser();
        user = userRepository.save(user);

        var payload = createGroupPayload();

        var mvcResponse = mockMvc.perform(post(URI + "/user/" + user.getId())
                        .header("Authorization", userBearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        var responseGroupe = objectMapper.readValue(mvcResponse.getContentAsString(), Group.class);
        var groupFromDb = groupRepository.findById(responseGroupe.getId());

        assertAll(
                () -> assertThat(groupFromDb).isPresent(),
                () -> assertThat(groupFromDb.get()).isEqualTo(responseGroupe),
                () -> assertThat(responseGroupe.getDescription()).isEqualTo(payload.description()),
                () -> assertThat(responseGroupe.getName()).isEqualTo(payload.name())
        );

        deleteGroupFromDb(groupFromDb.get());
        deleteUserFromDb(user);
    }

    @Test
    @SneakyThrows
    void addGroupToUser_shouldThrowUserNotFound_whenUserNotFound() {
        var payload = createGroupPayload();

        mockMvc.perform(post(URI + "/user/" + BAD_USER_ID)
                        .header("Authorization", userBearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.details.id").value(BAD_USER_ID),
                        jsonPath("$.message").value("id.not_found"),
                        jsonPath("$.error").value("Not Found"),
                        jsonPath("$.status").value(404)
                );
    }

    @Test
    @SneakyThrows
    public void updateGroup_success() {
        User user = createUser();
        user = userRepository.save(user);

        var payload = createGroupPayload("new name", "new description");

        Group group = createGroup(user);
        group = groupRepository.save(group);


        var mvcResponse = mockMvc.perform(put(URI + "/" + group.getId())
                        .header("Authorization", userBearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();


        var responseGroupe = objectMapper.readValue(mvcResponse.getContentAsString(), Group.class);
        var groupFromDb = groupRepository.findById(responseGroupe.getId());

        assertAll(
                () -> assertThat(groupFromDb).isPresent(),
                () -> assertThat(groupFromDb.get().getDescription()).isEqualTo(payload.description()),
                () -> assertThat(groupFromDb.get().getName()).isEqualTo(payload.name())
        );

        deleteGroupFromDb(groupFromDb.get());
        deleteUserFromDb(user);
    }

    @Test
    @SneakyThrows
    void updateGroup_shouldThrowGroupNotFound_whenGroupNotFound() {
        var payload = createGroupPayload();

        mockMvc.perform(put(URI + "/" + BAD_GROUP_ID)
                        .header("Authorization", userBearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.details.id").value(BAD_USER_ID),
                        jsonPath("$.message").value("id.not_found"),
                        jsonPath("$.error").value("Not Found"),
                        jsonPath("$.status").value(404)
                );
    }

    @Test
    @SneakyThrows
    public void deleteGroup_success() {
        User user = createUser();
        user = userRepository.save(user);

        Group group = createGroup(user);
        group = groupRepository.save(group);

        mockMvc.perform(delete(URI + "/" + group.getId())
                        .header("Authorization", userBearerToken))
                .andExpectAll(
                        status().isNoContent(),
                        content().string("")
                );

        var groupFromDb = groupRepository.findById(group.getId());

        assertThat(groupFromDb).isNotPresent();

        deleteUserFromDb(user);
    }

    @Test
    @SneakyThrows
    public void deleteAllGroupsByUser_success() {
        User user = createUser();
        user = userRepository.save(user);

        Group group1 = createGroup(user);
        group1 = groupRepository.save(group1);

        Group group2 = createGroup(user);
        group2 = groupRepository.save(group2);

        mockMvc.perform(delete(URI + "/user/" + user.getId())
                        .header("Authorization", userBearerToken))
                .andExpectAll(
                        status().isNoContent(),
                        content().string("")
                );

        var groupFromDb1 = groupRepository.findById(group1.getId());
        var groupFromDb2 = groupRepository.findById(group2.getId());

        assertAll(
                () -> assertThat(groupFromDb1).isNotPresent(),
                () -> assertThat(groupFromDb2).isNotPresent()
        );

        var mvcResponse = mockMvc.perform(get(URI + "/user/" + user.getId())
                        .header("Authorization", userBearerToken))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        var groups = objectMapper.readValue(
                mvcResponse.getContentAsString(),
                new TypeReference<List<Group>>() {
                }
        );

        assertThat(groups).isEmpty();

        deleteUserFromDb(user);
    }

    private User createUser() {
        return User.builder()
                .name("User name")
                .login("User login")
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

    private GroupPayload createGroupPayload() {
        return GroupPayload.builder()
                .name("Test group")
                .description("Test description")
                .build();
    }

    private GroupPayload createGroupPayload(String name, String description) {
        return GroupPayload.builder()
                .name(name)
                .description(description)
                .build();
    }

    private void deleteUserFromDb(User user) {
        userRepository.delete(user);
    }

    private void deleteGroupFromDb(Group group) {
        groupRepository.delete(group);
    }
}
