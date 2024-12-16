package com.cf.cfteam.repositories.jpa.codeforces;

import com.cf.cfteam.models.entities.codeforces.Group;
import com.cf.cfteam.models.entities.security.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByUser(User user);
}
