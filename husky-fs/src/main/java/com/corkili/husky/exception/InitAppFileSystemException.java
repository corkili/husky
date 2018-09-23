package com.corkili.husky.exception;

public class InitAppFileSystemException extends RuntimeException {

    public InitAppFileSystemException(String message) {
        super(message);
    }

    public InitAppFileSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public InitAppFileSystemException(Throwable cause) {
        super(cause);
    }

    public InitAppFileSystemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
