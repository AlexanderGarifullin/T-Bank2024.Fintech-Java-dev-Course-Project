package com.cf.cfteam.controllers.security;

import com.cf.cfteam.BaseIntegrationTest;
import com.cf.cfteam.models.entities.security.User;
import com.cf.cfteam.repositories.jpa.security.TokenRepository;
import com.cf.cfteam.repositories.jpa.security.UserRepository;
import com.cf.cfteam.transfer.payloads.security.AuthenticationPayload;
import com.cf.cfteam.transfer.payloads.security.ChangePasswordPayload;
import com.cf.cfteam.transfer.payloads.security.RegistrationPayload;
import com.cf.cfteam.transfer.responses.security.JwtAuthenticationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
public class UserControllerIntegrationTest extends BaseIntegrationTest {

    private static final String uri = "/auth";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Test
    public void register_success() throws Exception {
        RegistrationPayload payload = RegistrationPayload.builder()
                .login("register-login")
                .name("register-name")
                .password("register-password")
                .build();

        var jwtResponse = register(payload);

        var user = userRepository.findByLogin("register-login");
        var token = tokenRepository.findByToken(jwtResponse.token());

        assertAll(
                () -> assertThat(jwtResponse.token()).isNotEmpty(),

                () -> assertThat(token).isPresent(),
                () -> assertThat(token.get().isRevoked()).isFalse(),

                () -> assertThat(user).isPresent()
        );

        deleteUserFromDb(user.get());
    }

    @Test
    public void register_shouldThrowUserAlreadyRegisterException_WhenUserAlreadyRegistered() throws Exception {
        String login = "user";
        String password = "password";
        String name = "Test User";

        RegistrationPayload payload = RegistrationPayload.builder()
                .login(login)
                .password(password)
                .name(name)
                .build();

        String requestBody = objectMapper.writeValueAsString(payload);

        mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("login.already_register"))
                .andExpect(jsonPath("$.details.login").value(login))
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    public void login_success() throws Exception {
        RegistrationPayload payloadToRegister = RegistrationPayload.builder()
                .login("register-login")
                .name("register-name")
                .password("register-password")
                .build();

        var jwtResponse = register(payloadToRegister);

        var payloadToLogin = AuthenticationPayload.builder()
                .login("register-login")
                .password("register-password")
                .rememberMe(true)
                .build();

        var response = mockMvc.perform(post(uri + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payloadToLogin)))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        var jwtFromLogin =  objectMapper.readValue(response.getContentAsString(), JwtAuthenticationResponse.class);

        var oldToken = tokenRepository.findByToken(jwtResponse.token());
        var newToken = tokenRepository.findByToken(jwtFromLogin.token());

        assertAll(
                () -> assertThat(jwtFromLogin.token()).isNotNull(),

                () -> assertThat(jwtResponse.token()).isNotNull(),
                () -> assertThat(jwtFromLogin.token()).isNotEqualTo(jwtResponse.token()),

                () -> assertThat(oldToken).isPresent(),
                () -> assertThat(newToken).isPresent(),

                () -> assertThat(oldToken.get().isRevoked()).isTrue(),
                () -> assertThat(newToken.get().isRevoked()).isFalse(),

                () -> assertThat(oldToken.get()).isNotEqualTo(newToken.get())
        );

        deleteUserFromDb(userRepository.findByLogin("register-login").get());
    }

    @Test
    public void logout_success() throws Exception {
        RegistrationPayload payloadToRegister = RegistrationPayload.builder()
                .login("register-login")
                .name("register-name")
                .password("register-password")
                .build();

        var jwtResponse = register(payloadToRegister);

        mockMvc.perform(post(uri + "/logout")
                        .header("Authorization", "Bearer %s".formatted(jwtResponse.token())))
                .andExpectAll(
                        status().isOk())
                .andReturn()
                .getResponse();

        var oldToken = tokenRepository.findByToken(jwtResponse.token());

        assertAll(
                () -> assertThat(oldToken).isPresent(),
                () -> assertThat(oldToken.get().isRevoked()).isTrue()
        );

        deleteUserFromDb(userRepository.findByLogin("register-login").get());
    }

    @Test
    public void changePassword_success() throws Exception {
        RegistrationPayload payloadToRegister = RegistrationPayload.builder()
                .login("register-login")
                .name("register-name")
                .password("register-password")
                .build();

        var jwtResponse = register(payloadToRegister);

        var oldUser = userRepository.findByLogin("register-login");

        var changePasswordPayload = ChangePasswordPayload.builder()
                .newPassword("new-password")
                .twoFactorCode("0000")
                .build();

        mockMvc.perform(patch(uri + "/change-password")
                        .header("Authorization", "Bearer %s".formatted(jwtResponse.token()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePasswordPayload)))
                .andExpectAll(
                        status().isOk())
                .andReturn()
                .getResponse();

        var newUser = userRepository.findByLogin("register-login");

        assertAll(
                () -> assertThat(oldUser).isPresent(),
                () -> assertThat(newUser).isPresent(),

                () -> assertThat(oldUser.get()).isNotEqualTo(newUser.get()),
                () -> assertThat(oldUser.get().getHashedPassword()).isNotEqualTo(newUser.get().getHashedPassword())
        );

        var payloadToLogin = AuthenticationPayload.builder()
                .login("register-login")
                .password("new-password")
                .rememberMe(true)
                .build();

        var loginResponse = mockMvc.perform(post(uri + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payloadToLogin)))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        var jwtFromLogin =  objectMapper.readValue(loginResponse.getContentAsString(), JwtAuthenticationResponse.class);

        var loginUser = userRepository.findByLogin("register-login");
        var loginToken=  tokenRepository.findByToken(jwtFromLogin.token());
        var oldToken=  tokenRepository.findByToken(jwtResponse.token());

        assertAll(
                () -> assertThat(jwtFromLogin.token()).isNotEmpty(),

                () ->  assertThat(loginToken).isPresent(),
                () ->  assertThat(oldToken).isPresent(),

                () ->  assertThat(loginToken.get().isRevoked()).isFalse(),
                () ->  assertThat(oldToken.get().isRevoked()).isTrue(),

                () -> assertThat(loginToken.get()).isNotEqualTo(oldToken),

                () -> assertThat(loginUser).isPresent(),
                () -> assertThat(loginUser.get()).isEqualTo(newUser.get())
        );

        deleteUserFromDb(loginUser.get());
    }

    @Test
    public void changePassword_shouldReturnBadRequest_WhenTwoFactorCodeInvalid() throws Exception {
        RegistrationPayload payloadToRegister = RegistrationPayload.builder()
                .login("register-login")
                .name("register-name")
                .password("register-password")
                .build();

        var jwtResponse = register(payloadToRegister);
        var user = userRepository.findByLogin("register-login");


        ChangePasswordPayload payload = ChangePasswordPayload.builder()
                .newPassword("new-password")
                .twoFactorCode("invalid-code")
                .build();

        mockMvc.perform(patch(uri + "/change-password")
                        .header("Authorization", "Bearer %s".formatted(jwtResponse.token()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("two-factor.code_invalid"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400));


        deleteUserFromDb(user.get());
    }


    private JwtAuthenticationResponse register(RegistrationPayload payload) throws Exception {
        var response = mockMvc.perform(post(uri + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        return objectMapper.readValue(response.getContentAsString(), JwtAuthenticationResponse.class);
    }

    private void deleteUserFromDb(User user) {
        userRepository.delete(user);
    }
}
