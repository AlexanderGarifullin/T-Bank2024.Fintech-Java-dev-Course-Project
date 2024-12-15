package com.cf.cfteam.repositories.jpa.codeforces.users;

import com.cf.cfteam.models.entities.codeforces.users.CfUsersGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CfUsersGroupRepository extends JpaRepository<CfUsersGroup, Long> {
}
