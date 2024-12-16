package com.cf.cfteam.advicers.codeforces;

import com.cf.cfteam.exceptions.codeforces.GroupNotFoundException;
import com.cf.cfteam.utils.ErrorResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class CodeforcesExceptionHandler {
    private static final String ID = "id";

    @ExceptionHandler(GroupNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(GroupNotFoundException ex) {
        return ErrorResponseBuilder.buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, Map.of(ID, ex.getId()));
    }

}
