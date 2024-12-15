package com.cf.cfteam.repositories.jpa.codeforces;

import com.cf.cfteam.models.entities.codeforces.CfTeamsGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CfUsersTeamsGroupRepository extends JpaRepository<CfTeamsGroup, Long> {
}
