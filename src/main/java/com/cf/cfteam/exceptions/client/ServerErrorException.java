package com.cf.cfteam.exceptions.client;

import lombok.Getter;

@Getter
public class ServerErrorException extends RuntimeException {

    private final String code;

    public ServerErrorException(String code) {
        super("server.error");
        this.code = code;
    }
}
