package com.cf.cfteam.exceptions.security;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException  {

    private final String login;

    public UserNotFoundException(String login) {
        super("login.not_found");
        this.login = login;
    }
}