package com.cf.cfteam.exceptions.codeforces;

import lombok.Getter;

@Getter
public class PlayerNotFromTeamException extends RuntimeException {

    private final Long teamId, playerId;

    public PlayerNotFromTeamException(Long teamId, Long playerId) {
        super("player.not_from_team");
        this.playerId = playerId;
        this.teamId = teamId;
    }
}