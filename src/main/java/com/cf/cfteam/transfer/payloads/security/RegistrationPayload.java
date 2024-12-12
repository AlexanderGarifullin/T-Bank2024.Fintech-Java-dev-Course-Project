package com.cf.cfteam.transfer.payloads.security;

import lombok.Builder;

@Builder
public record RegistrationPayload(
        String name,
        String login,
        String password
) {
}