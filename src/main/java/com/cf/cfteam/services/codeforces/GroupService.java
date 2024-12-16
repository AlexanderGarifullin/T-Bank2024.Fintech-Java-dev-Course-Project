package com.cf.cfteam.services.codeforces;

import com.cf.cfteam.exceptions.codeforces.GroupNotFoundException;
import com.cf.cfteam.exceptions.security.UserNotFoundException;
import com.cf.cfteam.transfer.responses.codeforces.GroupResponse;
import com.cf.cfteam.utils.codeforces.mappers.GroupMapper;
import com.cf.cfteam.models.entities.codeforces.Group;
import com.cf.cfteam.models.entities.security.User;
import com.cf.cfteam.repositories.jpa.codeforces.GroupRepository;
import com.cf.cfteam.repositories.jpa.security.UserRepository;
import com.cf.cfteam.transfer.payloads.codeforces.GroupPayload;
import com.cf.cfteam.utils.codeforces.mappers.TeamMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupMapper groupMapper;

    public List<GroupResponse> getAllGroupsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        List<Group> groups = groupRepository.findByUser(user);

        return groups.stream()
                .map(groupMapper::fromEntityToResponse)
                .toList();
    }

    public GroupResponse getGroupById(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));

        return groupMapper.fromEntityToResponse(group);
    }

    public GroupResponse addGroupToUser(Long userId, GroupPayload groupPayload) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Group group = groupMapper.fromPayloadToEntity(groupPayload, user);
        group = groupRepository.save(group);

        return groupMapper.fromEntityToResponse(group);
    }

    public GroupResponse updateGroup(Long groupId, GroupPayload groupPayload) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));

        Group updatedGroup = groupMapper.updateEntityFromPayload(group, groupPayload);
        updatedGroup = groupRepository.save(updatedGroup);

        return groupMapper.fromEntityToResponse(updatedGroup);
    }


    public void deleteGroup(Long groupId) {
        groupRepository.deleteById(groupId);
    }

    public void deleteAllGroupsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        List<Group> groups = groupRepository.findByUser(user);
        groupRepository.deleteAll(groups);
    }
}
