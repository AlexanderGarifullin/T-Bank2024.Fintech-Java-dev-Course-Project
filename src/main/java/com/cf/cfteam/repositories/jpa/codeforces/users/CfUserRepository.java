package com.cf.cfteam.repositories.jpa.codeforces.users;

import com.cf.cfteam.models.entities.codeforces.users.CfUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CfUserRepository extends JpaRepository<CfUser, Long> {
}
