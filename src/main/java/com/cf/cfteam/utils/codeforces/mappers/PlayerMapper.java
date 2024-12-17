package com.cf.cfteam.utils.codeforces.mappers;

import com.cf.cfteam.models.entities.codeforces.Group;
import com.cf.cfteam.models.entities.codeforces.Player;
import com.cf.cfteam.models.entities.codeforces.Team;
import com.cf.cfteam.transfer.payloads.codeforces.PlayerPayload;
import com.cf.cfteam.transfer.payloads.codeforces.TeamPayload;
import com.cf.cfteam.transfer.responses.codeforces.PlayerResponse;
import com.cf.cfteam.transfer.responses.codeforces.TeamResponse;
import com.cf.cfteam.utils.codeforces.RatingCalculator;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PlayerMapper {

    private final LoadingCache<String, Double> ratingCache;

    public PlayerResponse fromEntityToResponse(Player player) {
        return PlayerResponse.builder()
                .id(player.getId())
                .login(player.getLogin())
                .rating(ratingCache.get(player.getLogin()))
                .build();
    }

    public Player fromPayloadToEntity(PlayerPayload payload) {
        return Player.builder()
                .login(payload.login())
                .build();
    }
}
