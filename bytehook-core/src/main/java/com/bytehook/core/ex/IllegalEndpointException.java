package com.bytehook.core.ex;

/**
 * If the endpoint illegal is thrown out of this exception.
 *
 * @author xtherk
 */
public class IllegalEndpointException extends RuntimeException {

    public IllegalEndpointException(String message) {
        super(message);
    }
}
