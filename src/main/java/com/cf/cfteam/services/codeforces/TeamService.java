package com.cf.cfteam.services.codeforces;

import com.cf.cfteam.exceptions.codeforces.GroupNotFoundException;
import com.cf.cfteam.exceptions.codeforces.TeamNotFoundException;
import com.cf.cfteam.models.entities.codeforces.Group;
import com.cf.cfteam.models.entities.codeforces.Team;
import com.cf.cfteam.repositories.jpa.codeforces.GroupRepository;
import com.cf.cfteam.repositories.jpa.codeforces.TeamRepository;
import com.cf.cfteam.transfer.payloads.codeforces.TeamPayload;
import com.cf.cfteam.transfer.responses.codeforces.TeamResponse;
import com.cf.cfteam.utils.codeforces.mappers.TeamMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final GroupRepository groupRepository;
    private final TeamMapper teamMapper;

    public List<TeamResponse> getAllTeamsByGroup(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));

        List<Team> teams = teamRepository.findByGroup(group);

        return teams.stream()
                .map(teamMapper::fromEntityToResponse)
                .toList();
    }

    public TeamResponse getTeamById(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));

        return teamMapper.fromEntityToResponse(team);
    }

    public TeamResponse addTeamToGroup(Long groupId, TeamPayload teamPayload) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));

        Team team = teamMapper.fromPayloadToEntity(teamPayload, group);
        team = teamRepository.save(team);

        return teamMapper.fromEntityToResponse(team);
    }

    public TeamResponse updateTeam(Long teamId, TeamPayload teamPayload) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));

        Team updatedTeam = teamMapper.updateEntityFromPayload(team, teamPayload);
        updatedTeam = teamRepository.save(updatedTeam);

        return teamMapper.fromEntityToResponse(updatedTeam);
    }

    public void deleteTeam(Long teamId) {
        teamRepository.deleteById(teamId);
    }

    public void deleteAllTeamsByGroup(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));
        List<Team> teams = teamRepository.findByGroup(group);
        teamRepository.deleteAll(teams);
    }
}
