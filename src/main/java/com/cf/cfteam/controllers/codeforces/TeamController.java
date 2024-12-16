package com.cf.cfteam.controllers.codeforces;

import com.cf.cfteam.services.codeforces.TeamService;
import com.cf.cfteam.transfer.payloads.codeforces.GroupPayload;
import com.cf.cfteam.transfer.payloads.codeforces.TeamPayload;
import com.cf.cfteam.transfer.responses.codeforces.TeamResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/cf/teams")
public class TeamController {

    private final TeamService teamService;

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<TeamResponse>> getAllTeamsByGroup(@PathVariable Long groupId, Authentication authentication) {
        List<TeamResponse> teams = teamService.getAllTeamsByGroup(groupId);
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamResponse> getTeamById(@PathVariable Long teamId, Authentication authentication) {
        TeamResponse team = teamService.getTeamById(teamId);
        return ResponseEntity.ok(team);
    }

    @PostMapping("/group/{groupId}")
    public ResponseEntity<TeamResponse> addTeamToGroup(@PathVariable Long groupId, @RequestBody TeamPayload teamPayload,
                                                Authentication authentication) {
        TeamResponse createdTeam = teamService.addTeamToGroup(groupId, teamPayload);
        return ResponseEntity.ok(createdTeam);
    }

    @PutMapping("/{teamId}")
    public ResponseEntity<TeamResponse> updateTeam(@PathVariable Long teamId, @RequestBody TeamPayload teamPayload,
                                             Authentication authentication) {
        TeamResponse team = teamService.updateTeam(teamId, teamPayload);
        return ResponseEntity.ok(team);
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long teamId, Authentication authentication) {
        teamService.deleteTeam(teamId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/group/{groupId}")
    public ResponseEntity<Void> deleteAllTeamsByUser(@PathVariable Long groupId, Authentication authentication) {
        teamService.deleteAllTeamsByGroup(groupId);
        return ResponseEntity.noContent().build();
    }
}
