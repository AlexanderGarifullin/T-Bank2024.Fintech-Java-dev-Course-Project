package com.cf.cfteam.services.security;

import com.cf.cfteam.models.entities.security.User;
import com.cf.cfteam.repositories.jpa.security.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByLogin(login);
        if (user.isPresent())  return new com.cf.cfteam.models.entities.security.UserDetails(user.get());
        else throw new UsernameNotFoundException(login);
    }
}