package com.tanzeela.triangle;

/**
 * Thrown when three side lengths cannot form a valid (non-degenerate) triangle.
 */
public class InvalidTriangleException extends IllegalArgumentException {

    public InvalidTriangleException(String message) {
        super(message);
    }
}
