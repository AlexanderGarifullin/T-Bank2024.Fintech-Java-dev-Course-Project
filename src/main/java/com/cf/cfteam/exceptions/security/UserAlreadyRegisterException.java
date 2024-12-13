package com.cf.cfteam.exceptions.security;

import lombok.Getter;

@Getter
public class UserAlreadyRegisterException extends RuntimeException {

    private final String login;

    public UserAlreadyRegisterException(String login) {
        super("login.already_register");
        this.login = login;
    }
}