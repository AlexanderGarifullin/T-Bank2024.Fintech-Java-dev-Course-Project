package com.cf.cfteam.services.codeforces;

import com.cf.cfteam.models.entities.codeforces.Player;
import com.cf.cfteam.repositories.jpa.codeforces.PlayerRepository;
import com.cf.cfteam.transfer.payloads.codeforces.GroupPayload;
import com.cf.cfteam.transfer.payloads.codeforces.TeamPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;

    public List<Player> getAllPlayersByTeam(Long teamId) {
        return null;
    }

    public Player getPlayerById(Long playerId) {
        return null;
    }

    public Player addPlayerToTeam(Long teamId, TeamPayload teamPayload) {
        return null;
    }


    public Player updatePlayer(Long playerId, Long teamId, GroupPayload groupPayload) {
        return null;
    }


    public void deletePlayerFromTeam(Long playerId, Long teamId) {
    }


    public void deleteAllPlayersFromTeam(Long teamId) {
    }
}
