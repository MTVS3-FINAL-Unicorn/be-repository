package com.ohgiraffers.unicorn.error.exception;

public class InternalServerException extends RuntimeException {

    public static final InternalServerException EXCEPTION = new InternalServerException("Internal server error occurred");

    public InternalServerException(String message) {
        super(message);
    }

    public InternalServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
