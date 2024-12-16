package com.cf.cfteam.transfer.responses.codeforces;

import lombok.Builder;

@Builder
public record PlayerResponse(
        Long id,
        String login,
        Double rating
) {
}
