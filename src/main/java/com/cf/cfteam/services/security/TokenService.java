package com.cf.cfteam.services.security;

import com.cf.cfteam.exceptions.security.TokenNotFoundException;
import com.cf.cfteam.repositories.jpa.security.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService  {

    private final TokenRepository tokenRepository;

    public boolean isTokenRevoked(String token) {
        return tokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenNotFoundException(token))
                .isRevoked();
    }
}