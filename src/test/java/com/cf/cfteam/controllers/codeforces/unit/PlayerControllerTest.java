package com.cf.cfteam.controllers.codeforces.unit;

import com.cf.cfteam.controllers.codeforces.PlayerController;
import com.cf.cfteam.services.codeforces.PlayerService;
import com.cf.cfteam.transfer.payloads.codeforces.PlayerPayload;
import com.cf.cfteam.transfer.responses.codeforces.PlayerResponse;
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
class PlayerControllerTest {

    @InjectMocks
    private PlayerController playerController;

    @Mock
    private PlayerService playerService;

    private static PlayerPayload playerPayload;
    private static PlayerResponse playerResponse;

    @BeforeAll
    static void setUp() {
        playerPayload = PlayerPayload.builder()
                .login("login")
                .build();

        playerResponse = PlayerResponse.builder()
                .id(1L)
                .rating(1000.)
                .login("login")
                .build();
    }

    @Test
    void shouldReturnAllPlayersByTeam() {
        when(playerService.getAllPlayersByTeam(1L)).thenReturn(List.of(playerResponse));

        var players = playerController.getAllPlayersByTeam(1L, null).getBody();

        verify(playerService, times(1)).getAllPlayersByTeam(1L);

        assertThat(players)
                .isNotNull()
                .hasSize(1)
                .contains(playerResponse);
    }

    @Test
    void shouldReturnPlayerById() {
        when(playerService.getPlayerById(1L)).thenReturn(playerResponse);

        var result = playerController.getPlayerById(1L, null).getBody();

        verify(playerService, times(1)).getPlayerById(1L);
        assertThat(result).isEqualTo(playerResponse);
    }

    @Test
    void shouldAddPlayerToTeam() {
        when(playerService.addPlayerToTeam(1L, playerPayload)).thenReturn(playerResponse);

        var result = playerController.addPlayerToTeam(1L, playerPayload, null).getBody();

        verify(playerService, times(1)).addPlayerToTeam(1L, playerPayload);

        assertThat(result).isEqualTo(playerResponse);
    }

    @Test
    void shouldUpdatePlayerInTeam() {
        when(playerService.updatePlayerInTeam(1L, 1L, playerPayload)).thenReturn(playerResponse);

        var result = playerController.updatePlayerInTeam(1L, 1L, playerPayload, null).getBody();

        verify(playerService, times(1)).updatePlayerInTeam(1L, 1L, playerPayload);
        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.login()).isEqualTo(playerResponse.login())
        );
    }

    @Test
    void shouldDeletePlayerFromTeam() {
        playerController.deletePlayerFromTeam(1L, 1L, null);

        verify(playerService, times(1)).deletePlayerFromTeam(1L, 1L);
    }

    @Test
    void shouldDeleteAllPlayersFromTeam() {
        playerController.deleteAllPlayersFromTeam(1L, null);

        verify(playerService, times(1)).deleteAllPlayersFromTeam(1L);
    }
}