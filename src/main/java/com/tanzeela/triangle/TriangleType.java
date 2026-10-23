package com.tanzeela.triangle;

/**
 * Classification of a triangle by how many of its sides are equal in length.
 */
public enum TriangleType {

    /** All three sides are equal. */
    EQUILATERAL("Equilateral"),

    /** Exactly two sides are equal. */
    ISOSCELES("Isosceles"),

    /** No two sides are equal. */
    SCALENE("Scalene");

    private final String displayName;

    TriangleType(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
