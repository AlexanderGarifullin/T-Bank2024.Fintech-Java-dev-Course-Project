package com.cf.cfteam.repositories.jpa.codeforces;

import com.cf.cfteam.models.entities.codeforces.CfUsersTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CfUsersTeamRepository extends JpaRepository<CfUsersTeam, Long> {
}
