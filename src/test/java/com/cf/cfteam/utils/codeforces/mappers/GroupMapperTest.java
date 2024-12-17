package com.cf.cfteam.utils.codeforces.mappers;

import com.cf.cfteam.models.entities.codeforces.Group;
import com.cf.cfteam.models.entities.security.Role;
import com.cf.cfteam.models.entities.security.User;
import com.cf.cfteam.transfer.payloads.codeforces.GroupPayload;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class GroupMapperTest {

    @InjectMocks
    private GroupMapper groupMapper;

    @Test
    void shouldMapPayloadToEntity() {
        GroupPayload payload = GroupPayload.builder()
                .name("GroupName")
                .description("GroupDescription")
                .build();
        User user = User.builder()
                .name("User name")
                .login("User login")
                .hashedPassword("Password")
                .role(Role.USER)
                .build();


        Group group = groupMapper.fromPayloadToEntity(payload, user);

        assertAll(
                () -> assertThat(group).isNotNull(),
                () -> assertThat(group.getName()).isEqualTo(payload.name()),
                () -> assertThat(group.getDescription()).isEqualTo(payload.description()),
                () -> assertThat(group.getUser()).isEqualTo(user)
        );
    }

    @Test
    void shouldUpdateEntityFromPayload() {
        Group group = Group.builder()
                .name("Test Group")
                .description("Test description")
                .build();

        GroupPayload payload = GroupPayload.builder()
                .name("NewName")
                .description("NewDescription")
                .build();

        Group updatedGroup = groupMapper.updateEntityFromPayload(group, payload);

        assertAll(
                () -> assertThat(updatedGroup).isNotNull(),
                () -> assertThat(updatedGroup.getName()).isEqualTo(payload.name()),
                () -> assertThat(updatedGroup.getDescription()).isEqualTo(payload.description())
        );
    }
}