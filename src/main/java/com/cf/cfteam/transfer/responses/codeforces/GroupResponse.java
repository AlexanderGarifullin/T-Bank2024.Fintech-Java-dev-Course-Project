package com.cf.cfteam.transfer.responses.codeforces;

import lombok.Builder;

import java.util.List;

@Builder
public record GroupResponse(
        Long id,
        String name,
        String description,
        List<TeamResponse> teams
)  {
}
