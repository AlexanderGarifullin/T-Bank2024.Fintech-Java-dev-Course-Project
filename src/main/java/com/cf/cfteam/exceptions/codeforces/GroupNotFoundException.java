package com.cf.cfteam.exceptions.codeforces;

import lombok.Getter;

@Getter
public class GroupNotFoundException extends RuntimeException {

    private final Long id;

    public GroupNotFoundException(Long id) {
        super("id.not_found");
        this.id = id;
    }
}
