package com.cf.cfteam.services.codeforces;

import com.cf.cfteam.exceptions.codeforces.GroupNotFoundException;
import com.cf.cfteam.exceptions.security.UserNotFoundException;
import com.cf.cfteam.models.entities.codeforces.Group;
import com.cf.cfteam.models.entities.security.Role;
import com.cf.cfteam.models.entities.security.User;
import com.cf.cfteam.repositories.jpa.codeforces.GroupRepository;
import com.cf.cfteam.repositories.jpa.security.UserRepository;
import com.cf.cfteam.transfer.payloads.codeforces.GroupPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class GroupServiceTest {

    @InjectMocks
    private GroupService groupService;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private UserRepository userRepository;

    private User user;
    private Group group;
    private GroupPayload groupPayload;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .name("User name")
                .login("User login")
                .hashedPassword("Password")
                .role(Role.USER)
                .build();

        group = Group.builder()
                .name("Test Group")
                .description("Test description")
                .user(user)
                .build();

        groupPayload = GroupPayload.builder()
                .name("Test Group")
                .description("Test description")
                .build();
    }

    @Test
    void getAllGroupsByUser_ShouldReturnGroups_WhenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(groupRepository.findByUser(user)).thenReturn(List.of(group));

        var groups = groupService.getAllGroupsByUser(1L);

        assertThat(groups).hasSize(1)
                        .contains(group);
    }

    @Test
    void getAllGroupsByUser_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->  groupService.getAllGroupsByUser(1L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("id.not_found");
    }

    @Test
    void getGroupById_ShouldReturnGroup_WhenGroupExists() {
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));

        var result = groupService.getGroupById(1L);

        assertThat(result).isEqualTo(group);
    }

    @Test
    void getGroupById_ShouldThrowGroupNotFoundException_WhenGroupDoesNotExist() {
        when(groupRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->  groupService.getGroupById(1L))
                .isInstanceOf(GroupNotFoundException.class)
                .hasMessageContaining("id.not_found");
    }

    @Test
    void addGroupToUser_ShouldAddGroup_WhenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(groupRepository.save(any(Group.class))).thenReturn(group);

        var result = groupService.addGroupToUser(1L, groupPayload);

        assertThat(result).isEqualTo(group);
        verify(groupRepository, times(1)).save(any(Group.class));
    }

    @Test
    void addGroupToUser_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> groupService.addGroupToUser(1L, groupPayload))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("id.not_found");
    }

    @Test
    void updateGroup_ShouldUpdateGroup_WhenGroupExists() {
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(groupRepository.save(any(Group.class))).thenReturn(group);

        groupPayload = GroupPayload.builder()
                .name("NEW Test Group")
                .description("NEW Test description")
                .build();

        var result = groupService.updateGroup(1L, groupPayload);

        assertAll(
                () -> assertThat(result.getDescription()).isEqualTo(groupPayload.description()),
                () -> assertThat(result.getName()).isEqualTo(group.getName())
        );
        verify(groupRepository, times(1)).save(any(Group.class));
    }

    @Test
    void updateGroup_ShouldThrowGroupNotFoundException_WhenGroupDoesNotExist() {
        when(groupRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->  groupService.updateGroup(1L, groupPayload))
                .isInstanceOf(GroupNotFoundException.class)
                .hasMessageContaining("id.not_found");
    }

    @Test
    void deleteGroup_ShouldDeleteGroup_WhenGroupExists() {
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));

        groupService.deleteGroup(1L);

        verify(groupRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteAllGroupsByUser_ShouldDeleteAllGroups_WhenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(groupRepository.findByUser(user)).thenReturn(List.of(group));

        groupService.deleteAllGroupsByUser(1L);

        verify(groupRepository, times(1)).deleteAll(anyList());
    }

    @Test
    void deleteAllGroupsByUser_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> groupService.deleteAllGroupsByUser(1L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("id.not_found");
    }
}