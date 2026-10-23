package com.tanzeela.triangle;

/**
 * Thrown when a side length cannot be parsed or is outside the supported range.
 */
public class InvalidSideLengthException extends IllegalArgumentException {

    public InvalidSideLengthException(String message) {
        super(message);
    }

    public InvalidSideLengthException(String message, Throwable cause) {
        super(message, cause);
    }
}
