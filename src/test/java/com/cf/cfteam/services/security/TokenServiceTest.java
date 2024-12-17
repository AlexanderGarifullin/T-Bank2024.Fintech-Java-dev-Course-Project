package com.cf.cfteam.services.security;


import com.cf.cfteam.exceptions.security.TokenNotFoundException;
import com.cf.cfteam.models.entities.security.Token;
import com.cf.cfteam.repositories.jpa.security.TokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    @Mock
    private TokenRepository tokenRepository;

    @Test
    void isTokenRevoked_shouldReturnTrue_whenTokenIsRevoked() {
        String tokenValue = "valid-token";
        Token token = new Token();
        token.setRevoked(true);
        when(tokenRepository.findByToken(tokenValue)).thenReturn(Optional.of(token));

        boolean result = tokenService.isTokenRevoked(tokenValue);

        assertThat(result).isTrue();
        verify(tokenRepository, times(1)).findByToken(tokenValue);
    }

    @Test
    void isTokenRevoked_shouldReturnFalse_whenTokenIsNotRevoked() {
        String tokenValue = "valid-token";
        Token token = new Token();
        token.setRevoked(false);
        when(tokenRepository.findByToken(tokenValue)).thenReturn(Optional.of(token));

        boolean result = tokenService.isTokenRevoked(tokenValue);

        assertThat(result).isFalse();
        verify(tokenRepository, times(1)).findByToken(tokenValue);
    }

    @Test
    void isTokenRevoked_shouldThrowTokenNotFoundException_whenTokenDoesNotExist() {
        String tokenValue = "invalid-token";
        when(tokenRepository.findByToken(tokenValue)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tokenService.isTokenRevoked(tokenValue))
                .isInstanceOf(TokenNotFoundException.class)
                .hasMessageContaining("token.not_found");

        verify(tokenRepository, times(1)).findByToken(tokenValue);
    }
}