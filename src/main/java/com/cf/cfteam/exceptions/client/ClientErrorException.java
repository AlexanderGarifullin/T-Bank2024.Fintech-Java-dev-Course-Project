package com.cf.cfteam.exceptions.client;

public class ClientErrorException extends RuntimeException {

    private final String code;

    public ClientErrorException(String code) {
        super("server.error");
        this.code = code;
    }
}
