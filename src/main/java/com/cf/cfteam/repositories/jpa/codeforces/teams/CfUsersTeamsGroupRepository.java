package com.cf.cfteam.repositories.jpa.codeforces.teams;

import com.cf.cfteam.models.entities.codeforces.teams.CfTeamsGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CfUsersTeamsGroupRepository extends JpaRepository<CfTeamsGroup, Long> {
}
