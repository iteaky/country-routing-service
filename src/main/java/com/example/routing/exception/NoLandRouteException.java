package com.example.routing.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoLandRouteException extends ResponseStatusException {

    public NoLandRouteException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
