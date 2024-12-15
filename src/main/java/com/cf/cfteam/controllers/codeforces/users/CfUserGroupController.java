package com.cf.cfteam.controllers.codeforces.users;

import com.cf.cfteam.services.codeforces.users.CfUserGroupService;
import com.cf.cfteam.transfer.payloads.codeforces.users.CfUserGroupPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cf/user/group")
public class CfUserGroupController {

    private final CfUserGroupService cfUserGroupService;

    @PostMapping
    public ResponseEntity<Void> createCfUserGroup(@RequestBody CfUserGroupPayload cfUserGroupPayload,
                                                  Authentication authentication) {
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Void> getAllCfUserGroups() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Void> getCfUserGroupById(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCfUserGroup(@PathVariable Long id,
                                             @RequestBody CfUserGroupPayload cfUserGroupPayload,
                                             Authentication authentication) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllCfUserGroups(Authentication authentication) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCfUserGroupById(@PathVariable Long id,
                                                 Authentication authentication) {
        return ResponseEntity.ok().build();
    }
}
