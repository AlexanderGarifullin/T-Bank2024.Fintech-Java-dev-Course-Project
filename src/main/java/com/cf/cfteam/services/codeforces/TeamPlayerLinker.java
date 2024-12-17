package com.cf.cfteam.services.codeforces;

import com.cf.cfteam.models.entities.codeforces.Player;
import com.cf.cfteam.models.entities.codeforces.Team;
import com.cf.cfteam.repositories.jpa.codeforces.PlayerRepository;
import com.cf.cfteam.repositories.jpa.codeforces.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamPlayerLinker {

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    public void unlinkTeamAndPlayer(Team team, Player player) {
        team.getPlayers().remove(player);
        player.getTeams().remove(team);
        saveTeamAndPlayer(team, player);
    }

    public void linkTeamAndPlayer(Team team, Player player) {
        team.getPlayers().add(player);
        player.getTeams().add(team);
        saveTeamAndPlayer(team, player);
    }

    public void saveTeamAndPlayer(Team team, Player player) {
        teamRepository.save(team);
        playerRepository.save(player);
    }
}
