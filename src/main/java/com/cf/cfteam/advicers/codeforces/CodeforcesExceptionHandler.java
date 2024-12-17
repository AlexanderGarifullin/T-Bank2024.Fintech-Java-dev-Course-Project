package com.cf.cfteam.advicers.codeforces;

import com.cf.cfteam.exceptions.codeforces.*;
import com.cf.cfteam.utils.ErrorResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CodeforcesExceptionHandler {
    private static final String ID = "id";
    private static final String LOGIN = "login";
    private static final String TEAM_ID = "teamId";
    private static final String PLAYER_ID = "playerId";

    @ExceptionHandler(GroupNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(GroupNotFoundException ex) {
        return ErrorResponseBuilder.buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, Map.of(ID, ex.getId()));
    }

    @ExceptionHandler(TeamNotFoundException.class)
    public ResponseEntity<Object> handleTeamNotFoundException(TeamNotFoundException ex) {
        return ErrorResponseBuilder.buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, Map.of(ID, ex.getId()));
    }

    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<Object> handlePayerNotFoundException(PlayerNotFoundException ex) {
        return ErrorResponseBuilder.buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, Map.of(ID, ex.getId()));
    }

    @ExceptionHandler(PlayerAlreadyInTeamException.class)
    public ResponseEntity<Object> handlePlayerAlreadyInTeamException(PlayerAlreadyInTeamException ex) {
        return ErrorResponseBuilder.buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, Map.of(LOGIN, ex.getLogin()));
    }

    @ExceptionHandler(PlayerNotFromTeamException.class)
    public ResponseEntity<Object> handlePlayerNotFromTeamException(PlayerNotFromTeamException ex) {
        Map<String, Object> details = new HashMap<>();
        if (ex.getPlayerId() != null) {
            details.put(PLAYER_ID, ex.getPlayerId());
        }
        if (ex.getTeamId() != null) {
            details.put(TEAM_ID, ex.getTeamId());
        }

        return ErrorResponseBuilder.buildErrorResponse(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                details
        );
    }
}
