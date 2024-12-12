package com.cf.cfteam.repositories.jpa.codeforces;

import com.cf.cfteam.models.entities.codeforces.CfUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CfUserRepository extends JpaRepository<CfUser, Long> {
}
