package com.example.six_entities.exception;

public class ConvertingException extends RuntimeException {

    public ConvertingException(String message) {
        super(message);
    }

    public ConvertingException(String message, Throwable cause) {
        super(message, cause);
    }
}
