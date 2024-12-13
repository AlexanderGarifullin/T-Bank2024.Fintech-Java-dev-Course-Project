package com.cf.cfteam.controllers.security;


import com.cf.cfteam.services.security.AuthenticationService;
import com.cf.cfteam.transfer.payloads.security.AuthenticationPayload;
import com.cf.cfteam.transfer.payloads.security.ChangePasswordPayload;
import com.cf.cfteam.transfer.payloads.security.RegistrationPayload;
import com.cf.cfteam.transfer.responses.security.JwtAuthenticationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_success() {
        RegistrationPayload registrationPayload = getRegistrationPayload();
        JwtAuthenticationResponse expectedResponse = getJwtAuthenticationResponse();
        when(authenticationService.register(registrationPayload)).thenReturn(expectedResponse);

        JwtAuthenticationResponse response = userController.register(registrationPayload);

        assertThat(response).isEqualTo(expectedResponse);
        verify(authenticationService).register(registrationPayload);
    }

    @Test
    void testLogin_success() {
        AuthenticationPayload authenticationPayload = getAuthenticationPayload();
        JwtAuthenticationResponse expectedResponse = getJwtAuthenticationResponse();
        when(authenticationService.login(authenticationPayload)).thenReturn(expectedResponse);

        JwtAuthenticationResponse response = userController.login(authenticationPayload, authentication);

        assertThat(response).isEqualTo(expectedResponse);
        verify(authenticationService).login(authenticationPayload);
    }

    @Test
    void testLogout_success() {
        userController.logout(authentication);

        verify(authenticationService).logout(authentication);
    }

    @Test
    void testChangePassword_success() {
        ChangePasswordPayload changePasswordPayload = getChangePasswordPayload();

        userController.changePassword(changePasswordPayload, authentication);

        verify(authenticationService).changePassword(changePasswordPayload, authentication);
    }

    private RegistrationPayload getRegistrationPayload() {
        return new RegistrationPayload("Test User", "test_login", "test_password");
    }

    private JwtAuthenticationResponse getJwtAuthenticationResponse() {
        return new JwtAuthenticationResponse("mockJwtToken");
    }

    private AuthenticationPayload getAuthenticationPayload() {
        return new AuthenticationPayload("test_login", "test_password", true);
    }

    private ChangePasswordPayload getChangePasswordPayload() {
        return new ChangePasswordPayload("0000", "new_password");
    }
}