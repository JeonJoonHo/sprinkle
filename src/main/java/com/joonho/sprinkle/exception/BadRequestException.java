package com.joonho.sprinkle.exception;

public class BadRequestException extends RuntimeException {

    private final Object id;
    private final String message;

    public BadRequestException(Object id, String message) {
        this.id = id;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
