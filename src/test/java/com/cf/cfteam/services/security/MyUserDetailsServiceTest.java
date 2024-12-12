package com.cf.cfteam.services.security;

import com.cf.cfteam.models.entities.security.User;
import com.cf.cfteam.repositories.jpa.security.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
class MyUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MyUserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_shouldLoadUserByUsername_WhenUserExists() {
        String login = "testLogin";
        User user = new User();
        user.setLogin(login);
        com.cf.cfteam.models.entities.security.UserDetails userDetails = new com.cf.cfteam.models.entities.security.UserDetails(user);

        when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));

        UserDetails result = userDetailsService.loadUserByUsername(login);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(login);
    }

    @Test
    void loadUserByUsername_shouldThrowUsernameNotFoundException_WhenUserDoesNotExist() {
        String login = "nonExistentLogin";

        when(userRepository.findByLogin(login)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(login))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage(login);
    }
}