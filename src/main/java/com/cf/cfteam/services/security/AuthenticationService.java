package com.cf.cfteam.services.security;

import com.cf.cfteam.exceptions.security.InvalidTwoFactorCodeException;
import com.cf.cfteam.exceptions.security.UserAlreadyRegisterException;
import com.cf.cfteam.exceptions.security.UserNotFoundException;
import com.cf.cfteam.models.entities.security.Role;
import com.cf.cfteam.models.entities.security.Token;
import com.cf.cfteam.models.entities.security.User;
import com.cf.cfteam.models.entities.security.UserDetails;
import com.cf.cfteam.repositories.jpa.security.TokenRepository;
import com.cf.cfteam.repositories.jpa.security.UserRepository;
import com.cf.cfteam.transfer.payloads.security.AuthenticationPayload;
import com.cf.cfteam.transfer.payloads.security.ChangePasswordPayload;
import com.cf.cfteam.transfer.payloads.security.RegistrationPayload;
import com.cf.cfteam.transfer.responses.security.JwtAuthenticationResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationResponse register(@NotNull RegistrationPayload registrationRequest) {
        userRepository.findByLogin(registrationRequest.login())
                .ifPresent(user -> {
                    throw new UserAlreadyRegisterException(user.getLogin());
                });

        User user = User.builder()
                .name(registrationRequest.name())
                .login(registrationRequest.login())
                .role(Role.USER)
                .hashedPassword(passwordEncoder.encode(registrationRequest.password()))
                .build();

        String jwtToken = jwtService.generateToken(new UserDetails(user), false);

        Token token = Token.builder()
                .token(jwtToken)
                .user(user)
                .build();

        userRepository.save(user);
        tokenRepository.save(token);

        return new JwtAuthenticationResponse(jwtToken);
    }

    public JwtAuthenticationResponse login(@NotNull AuthenticationPayload authenticationPayload) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationPayload.login(),
                authenticationPayload.password()
        ));

        var user = userRepository.findByLogin(authenticationPayload.login())
                .orElseThrow(() -> new UserNotFoundException(authenticationPayload.login()));

        var tokens = tokenRepository.findAllByUserAndRevoked(user, false);
        tokens.forEach(token -> token.setRevoked(true));
        tokenRepository.saveAll(tokens);

        String jwtToken = jwtService.generateToken(new UserDetails(user), authenticationPayload.rememberMe());

        Token token = Token.builder()
                .token(jwtToken)
                .user(user)
                .build();
        tokenRepository.save(token);

        return new JwtAuthenticationResponse(jwtToken);
    }

    public void logout(@NotNull Authentication authentication) {
        var userDetails = (UserDetails) authentication.getPrincipal();
        var user = userDetails.getUser();

        var tokens = tokenRepository.findAllByUserAndRevoked(user, false);
        tokens.forEach(token -> token.setRevoked(true));
        tokenRepository.saveAll(tokens);
    }

    public void changePassword(@NotNull ChangePasswordPayload changePasswordPayload,
                               @NotNull Authentication authentication) {
        if (!changePasswordPayload.twoFactorCode().equals("0000")) {
            throw new InvalidTwoFactorCodeException();
        }

        var userDetails = (UserDetails) authentication.getPrincipal();
        var user = userDetails.getUser();

        user.setHashedPassword(passwordEncoder.encode(changePasswordPayload.newPassword()));
        userRepository.save(user);
    }
}
