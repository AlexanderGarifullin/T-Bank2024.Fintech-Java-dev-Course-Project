package com.cf.cfteam.controllers.security;

import com.cf.cfteam.services.security.AuthenticationService;
import com.cf.cfteam.transfer.payloads.security.AuthenticationPayload;
import com.cf.cfteam.transfer.payloads.security.ChangePasswordPayload;
import com.cf.cfteam.transfer.payloads.security.RegistrationPayload;
import com.cf.cfteam.transfer.responses.security.JwtAuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public JwtAuthenticationResponse register(@RequestBody RegistrationPayload registrationRequest) {
        return authenticationService.register(registrationRequest);
    }

    @PostMapping("/login")
    public JwtAuthenticationResponse login(@RequestBody AuthenticationPayload authenticationPayload) {
        return authenticationService.login(authenticationPayload);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(Authentication authentication) {
        authenticationService.logout(authentication);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("change-password")
    public ResponseEntity<Void> changePassword(
            @RequestBody ChangePasswordPayload changePasswordRequest,
            Authentication authentication
    ) {
        authenticationService.changePassword(changePasswordRequest, authentication);
        return ResponseEntity.ok().build();
    }
}
