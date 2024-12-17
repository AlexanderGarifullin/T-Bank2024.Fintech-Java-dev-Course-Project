package com.cf.cfteam.transfer.responses.codeforces;

import lombok.Builder;

import java.util.List;

@Builder
public record TeamResponse(
        Long id,
        String name,
        String description,
        List<PlayerResponse> players,
        Double teamRating
) {
}
