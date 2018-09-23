package com.corkili.husky.exception;

import java.io.IOException;

public class AppIOException extends IOException {

    public AppIOException(String message) {
        super(message);
    }

    public AppIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
