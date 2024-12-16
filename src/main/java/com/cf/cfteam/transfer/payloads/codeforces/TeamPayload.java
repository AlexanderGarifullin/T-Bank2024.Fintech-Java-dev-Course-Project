package com.cf.cfteam.transfer.payloads.codeforces;

import lombok.Builder;

@Builder
public record TeamPayload(
        String name,
        String description
) {
}
