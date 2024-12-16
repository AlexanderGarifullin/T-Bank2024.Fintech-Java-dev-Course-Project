package com.cf.cfteam.advicers.security;

import com.cf.cfteam.exceptions.security.InvalidTwoFactorCodeException;
import com.cf.cfteam.exceptions.security.TokenRevokedException;
import com.cf.cfteam.exceptions.security.UserAlreadyRegisterException;
import com.cf.cfteam.exceptions.security.UserNotFoundException;
import com.cf.cfteam.utils.ErrorResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class SecurityExceptionHandler {

    private static final String LOGIN = "login";
    private static final String TOKEN = "token";

    @ExceptionHandler(UserAlreadyRegisterException.class)
    public ResponseEntity<Object> handleUserAlreadyRegisterException(UserAlreadyRegisterException ex) {
        return ErrorResponseBuilder.buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT, Map.of(LOGIN,
                ex.getLogin()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
        return ErrorResponseBuilder.buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, Map.of(LOGIN,
                ex.getLogin()));
    }

    @ExceptionHandler(TokenRevokedException.class)
    public ResponseEntity<Object> handleTokenRevokedException(TokenRevokedException ex) {
        return ErrorResponseBuilder.buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED, Map.of(TOKEN,
                ex.getToken()));
    }

    @ExceptionHandler(InvalidTwoFactorCodeException.class)
    public ResponseEntity<Object> handleInvalidTwoFactorCodeException(InvalidTwoFactorCodeException ex) {
        return ErrorResponseBuilder.buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, null);
    }
}
