package com.cf.cfteam.utils.codeforces.mappers;

import com.cf.cfteam.models.entities.codeforces.Group;
import com.cf.cfteam.models.entities.codeforces.Team;
import com.cf.cfteam.transfer.payloads.codeforces.TeamPayload;
import com.cf.cfteam.transfer.responses.codeforces.TeamResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class TeamMapperTest {

    @InjectMocks
    private TeamMapper teamMapper;

    @Test
    void shouldMapFromEntityToResponse() {
        Team team = Team.builder()
                .name("Team name")
                .description(" Team description")
                .id(1L)
                .players(null)
                .build();

        TeamResponse response = teamMapper.fromEntityToResponse(team);

        assertAll(
                () -> assertThat(response).isNotNull(),
                () -> assertThat(response.name()).isEqualTo(team.getName()),
                () -> assertThat(response.description()).isEqualTo(team.getDescription()),
                () -> assertThat(response.id()).isEqualTo(team.getId()),
                () -> assertThat(response.players()).isEqualTo(List.of()),
                () -> assertThat(response.teamRating()).isEqualTo(0L)
        );
    }

    @Test
    void shouldMapFromPayloadToEntity() {
        TeamPayload payload = TeamPayload.builder()
                .name("team name")
                .description("team description")
                .build();
        Group group = Group.builder()
                .name("group name")
                .description("group descr")
                .build();

        Team team = teamMapper.fromPayloadToEntity(payload, group);

        assertAll(
                () -> assertThat(team).isNotNull(),
                () -> assertThat(team.getGroup()).isEqualTo(group),
                () -> assertThat(team.getName()).isEqualTo(payload.name()),
                () -> assertThat(team.getDescription()).isEqualTo(payload.description())
        );
    }

    @Test
    void shouldUpdateEntityFromPayload() {
        Team team = Team.builder()
                .name("Team name")
                .description(" Team description")
                .id(1L)
                .players(null)
                .build();

        TeamPayload payload = TeamPayload.builder()
                .name("new team name")
                .description("new team description")
                .build();

        Team updatedTeam = teamMapper.updateEntityFromPayload(team, payload);

        assertAll(
                () -> assertThat(updatedTeam).isNotNull(),
                () -> assertThat(updatedTeam.getName()).isEqualTo(payload.name()),
                () -> assertThat(updatedTeam.getDescription()).isEqualTo(payload.description())
        );
    }
}