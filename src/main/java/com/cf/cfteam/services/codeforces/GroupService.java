package com.cf.cfteam.services.codeforces;

import com.cf.cfteam.exceptions.codeforces.GroupNotFoundException;
import com.cf.cfteam.exceptions.security.UserNotFoundException;
import com.cf.cfteam.utils.codeforces.GroupMapper;
import com.cf.cfteam.models.entities.codeforces.Group;
import com.cf.cfteam.models.entities.security.User;
import com.cf.cfteam.repositories.jpa.codeforces.GroupRepository;
import com.cf.cfteam.repositories.jpa.security.UserRepository;
import com.cf.cfteam.transfer.payloads.codeforces.GroupPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public List<Group> getAllGroupsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return groupRepository.findByUser(user);
    }

    public Group getGroupById(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));
    }

    public Group addGroupToUser(Long userId, GroupPayload groupPayload) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Group group = GroupMapper.fromPayloadToEntity(groupPayload, user);

        return groupRepository.save(group);
    }

    public Group updateGroup(Long groupId, GroupPayload groupPayload) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));

        Group updatedGroup = GroupMapper.updateEntityFromPayload(group, groupPayload);

        return groupRepository.save(updatedGroup);
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
