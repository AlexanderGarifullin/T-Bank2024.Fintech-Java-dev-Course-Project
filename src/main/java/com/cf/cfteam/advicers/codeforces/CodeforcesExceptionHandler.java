package com.cf.cfteam.advicers.codeforces;

import com.cf.cfteam.exceptions.codeforces.GroupNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CodeforcesExceptionHandler {
    private static final String ID = "id";

    @ExceptionHandler(GroupNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(GroupNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, Map.of(ID, ex.getId()));
    }

    private ResponseEntity<Object> buildErrorResponse(String message, HttpStatus status, Map<String, Object> details) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", status.value());
        errorResponse.put("error", status.getReasonPhrase());
        errorResponse.put("message", message);

        if (details != null) {
            errorResponse.put("details", details);
        }

        return new ResponseEntity<>(errorResponse, status);
    }
}
