package com.cf.cfteam.transfer.responses.security;

import lombok.Builder;

@Builder
public record JwtAuthenticationResponse (
        String token
) {
}
