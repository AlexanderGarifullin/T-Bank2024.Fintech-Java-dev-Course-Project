package com.cf.cfteam.repositories.jpa.codeforces;

import com.cf.cfteam.models.entities.codeforces.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
