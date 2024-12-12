package com.cf.cfteam.transfer.payloads.security;

import lombok.Builder;

@Builder
public record ChangePasswordPayload(
        String newPassword,
        String twoFactorCode
) {
}
