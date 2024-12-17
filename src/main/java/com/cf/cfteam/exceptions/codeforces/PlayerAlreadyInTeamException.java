package com.cf.cfteam.exceptions.codeforces;

import lombok.Getter;

@Getter
public class PlayerAlreadyInTeamException extends RuntimeException {
    private final String login;

    public PlayerAlreadyInTeamException(String login) {
        super("player.already_in_team");
        this.login = login;
    }
}
