package com.corkili.husky.exception;

public class AppIOException extends Exception {

    public AppIOException(String message) {
        super(message);
    }

    public AppIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
