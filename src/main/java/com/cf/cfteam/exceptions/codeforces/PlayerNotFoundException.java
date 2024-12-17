package com.cf.cfteam.exceptions.codeforces;

import lombok.Getter;

@Getter
public class PlayerNotFoundException extends RuntimeException {

    private final Long id;

    public PlayerNotFoundException(Long id) {
        super("id.not_found");
        this.id = id;
    }
}