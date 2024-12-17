package com.cf.cfteam.services.codeforces;

import com.cf.cfteam.exceptions.codeforces.PlayerNotFoundException;
import com.cf.cfteam.exceptions.codeforces.PlayerNotFromTeamException;
import com.cf.cfteam.exceptions.codeforces.TeamNotFoundException;
import com.cf.cfteam.models.entities.codeforces.Player;
import com.cf.cfteam.models.entities.codeforces.Team;
import com.cf.cfteam.repositories.jpa.codeforces.PlayerRepository;
import com.cf.cfteam.repositories.jpa.codeforces.TeamRepository;
import com.cf.cfteam.transfer.payloads.codeforces.PlayerPayload;
import com.cf.cfteam.transfer.responses.codeforces.PlayerResponse;
import com.cf.cfteam.utils.codeforces.mappers.PlayerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final PlayerMapper playerMapper;

    public List<PlayerResponse> getAllPlayersByTeam(Long teamId) {
        var team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));

        return team.getPlayers().stream()
                .map(playerMapper::fromEntityToResponse)
                .toList();
    }

    public PlayerResponse getPlayerById(Long playerId) {
        var player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException(playerId));

        return playerMapper.fromEntityToResponse(player);
    }

    public PlayerResponse addPlayerToTeam(Long teamId, PlayerPayload payload) {
        var team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));

        var player = createPlayer(payload);

        linkTeamAndPlayer(team, player);

        return playerMapper.fromEntityToResponse(player);
    }


    public PlayerResponse updatePlayerInTeam(Long playerId, Long teamId, PlayerPayload payload) {
        var player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException(playerId));

        var team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));

        if (!team.getPlayers().contains(player)) {
            throw new PlayerNotFromTeamException(teamId, playerId);
        }

        unlinkTeamAndPlayer(team, player);;
        player = createPlayer(payload);
        linkTeamAndPlayer(team, player);

        return playerMapper.fromEntityToResponse(player);
    }

    private Player createPlayer(PlayerPayload payload) {
        var exist = playerRepository.findByLogin(payload.login());
        if (exist.isPresent()) return exist.get();

        Player player = playerMapper.fromPayloadToEntity(payload);
        return playerRepository.save(player);
    }


    public void deletePlayerFromTeam(Long playerId, Long teamId) {
        var player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException(playerId));

        var team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));

        unlinkTeamAndPlayer(team, player);
    }


    public void deleteAllPlayersFromTeam(Long teamId) {
        var team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));

        var players = team.getPlayers();

        players.forEach(player -> player.getTeams().remove(team));
        team.getPlayers().clear();

        teamRepository.save(team);
        players.forEach(playerRepository::save);
    }

    private void unlinkTeamAndPlayer(Team team, Player player) {
        team.getPlayers().remove(player);
        player.getTeams().remove(team);
        saveTeamAndPlayer(team, player);
    }

    private void linkTeamAndPlayer(Team team, Player player) {
        team.getPlayers().add(player);
        player.getTeams().add(team);
        saveTeamAndPlayer(team, player);
    }

    private void saveTeamAndPlayer(Team team, Player player) {
        teamRepository.save(team);
        playerRepository.save(player);
    }
}
