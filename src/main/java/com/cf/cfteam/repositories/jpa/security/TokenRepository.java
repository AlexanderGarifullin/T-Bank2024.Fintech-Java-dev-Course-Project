package com.cf.cfteam.repositories.jpa.security;

import com.cf.cfteam.models.entities.security.Token;
import com.cf.cfteam.models.entities.security.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);

    List<Token> findAllByUserAndRevoked(User user, boolean revoked);
}