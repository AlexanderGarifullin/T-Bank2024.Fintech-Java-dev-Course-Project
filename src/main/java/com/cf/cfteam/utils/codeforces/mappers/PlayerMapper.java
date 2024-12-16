package com.cf.cfteam.utils.codeforces.mappers;

import com.cf.cfteam.models.entities.codeforces.Player;
import com.cf.cfteam.transfer.responses.codeforces.PlayerResponse;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
}
