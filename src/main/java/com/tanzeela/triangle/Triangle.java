package com.tanzeela.triangle;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * An immutable triangle defined by its three side lengths.
 *
 * <p>Side lengths are held as {@link BigDecimal} so that equality and the triangle
 * inequality are evaluated exactly. With {@code double}, inputs such as
 * {@code 0.1, 0.2, 0.3} would be wrongly accepted as a triangle because
 * {@code 0.1 + 0.2 > 0.3} in binary floating point.
 *
 * <p>Construction fails with {@link InvalidTriangleException} unless every side is
 * strictly positive and the sides satisfy the strict triangle inequality, so any
 * {@code Triangle} instance is guaranteed to be classifiable.
 *
 * <p>Sides are stored with trailing zeros stripped, so {@code new Triangle(2, 2, 2)}
 * and {@code new Triangle(2.0, 2, 2.00)} are equal.
 */
public record Triangle(BigDecimal sideA, BigDecimal sideB, BigDecimal sideC) {

    public Triangle {
        Objects.requireNonNull(sideA, "sideA must not be null");
        Objects.requireNonNull(sideB, "sideB must not be null");
        Objects.requireNonNull(sideC, "sideC must not be null");
        // Normalised so that record equality treats 2 and 2.0 as the same length, as classify() does.
        sideA = sideA.stripTrailingZeros();
        sideB = sideB.stripTrailingZeros();
        sideC = sideC.stripTrailingZeros();
        requirePositive(sideA, sideB, sideC);
        triangleSideLengthCheck(sideA, sideB, sideC);
    }

    public TriangleType classify() {
        // compareTo rather than equals, so that 2 and 2.0 are treated as the same length.
        boolean aEqualsB = sideA.compareTo(sideB) == 0;
        boolean bEqualsC = sideB.compareTo(sideC) == 0;
        boolean aEqualsC = sideA.compareTo(sideC) == 0;

        if (aEqualsB && bEqualsC) {
            return TriangleType.EQUILATERAL;
        }
        if (aEqualsB || bEqualsC || aEqualsC) {
            return TriangleType.ISOSCELES;
        }
        return TriangleType.SCALENE;
    }

    private static void requirePositive(BigDecimal... sides) {
        for (BigDecimal side : sides) {
            if (side.signum() <= 0) {
                throw new InvalidTriangleException(
                        "Side lengths must be greater than zero, but got " + side.toPlainString());
            }
        }
    }

    /**
     * Each side must be strictly shorter than the sum of the other two. Sides that are
     * exactly equal to that sum describe a degenerate (flat) triangle, which is rejected.
     */
    private static void triangleSideLengthCheck(BigDecimal sideA, BigDecimal sideB, BigDecimal sideC) {
        if (sideA.add(sideB).compareTo(sideC) <= 0
                || sideA.add(sideC).compareTo(sideB) <= 0
                || sideB.add(sideC).compareTo(sideA) <= 0) {
            throw new InvalidTriangleException(
                    "Side lengths %s, %s and %s do not form a triangle: each side must be shorter than the sum of the other two"
                            .formatted(sideA.toPlainString(), sideB.toPlainString(), sideC.toPlainString()));
        }
    }
}
