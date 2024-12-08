package com.cf.cfteam.repositories.jpa.security;

import com.cf.cfteam.models.entities.security.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);
}
