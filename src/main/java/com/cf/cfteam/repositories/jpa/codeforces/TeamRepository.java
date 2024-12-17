package com.cf.cfteam.repositories.jpa.codeforces;

import com.cf.cfteam.models.entities.codeforces.Group;
import com.cf.cfteam.models.entities.codeforces.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findByGroup(Group group);
}
