package com.cf.cfteam.utils.codeforces.mappers;

import com.cf.cfteam.models.entities.codeforces.Group;
import com.cf.cfteam.models.entities.codeforces.Team;
import com.cf.cfteam.models.entities.security.User;
import com.cf.cfteam.transfer.payloads.codeforces.GroupPayload;
import com.cf.cfteam.transfer.payloads.codeforces.TeamPayload;
import com.cf.cfteam.transfer.responses.codeforces.PlayerResponse;
import com.cf.cfteam.transfer.responses.codeforces.TeamResponse;
import com.cf.cfteam.utils.codeforces.RatingCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TeamMapper {

    private final PlayerMapper playerMapper;

    public TeamResponse fromEntityToResponse(Team team) {
        List<PlayerResponse> convertedPlayers = new ArrayList<>();
        Double teamRating = 0.;

        if (team.getPlayers() != null) {
            convertedPlayers = team.getPlayers().stream()
                    .map(playerMapper::fromEntityToResponse)
                    .toList();

            teamRating = RatingCalculator.aggregateRatings(convertedPlayers);
        }

        return TeamResponse.builder()
                .id(team.getId())
                .name(team.getName())
                .description(team.getDescription())
                .players(convertedPlayers)
                .teamRating(teamRating)
                .build();
    }

    public Team fromPayloadToEntity(TeamPayload payload, Group group) {
        return Team.builder()
                .group(group)
                .name(payload.name())
                .description(payload.description())
                .build();
    }

    public Team updateEntityFromPayload(Team team, TeamPayload payload) {
        team.setName(payload.name());
        team.setDescription(payload.description());
        return team;
    }
}
