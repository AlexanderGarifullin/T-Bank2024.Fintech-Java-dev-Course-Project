package com.cf.cfteam.utils.codeforces.mappers;

import com.cf.cfteam.models.entities.codeforces.Player;
import com.cf.cfteam.transfer.payloads.codeforces.PlayerPayload;
import com.cf.cfteam.transfer.responses.codeforces.PlayerResponse;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class PlayerMapperTest {

    @InjectMocks
    private PlayerMapper playerMapper;

    @Mock
    private LoadingCache<String, Double> ratingCache;

    @Test
    void shouldMapFromEntityToResponse() {
        Player player = Player.builder()
                .login("login")
                .id(1L)
                .build();

        when(ratingCache.get(player.getLogin())).thenReturn(0.);

        PlayerResponse response = playerMapper.fromEntityToResponse(player);

        assertAll(
                () -> assertThat(response).isNotNull(),
                () -> assertThat(response.id()).isEqualTo(player.getId()),
                () -> assertThat(response.rating()).isEqualTo(0.),
                () -> assertThat(response.login()).isEqualTo(player.getLogin())
        );
    }

    @Test
    void shouldMapFromPayloadToEntity() {
        PlayerPayload payload = PlayerPayload.builder()
                .login("player login")
                .build();

        Player player = playerMapper.fromPayloadToEntity(payload);

        assertAll(
                () -> assertThat(player).isNotNull(),
                () -> assertThat(player.getLogin()).isEqualTo(payload.login())
        );
    }
}