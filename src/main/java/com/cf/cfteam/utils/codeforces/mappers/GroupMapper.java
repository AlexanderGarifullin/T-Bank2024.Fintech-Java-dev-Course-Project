package com.cf.cfteam.utils.codeforces.mappers;

import com.cf.cfteam.models.entities.codeforces.Group;
import com.cf.cfteam.models.entities.security.User;
import com.cf.cfteam.transfer.payloads.codeforces.GroupPayload;
import com.cf.cfteam.transfer.responses.codeforces.GroupResponse;
import com.cf.cfteam.transfer.responses.codeforces.PlayerResponse;
import com.cf.cfteam.transfer.responses.codeforces.TeamResponse;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public final class GroupMapper {

    private final TeamMapper teamMapper;

    public Group fromPayloadToEntity(GroupPayload payload, User user) {
        return Group.builder()
                .name(payload.name())
                .description(payload.description())
                .user(user)
                .build();
    }

    public Group updateEntityFromPayload(Group group, GroupPayload payload) {
        group.setName(payload.name());
        group.setDescription(payload.description());
        return group;
    }

    public GroupResponse fromEntityToResponse(Group group) {
        List<TeamResponse> convertedTeams = new ArrayList<>();

        if (group.getTeams() != null) {
            convertedTeams = group.getTeams().stream()
                    .map(teamMapper::fromEntityToResponse)
                    .toList();
        }

        return GroupResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .description(group.getDescription())
                .teams(convertedTeams)
                .build();
    }
}
