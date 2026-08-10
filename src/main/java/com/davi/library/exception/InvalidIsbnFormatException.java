package com.davi.library.exception;

public class InvalidIsbnFormatException extends RuntimeException {
    public InvalidIsbnFormatException(String message) {
        super(message);
    }
}
