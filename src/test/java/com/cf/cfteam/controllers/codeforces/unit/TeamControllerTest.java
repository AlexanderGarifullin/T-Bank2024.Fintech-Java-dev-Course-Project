package com.cf.cfteam.controllers.codeforces.unit;

import com.cf.cfteam.controllers.codeforces.TeamController;
import com.cf.cfteam.services.codeforces.TeamService;
import com.cf.cfteam.transfer.payloads.codeforces.TeamPayload;
import com.cf.cfteam.transfer.responses.codeforces.TeamResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class TeamControllerTest {

    @InjectMocks
    private TeamController teamController;

    @Mock
    private TeamService teamService;

    private static TeamPayload teamPayload;
    private static TeamResponse teamResponse;

    @BeforeAll
    static void setUp() {
        teamPayload = TeamPayload.builder()
                .name("team name")
                .description("team description")
                .build();

        teamResponse = TeamResponse.builder()
                .id(1L)
                .name("team name")
                .description("team description")
                .players(List.of())
                .teamRating(0.)
                .build();
    }

    @Test
    void shouldReturnAllTeamsByGroup() {
        when(teamService.getAllTeamsByGroup(1L)).thenReturn(List.of(teamResponse));

        var teams = teamController.getAllTeamsByGroup(1L, null).getBody();

        verify(teamService, times(1)).getAllTeamsByGroup(1L);

        assertThat(teams)
                .isNotNull()
                .hasSize(1)
                .contains(teamResponse);
    }

    @Test
    void shouldReturnTeamById() {
        when(teamService.getTeamById(1L)).thenReturn(teamResponse);

        var result = teamController.getTeamById(1L, null).getBody();

        verify(teamService, times(1)).getTeamById(1L);
        assertThat(result).isEqualTo(teamResponse);
    }

    @Test
    void shouldAddTeamToGroup() {
        when(teamService.addTeamToGroup(1L, teamPayload)).thenReturn(teamResponse);

        var result = teamController.addTeamToGroup(1L, teamPayload, null).getBody();

        verify(teamService, times(1)).addTeamToGroup(1L, teamPayload);

        assertThat(result).isEqualTo(teamResponse);
    }

    @Test
    void shouldUpdateTeam() {
        when(teamService.updateTeam(1L, teamPayload)).thenReturn(teamResponse);

        var result = teamController.updateTeam(1L, teamPayload, null).getBody();

        verify(teamService, times(1)).updateTeam(1L, teamPayload);
        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.name()).isEqualTo(teamResponse.name()),
                () -> assertThat(result.description()).isEqualTo(teamResponse.description())
        );
    }

    @Test
    void shouldDeleteGroup() {
        teamController.deleteTeam(1L, null);

        verify(teamService, times(1)).deleteTeam(1L);
    }

    @Test
    void shouldDeleteAllGroupsByUser() {
        teamController.deleteAllTeamsByGroup(1L, null);

        verify(teamService, times(1)).deleteAllTeamsByGroup(1L);
    }
}