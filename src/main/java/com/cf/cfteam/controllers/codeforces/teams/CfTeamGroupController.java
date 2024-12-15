package com.cf.cfteam.controllers.codeforces.teams;

import com.cf.cfteam.services.codeforces.teams.CfTeamGroupService;
import com.cf.cfteam.transfer.payloads.codeforces.teams.CfTeamGroupPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cf/team/group")
public class CfTeamGroupController {

    private final CfTeamGroupService cfTeamGroupService;

    @PostMapping
    public ResponseEntity<Void> createCfTeamGroup(@RequestBody CfTeamGroupPayload cfTeamGroupPayload,
                                            Authentication authentication) {
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Void> getAllCfTeamGroups() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Void> getCfTeamGroupById(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCfTeamGroup(@PathVariable Long id,
                                                @RequestBody CfTeamGroupPayload cfTeamGroupPayload,
                                                Authentication authentication) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllCfTeamGroups(Authentication authentication) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCfTeamGroupById(@PathVariable Long id,
                                                    Authentication authentication) {
        return ResponseEntity.ok().build();
    }
}
