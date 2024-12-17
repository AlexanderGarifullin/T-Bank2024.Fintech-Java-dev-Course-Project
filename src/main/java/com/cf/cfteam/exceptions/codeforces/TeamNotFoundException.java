package com.cf.cfteam.exceptions.codeforces;

import lombok.Getter;

@Getter
public class TeamNotFoundException extends RuntimeException {

    private final Long id;

    public TeamNotFoundException(Long id) {
        super("id.not_found");
        this.id = id;
    }
}
