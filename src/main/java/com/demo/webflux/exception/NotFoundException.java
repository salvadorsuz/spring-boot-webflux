package com.demo.webflux.exception;

public class NotFoundException extends Exception {

    public NotFoundException(String message) {
        super(message);
    }
}
