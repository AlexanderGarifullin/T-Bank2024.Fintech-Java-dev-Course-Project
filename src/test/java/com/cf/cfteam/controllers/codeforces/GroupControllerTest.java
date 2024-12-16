package com.cf.cfteam.controllers.codeforces;

import com.cf.cfteam.models.entities.codeforces.Group;
import com.cf.cfteam.services.codeforces.GroupService;
import com.cf.cfteam.transfer.payloads.codeforces.GroupPayload;
import com.cf.cfteam.transfer.responses.codeforces.GroupResponse;
import com.cf.cfteam.utils.codeforces.mappers.GroupMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;


@ActiveProfiles("test")
class GroupControllerTest {

    @InjectMocks
    private GroupController groupController;

    @Mock
    private GroupMapper groupMapper;

    @Mock
    private GroupService groupService;

    private GroupPayload groupPayload;
//    private Group group;
    private GroupResponse groupResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

//        group = Group.builder()
//                .name("Test Group")
//                .description("Test description")
//                .user(null)
//                .build();

        groupPayload = GroupPayload.builder()
                .name("Test Group")
                .description("Test description")
                .build();

        groupResponse = GroupResponse.builder()
                .name("Test Group")
                .description("Test description")
                .teams(null)
                .build();
    }

    @Test
    void shouldReturnGroupsByUserId() {
        when(groupService.getAllGroupsByUser(1L)).thenReturn(List.of(groupResponse));

        List<GroupResponse> groups = groupController.getAllGroupsByUser(1L, null).getBody();

        verify(groupService, times(1)).getAllGroupsByUser(1L);

        assertThat(groups)
                .isNotNull()
                .hasSize(1)
                .contains(groupResponse);
    }

    @Test
    void shouldReturnGroupById() {
        when(groupService.getGroupById(1L)).thenReturn(groupResponse);

        GroupResponse result = groupController.getGroupById(1L, null).getBody();

        verify(groupService, times(1)).getGroupById(1L);
        assertThat(result).isEqualTo(groupResponse);
    }

    @Test
    void shouldAddGroupToUser() {
        when(groupService.addGroupToUser(1L, groupPayload)).thenReturn(groupResponse);

        GroupResponse result = groupController.addGroupToUser(1L, groupPayload, null).getBody();

        verify(groupService, times(1)).addGroupToUser(1L, groupPayload);

        assertThat(result).isEqualTo(groupResponse);
    }

    @Test
    void shouldUpdateGroup() {
        when(groupService.updateGroup(1L, groupPayload)).thenReturn(groupResponse);

        GroupResponse result = groupController.updateGroup(1L, groupPayload, null).getBody();

        verify(groupService, times(1)).updateGroup(1L, groupPayload);
        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.name()).isEqualTo(groupPayload.name()),
                () -> assertThat(result.description()).isEqualTo(groupPayload.description())
        );
    }

    @Test
    void shouldDeleteGroup() {
        groupController.deleteGroup(1L, null);

        verify(groupService, times(1)).deleteGroup(1L);
    }

    @Test
    void shouldDeleteAllGroupsByUser() {
        groupController.deleteAllGroupsByUser(1L, null);

        verify(groupService, times(1)).deleteAllGroupsByUser(1L);
    }
}

