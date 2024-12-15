package com.cf.cfteam.repositories.jpa.codeforces;

import com.cf.cfteam.models.entities.codeforces.CfTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CfUsersTeamRepository extends JpaRepository<CfTeam, Long> {
}
