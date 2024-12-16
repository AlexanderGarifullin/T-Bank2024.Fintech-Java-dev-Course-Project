package com.cf.cfteam.mappers.codeforces;

import com.cf.cfteam.models.entities.codeforces.Group;
import com.cf.cfteam.models.entities.security.User;
import com.cf.cfteam.transfer.payloads.codeforces.GroupPayload;

public final class GroupMapper {

    public static Group fromPayloadToEntity(GroupPayload payload, User user) {
        return Group.builder()
                .name(payload.name())
                .description(payload.description())
                .user(user)
                .build();
    }

    public static Group updateEntityFromPayload(Group group, GroupPayload payload) {
        group.setName(payload.name());
        group.setDescription(payload.description());
        return group;
    }

    private GroupMapper() {
        throw new UnsupportedOperationException("Utility class");
    }
}
