package com.cf.cfteam.controllers.codeforces;

import com.cf.cfteam.services.codeforces.PlayerService;
import com.cf.cfteam.transfer.payloads.codeforces.PlayerPayload;
import com.cf.cfteam.transfer.responses.codeforces.PlayerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/cf/players")
public class PlayerController {
    
    private final PlayerService playerService;

    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<PlayerResponse>> getAllPlayersByTeam(@PathVariable Long teamId,
                                                                    Authentication authentication) {
        List<PlayerResponse> players = playerService.getAllPlayersByTeam(teamId);
        return ResponseEntity.ok(players);
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<PlayerResponse> getPlayerById(@PathVariable Long playerId, Authentication authentication) {
        PlayerResponse player = playerService.getPlayerById(playerId);
        return ResponseEntity.ok(player);
    }

    @PostMapping("/team/{teamId}")
    public ResponseEntity<PlayerResponse> addPlayerToTeam(@PathVariable Long teamId,
                                                          @RequestBody PlayerPayload playerPayload,
                                                          Authentication authentication) {
        PlayerResponse player = playerService.addPlayerToTeam(teamId, playerPayload);
        return ResponseEntity.ok(player);
    }

    @PutMapping("/players/{playerId}/teams/{teamId}")
    public ResponseEntity<PlayerResponse> updatePlayerInTeam(@PathVariable Long playerId,
                                                     @PathVariable Long teamId,
                                                     @RequestBody PlayerPayload playerPayload,
                                           Authentication authentication) {
        PlayerResponse player = playerService.updatePlayerInTeam(playerId, teamId, playerPayload);
        return ResponseEntity.ok(player);
    }

    @DeleteMapping("/players/{playerId}/teams/{teamId}")
    public ResponseEntity<Void> deletePlayerFromTeam(@PathVariable Long playerId,
                                                     @PathVariable Long teamId,
                                                     Authentication authentication) {
        playerService.deletePlayerFromTeam(playerId, teamId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/team/{teamId}")
    public ResponseEntity<Void> deleteAllPlayersFromTeam(@PathVariable Long teamId, Authentication authentication) {
        playerService.deleteAllPlayersFromTeam(teamId);
        return ResponseEntity.noContent().build();
    }
}
