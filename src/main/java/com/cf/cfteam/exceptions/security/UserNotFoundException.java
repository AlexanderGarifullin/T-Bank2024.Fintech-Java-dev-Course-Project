package com.cf.cfteam.exceptions.security;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException  {

    private final String login;
    private final Long id;

    public UserNotFoundException(String login) {
        super("login.not_found");
        this.login = login;
        this.id = null;
    }

    public UserNotFoundException(Long id) {
        super("id.not_found");
        this.id = id;
        this.login = null;
    }
}