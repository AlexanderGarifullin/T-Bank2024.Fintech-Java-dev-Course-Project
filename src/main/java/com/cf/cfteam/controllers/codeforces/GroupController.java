package com.cf.cfteam.controllers.codeforces;

import com.cf.cfteam.services.codeforces.GroupService;
import com.cf.cfteam.transfer.payloads.codeforces.GroupPayload;
import com.cf.cfteam.transfer.responses.codeforces.GroupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/cf/groups")
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<GroupResponse>> getAllGroupsByUser(@PathVariable Long userId, Authentication authentication) {
        List<GroupResponse> groups = groupService.getAllGroupsByUser(userId);
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<GroupResponse> getGroupById(@PathVariable Long groupId, Authentication authentication) {
        GroupResponse group = groupService.getGroupById(groupId);
        return ResponseEntity.ok(group);
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<GroupResponse> addGroupToUser(@PathVariable Long userId, @RequestBody GroupPayload groupPayload,
                                                Authentication authentication) {
        GroupResponse createdGroup = groupService.addGroupToUser(userId, groupPayload);
        return ResponseEntity.ok(createdGroup);
    }

    @PutMapping("/{groupId}")
    public ResponseEntity<GroupResponse> updateGroup(@PathVariable Long groupId, @RequestBody GroupPayload groupPayload,
                                             Authentication authentication) {
        GroupResponse group = groupService.updateGroup(groupId, groupPayload);
        return ResponseEntity.ok(group);
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long groupId, Authentication authentication) {
        groupService.deleteGroup(groupId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteAllGroupsByUser(@PathVariable Long userId, Authentication authentication) {
        groupService.deleteAllGroupsByUser(userId);
        return ResponseEntity.noContent().build();
    }
}
