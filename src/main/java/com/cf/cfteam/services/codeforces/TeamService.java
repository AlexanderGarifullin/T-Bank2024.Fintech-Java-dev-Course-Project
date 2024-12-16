package com.cf.cfteam.services.codeforces;

import com.cf.cfteam.models.entities.codeforces.Team;
import com.cf.cfteam.repositories.jpa.codeforces.TeamRepository;
import com.cf.cfteam.transfer.payloads.codeforces.GroupPayload;
import com.cf.cfteam.transfer.payloads.codeforces.TeamPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    public List<Team> getAllTeamsByGroup(Long groupId) {
        return null;
    }

    public Team getTeamById(Long teamId) {
        return teamRepository.findById(teamId).get();
    }

    public Team addTeamToGroup(Long groupId, TeamPayload teamPayload) {
        return null;
    }

    public Team updateTeam(Long teamId, GroupPayload groupPayload) {
        return null;
    }

    public void deleteTeam(Long teamId) {
    }

    public void deleteAllTeamsByGroup(Long groupId) {
    }
}
