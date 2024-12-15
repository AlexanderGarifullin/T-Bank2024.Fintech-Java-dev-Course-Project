package com.cf.cfteam.controllers.codeforces.teams;

import com.cf.cfteam.services.codeforces.teams.CfTeamService;
import com.cf.cfteam.transfer.payloads.codeforces.teams.CfTeamPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cf/team")
public class CfTeamController {

    private final CfTeamService cfTeamService;

    @PostMapping
    public ResponseEntity<Void> createCfTeam(@RequestBody CfTeamPayload cfTeamPayload,
                                                Authentication authentication) {
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Void> getAllCfTeams() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Void> getCfTeamById(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCfTeam(@PathVariable Long id,
                                                @RequestBody CfTeamPayload cFTeamPayload,
                                                Authentication authentication) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllCfTeams(Authentication authentication) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCfTeamById(@PathVariable Long id,
                                                    Authentication authentication) {
        return ResponseEntity.ok().build();
    }
}
