package com.cf.cfteam.repositories.jpa.codeforces;

import com.cf.cfteam.models.entities.codeforces.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
