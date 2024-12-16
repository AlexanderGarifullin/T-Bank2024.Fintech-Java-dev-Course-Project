package com.cf.cfteam.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public final class ErrorResponseBuilder {

    public static ResponseEntity<Object> buildErrorResponse(String message, HttpStatus status,
                                                            Map<String, Object> details) {
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

    private ErrorResponseBuilder() {
        throw new UnsupportedOperationException("Utility class");
    }
}