package com.cf.cfteam.services.security;

import com.cf.cfteam.exceptions.security.InvalidTwoFactorCodeException;
import com.cf.cfteam.exceptions.security.UserAlreadyRegisterException;
import com.cf.cfteam.exceptions.security.UserNotFoundException;
import com.cf.cfteam.models.entities.security.Token;
import com.cf.cfteam.models.entities.security.User;
import com.cf.cfteam.models.entities.security.UserDetails;
import com.cf.cfteam.repositories.jpa.security.TokenRepository;
import com.cf.cfteam.repositories.jpa.security.UserRepository;
import com.cf.cfteam.transfer.payloads.security.AuthenticationPayload;
import com.cf.cfteam.transfer.payloads.security.ChangePasswordPayload;
import com.cf.cfteam.transfer.payloads.security.RegistrationPayload;
import com.cf.cfteam.transfer.responses.security.JwtAuthenticationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_shouldThrowException_WhenUserAlreadyRegistered() {
        String login = "testLogin";
        RegistrationPayload payload = getRegistrationPayload(login);

        User existingUser = new User();
        existingUser.setLogin(login);
        when(userRepository.findByLogin(login)).thenReturn(Optional.of(existingUser));

        assertThatThrownBy(() -> authenticationService.register(payload))
                .isInstanceOf(UserAlreadyRegisterException.class)
                .hasMessageContaining("login.already_register");
    }

    @Test
    void register_shouldReturnJwtToken_WhenRegistrationIsSuccessful() {
        String login = "testLogin";
        RegistrationPayload payload = getRegistrationPayload(login);

        User user = new User();
        user.setLogin(login);
        when(userRepository.findByLogin(login)).thenReturn(java.util.Optional.empty());
        when(jwtService.generateToken(any(UserDetails.class), eq(false))).thenReturn("jwtToken");
        when(userRepository.save(any(User.class))).thenReturn(user);

        JwtAuthenticationResponse response = authenticationService.register(payload);

        assertThat(response.token()).isEqualTo("jwtToken");
        verify(userRepository, times(1)).save(any(User.class));
        verify(tokenRepository, times(1)).save(any(Token.class));
    }

    @Test
    void login_shouldThrowException_WhenUserNotFound() {
        AuthenticationPayload payload = getAuthenticationPayload();
        when(userRepository.findByLogin(payload.login())).thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() -> authenticationService.login(payload))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("login.not_found");
    }

    @Test
    void login_shouldReturnJwtToken_WhenLoginIsSuccessful() {
        String login = "testLogin";
        AuthenticationPayload payload = new AuthenticationPayload(login, "password", false);

        User user = new User();
        user.setLogin(login);
        when(userRepository.findByLogin(login)).thenReturn(java.util.Optional.of(user));
        when(jwtService.generateToken(any(UserDetails.class), eq(false))).thenReturn("jwtToken");
        when(tokenRepository.save(any(Token.class))).thenReturn(new Token());

        JwtAuthenticationResponse response = authenticationService.login(payload);

        assertThat(response.token()).isEqualTo("jwtToken");
        verify(tokenRepository).save(any(Token.class));
    }

    @Test
    void changePassword_shouldThrowException_WhenTwoFactorCodeIsInvalid() {
        ChangePasswordPayload payload = getChangePasswordPayload("newPassword", "1234");

        assertThatThrownBy(() -> authenticationService.changePassword(payload, authentication))
                .isInstanceOf(InvalidTwoFactorCodeException.class)
                .hasMessageContaining("two-factor.code_invalid");
    }

    @Test
    void changePassword_shouldUpdatePassword_WhenTwoFactorCodeIsValid() {
        ChangePasswordPayload payload = new ChangePasswordPayload("newPassword", "0000" );
        Authentication authentication = mock(Authentication.class);

        User user = new User();
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUser()).thenReturn(user);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword");

        authenticationService.changePassword(payload, authentication);

        assertThat(user.getHashedPassword()).isEqualTo("encodedPassword");
        verify(userRepository).save(user);
    }

    @Test
    void logout_shouldRevokeTokens_WhenUserLogsOut() {
        User user = new User();
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUser()).thenReturn(user);

        authenticationService.logout(authentication);

        verify(tokenRepository).saveAll(any());
    }

    private RegistrationPayload getRegistrationPayload(String login) {
        return new RegistrationPayload("Test User", login, "password");
    }

    private AuthenticationPayload getAuthenticationPayload() {
        return new AuthenticationPayload("testLogin", "password", false);
    }

    private ChangePasswordPayload getChangePasswordPayload(String newPassword, String twoFactorCode) {
        return new ChangePasswordPayload(newPassword, twoFactorCode);
    }
}