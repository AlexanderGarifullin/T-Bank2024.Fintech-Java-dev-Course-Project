package com.cf.cfteam.advicers.security;

import com.cf.cfteam.exceptions.security.InvalidTwoFactorCodeException;
import com.cf.cfteam.exceptions.security.TokenRevokedException;
import com.cf.cfteam.exceptions.security.UserAlreadyRegisterException;
import com.cf.cfteam.exceptions.security.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class SecurityExceptionHandler {

    private static final String LOGIN = "login";
    private static final String TOKEN = "token";
    private static final String UNEXPECTED_ERROR = "unexpected.error";

    @ExceptionHandler(UserAlreadyRegisterException.class)
    public ResponseEntity<Object> handleUserAlreadyRegisterException(UserAlreadyRegisterException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT, Map.of(LOGIN, ex.getLogin()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, Map.of(LOGIN, ex.getLogin()));
    }

    @ExceptionHandler(TokenRevokedException.class)
    public ResponseEntity<Object> handleTokenRevokedException(TokenRevokedException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED, Map.of(TOKEN, ex.getToken()));
    }

    @ExceptionHandler(InvalidTwoFactorCodeException.class)
    public ResponseEntity<Object> handleInvalidTwoFactorCodeException(InvalidTwoFactorCodeException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        return buildErrorResponse(UNEXPECTED_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, null);
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
