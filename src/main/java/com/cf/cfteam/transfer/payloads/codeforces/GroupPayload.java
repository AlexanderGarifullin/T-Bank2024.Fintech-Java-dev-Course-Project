package com.cf.cfteam.transfer.payloads.codeforces;

import lombok.Builder;

@Builder
public record GroupPayload(
        String name,
        String description
) {
}
