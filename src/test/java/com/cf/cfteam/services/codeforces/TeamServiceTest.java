package com.cf.cfteam.services.codeforces;

import com.cf.cfteam.exceptions.codeforces.GroupNotFoundException;
import com.cf.cfteam.exceptions.codeforces.TeamNotFoundException;
import com.cf.cfteam.exceptions.security.UserNotFoundException;
import com.cf.cfteam.models.entities.codeforces.Group;
import com.cf.cfteam.models.entities.codeforces.Team;
import com.cf.cfteam.models.entities.security.Role;
import com.cf.cfteam.models.entities.security.User;
import com.cf.cfteam.repositories.jpa.codeforces.GroupRepository;
import com.cf.cfteam.repositories.jpa.codeforces.TeamRepository;
import com.cf.cfteam.transfer.payloads.codeforces.GroupPayload;
import com.cf.cfteam.transfer.payloads.codeforces.TeamPayload;
import com.cf.cfteam.transfer.responses.codeforces.GroupResponse;
import com.cf.cfteam.transfer.responses.codeforces.TeamResponse;
import com.cf.cfteam.utils.codeforces.mappers.TeamMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @InjectMocks
    private TeamService teamService;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private TeamMapper teamMapper;

    private static Group group;
    private static Team team;
    private static TeamPayload teamPayload;
    private static TeamResponse teamResponse;

    @BeforeAll
    static void setUp() {
        group = Group.builder()
                .name("Test Group")
                .description("Test description")
                .user(null)
                .build();

        team = Team.builder()
                .name("team")
                .description("team description")
                .group(group)
                .build();

        teamPayload = TeamPayload.builder()
                .name("team")
                .description("team description")
                .build();

        teamResponse = TeamResponse.builder()
                .name("team")
                .description("team description")
                .players(List.of())
                .teamRating(0.)
                .build();
    }

    @Test
    void getAllTeamsByGroup_ShouldReturnTeams_WhenGroupExists() {
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(teamRepository.findByGroup(group)).thenReturn(List.of(team));
        when(teamMapper.fromEntityToResponse(team)).thenReturn(teamResponse);

        var teams = teamService.getAllTeamsByGroup(1L);

        assertThat(teams).hasSize(1)
                .contains(teamResponse);
    }

    @Test
    void getAllTeamsByGroup_ShouldThrowGroupNotFoundException_WhenGroupDoesNotExist() {
        when(groupRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->  teamService.getAllTeamsByGroup(1L))
                .isInstanceOf(GroupNotFoundException.class)
                .hasMessageContaining("id.not_found");
    }

    @Test
    void getTeamById_ShouldReturnTeam_WhenTeamExists() {
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(teamMapper.fromEntityToResponse(team)).thenReturn(teamResponse);

        var result = teamService.getTeamById(1L);

        assertThat(result).isEqualTo(teamResponse);
    }

    @Test
    void getTeamById_ShouldThrowTeamNotFoundException_WhenTeanDoesNotExist() {
        when(teamRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->  teamService.getTeamById(1L))
                .isInstanceOf(TeamNotFoundException.class)
                .hasMessageContaining("id.not_found");
    }

    @Test
    void addTeamToGroup_ShouldAddTeam_WhenGroupExists() {
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(teamRepository.save(any(Team.class))).thenReturn(team);

        when(teamMapper.fromPayloadToEntity(teamPayload, group)).thenReturn(team);
        when(teamMapper.fromEntityToResponse(team)).thenReturn(teamResponse);

        var result = teamService.addTeamToGroup(1L, teamPayload);

        assertThat(result).isEqualTo(teamResponse);
        verify(teamRepository, times(1)).save(any(Team.class));
    }

    @Test
    void addTeamToGroup_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        when(groupRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> teamService.addTeamToGroup(1L, teamPayload))
                .isInstanceOf(GroupNotFoundException.class)
                .hasMessageContaining("id.not_found");
    }

    @Test
    void updateTeam_ShouldUpdateTeam_WhenTeamExists() {
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(teamRepository.save(any(Team.class))).thenReturn(team);

        when(teamMapper.updateEntityFromPayload(team, teamPayload)).thenReturn(team);
        when(teamMapper.fromEntityToResponse(team)).thenReturn(teamResponse);

        var result = teamService.updateTeam(1L, teamPayload);

        assertAll(
                () -> assertThat(result.description()).isEqualTo(teamPayload.description()),
                () -> assertThat(result.name()).isEqualTo(teamPayload.name())
        );
        verify(teamRepository, times(1)).save(any(Team.class));
    }

    @Test
    void updateTeam_ShouldThrowTeamNotFoundException_WhenTeamDoesNotExist() {
        when(teamRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->  teamService.updateTeam(1L, teamPayload))
                .isInstanceOf(TeamNotFoundException.class)
                .hasMessageContaining("id.not_found");
    }

    @Test
    void deleteTeam_ShouldDeleteTeam_WhenTeamExists() {
        teamService.deleteTeam(1L);

        verify(teamRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteAllTeamsByGroup_ShouldDeleteAllTeams_WhenGroupExists() {
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(teamRepository.findByGroup(group)).thenReturn(List.of(team));

        teamService.deleteAllTeamsByGroup(1L);

        verify(teamRepository, times(1)).deleteAll(List.of(team));
    }

    @Test
    void deleteAllTeamsByGroup_ShouldThrowGroupNotFoundException_WhenGroupDoesNotExist() {
        when(groupRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> teamService.deleteAllTeamsByGroup(1L))
                .isInstanceOf(GroupNotFoundException.class)
                .hasMessageContaining("id.not_found");
    }

}