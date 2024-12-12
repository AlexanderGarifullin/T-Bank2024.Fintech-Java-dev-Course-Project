package com.cf.cfteam.transfer.payloads.security;

import lombok.Builder;

@Builder
public record AuthenticationPayload(
        String login,
        String password,
        boolean rememberMe
) {
}