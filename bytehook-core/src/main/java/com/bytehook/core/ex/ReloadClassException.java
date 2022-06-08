package com.bytehook.core.ex;

/**
 * @author xtherk
 */
public class ReloadClassException extends RuntimeException {
    public ReloadClassException(String message) {
        super(message);
    }

    public ReloadClassException(String message, Throwable cause) {
        super(message, cause);
    }
}
