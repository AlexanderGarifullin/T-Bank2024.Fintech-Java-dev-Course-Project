package com.cf.cfteam.services.codeforces;


import com.cf.cfteam.exceptions.codeforces.*;
import com.cf.cfteam.models.entities.codeforces.Player;
import com.cf.cfteam.models.entities.codeforces.Team;
import com.cf.cfteam.repositories.jpa.codeforces.PlayerRepository;
import com.cf.cfteam.repositories.jpa.codeforces.TeamRepository;
import com.cf.cfteam.transfer.payloads.codeforces.PlayerPayload;
import com.cf.cfteam.transfer.responses.codeforces.PlayerResponse;
import com.cf.cfteam.utils.codeforces.mappers.PlayerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @InjectMocks
    private PlayerService playerService;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private PlayerMapper playerMapper;

    @Mock
    private TeamPlayerLinker teamPlayerLinker;

    private Team team;
    private Player player;
    private PlayerPayload playerPayload;
    private PlayerResponse playerResponse;

    @BeforeEach
    void setUp() {
        team = Team.builder()
                .id(1L)
                .name("team")
                .description("team description")
                .build();

        player = Player.builder()
                .id(1L)
                .login("login")
                .teams(List.of())
                .build();


        playerPayload = PlayerPayload.builder()
                .login("login")
                .build();

        playerResponse = PlayerResponse.builder()
                .login("login")
                .rating(1000.)
                .id(1L)
                .build();
    }


    @Test
    void getAllPlayersByTeam_ShouldReturnPlayers_WhenTeamExists() {
        team.setPlayers(List.of(player));
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

        when(playerMapper.fromEntityToResponse(player)).thenReturn(playerResponse);

        var players = playerService.getAllPlayersByTeam(1L);

        assertThat(players).hasSize(1)
                .contains(playerResponse);
    }

    @Test
    void getAllPlayersByTeam_ShouldThrowTeamNotFoundException_WhenTeamDoesNotExist() {
        when(teamRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->  playerService.getAllPlayersByTeam(1L))
                .isInstanceOf(TeamNotFoundException.class)
                .hasMessageContaining("id.not_found");
    }

    @Test
    void getPlayerById_ShouldReturnPlayer_WhenPlayerExists() {
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(playerMapper.fromEntityToResponse(player)).thenReturn(playerResponse);

        var result = playerService.getPlayerById(1L);

        assertThat(result).isEqualTo(playerResponse);
    }

    @Test
    void getPlayerById_ShouldThrowPlayerNotFoundException_WhenPlayerDoesNotExist() {
        when(playerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->  playerService.getPlayerById(1L))
                .isInstanceOf(PlayerNotFoundException.class)
                .hasMessageContaining("id.not_found");
    }

    @Test
    void addPlayerToTeam_ShouldAddPlayer_WhenTeamExists() {
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

        when(playerRepository.findByLogin("login")).thenReturn(Optional.of(player));

        doNothing().when(teamPlayerLinker).linkTeamAndPlayer(team, player);

        doReturn(playerResponse).when(playerMapper).fromEntityToResponse(player);

        var result = playerService.addPlayerToTeam(1L, playerPayload);

        assertThat(result).isEqualTo(playerResponse);
        verify(teamPlayerLinker, times(1)).linkTeamAndPlayer(team, player);
    }

    @Test
    void addPlayerToTeam_ShouldThrowTeamNotFoundException_WhenTeamDoesNotExist() {
        when(teamRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> playerService.addPlayerToTeam(1L, playerPayload))
                .isInstanceOf(TeamNotFoundException.class)
                .hasMessageContaining("id.not_found");
    }

    @Test
    void addPlayerToTeam_ShouldThrowPlayerAlreadyInTeamException_WhenPlayerAlreadyInTeamException() {
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(playerRepository.findByLogin("login")).thenReturn(Optional.of(player));

        team.setPlayers(List.of(player));

        assertThatThrownBy(() -> playerService.addPlayerToTeam(1L, playerPayload))
                .isInstanceOf(PlayerAlreadyInTeamException.class)
                .hasMessageContaining("player.already_in_team");
    }

    @Test
    void updatePlayerInTeam_ShouldUpdatePlayer_WhenTeamAndPlayerExistsAndFromOneTeam() {
        team.setPlayers(List.of(player));

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

        doNothing().when(teamPlayerLinker).unlinkTeamAndPlayer(team, player);
        when(playerRepository.findByLogin("login")).thenReturn(Optional.of(player));
        doNothing().when(teamPlayerLinker).linkTeamAndPlayer(team, player);

        when(playerRepository.findByLogin("login")).thenReturn(Optional.of(player));

        doReturn(playerResponse).when(playerMapper).fromEntityToResponse(player);

        var result = playerService.updatePlayerInTeam(1L, 1L, playerPayload);

        assertThat(result).isEqualTo(playerResponse);
        verify(teamPlayerLinker, times(1)).unlinkTeamAndPlayer(team, player);
        verify(teamPlayerLinker, times(1)).linkTeamAndPlayer(team, player);
    }

    @Test
    void updatePlayerInTeam_ShouldThrowPlayerNotFoundException_WhenPlayerDoesNotExist() {
        when(playerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> playerService.updatePlayerInTeam(1L, 1L, playerPayload))
                .isInstanceOf(PlayerNotFoundException.class)
                .hasMessageContaining("id.not_found");
    }

    @Test
    void updatePlayerInTeam_ShouldThrowTeamNotFoundException_WhenTeamDoesNotExist() {
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(teamRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> playerService.updatePlayerInTeam(1L, 1L, playerPayload))
                .isInstanceOf(TeamNotFoundException.class)
                .hasMessageContaining("id.not_found");
    }

    @Test
    void updatePlayerInTeam_ShouldThrowPlayerNotFromTeamException_WhenPlayerNotFromTeam() {
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

        assertThatThrownBy(() -> playerService.updatePlayerInTeam(1L, 1L, playerPayload))
                .isInstanceOf(PlayerNotFromTeamException.class)
                .hasMessageContaining("player.not_from_team");
    }

    @Test
    void deletePlayerFromTeam_ShouldDeletePlayerFromTeam_WhenTeamNadPlayerExists() {
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

        doNothing().when(teamPlayerLinker).unlinkTeamAndPlayer(team, player);
        playerService.deletePlayerFromTeam(1L, 1L);
        verify(teamPlayerLinker, times(1)).unlinkTeamAndPlayer(team, player);
    }

    @Test
    void deletePlayerFromTeam_ShouldThrowPlayerNotFoundException_WhenPlayerNotFound() {
        when(playerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> playerService.deletePlayerFromTeam(1L, 1L))
                .isInstanceOf(PlayerNotFoundException.class)
                .hasMessageContaining("id.not_found");
    }

    @Test
    void deletePlayerFromTeam_ShouldThrowTeamNotFoundException_WhenTeamNotFound() {
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(teamRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> playerService.deletePlayerFromTeam(1L, 1L))
                .isInstanceOf(TeamNotFoundException.class)
                .hasMessageContaining("id.not_found");
    }

    @Test
    void deleteAllPlayersFromTeam_ShouldDeleteAllPlayersFromTeam_WhenTeamPlayerExists() {
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        team.setPlayers(new ArrayList<>(List.of(player)));
        player.setTeams(new ArrayList<>(List.of(team)));

        doReturn(team).when(teamRepository).save(team);
        doReturn(player).when(playerRepository).save(player);

        playerService.deleteAllPlayersFromTeam(1L);

        verify(teamRepository, times(1)).save(team);
        verify(playerRepository, times(1)).save(player);
    }

    @Test
    void deleteAllPlayersFromTeam_ShouldThrowTeamNotFoundException_WhenTeamNotFound() {
        when(teamRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> playerService.deleteAllPlayersFromTeam(1L))
                .isInstanceOf(TeamNotFoundException.class)
                .hasMessageContaining("id.not_found");
    }
}