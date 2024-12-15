package com.cf.cfteam.repositories.jpa.codeforces.teams;

import com.cf.cfteam.models.entities.codeforces.teams.CfTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CfUsersTeamRepository extends JpaRepository<CfTeam, Long> {
}
