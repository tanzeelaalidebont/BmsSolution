package com.tanzeela.triangle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class TriangleTest {

    @ParameterizedTest(name = "{0}, {1}, {2} is equilateral")
    @CsvSource({
            "1, 1, 1",
            "2.5, 2.5, 2.5",
            "2, 2.0, 2.00",
    })
    void classifiesEquilateral(String sideA, String sideB, String sideC) {
        assertEquals(TriangleType.EQUILATERAL, triangle(sideA, sideB, sideC).classify());
    }

    @ParameterizedTest(name = "{0}, {1}, {2} is isosceles")
    @CsvSource({
            "2, 2, 3",
            "2, 3, 2",
            "3, 2, 2",
            "5, 5.0, 1",
            "0.1, 0.1, 0.15",
    })
    void classifiesIsoscelesRegardlessOfWhichSidesAreEqual(String sideA, String sideB, String sideC) {
        assertEquals(TriangleType.ISOSCELES, triangle(sideA, sideB, sideC).classify());
    }

    @ParameterizedTest(name = "{0}, {1}, {2} is scalene")
    @CsvSource({
            "3, 4, 5",
            "5, 4, 3",
            "4.5, 6.1, 7.25",
    })
    void classifiesScalene(String sideA, String sideB, String sideC) {
        assertEquals(TriangleType.SCALENE, triangle(sideA, sideB, sideC).classify());
    }

    @ParameterizedTest(name = "{0}, {1}, {2} is rejected")
    @CsvSource({
            "0, 1, 1",
            "1, 0, 1",
            "1, 1, 0",
            "-1, 2, 2",
            "2, -1, 2",
            "2, 2, -1",
    })
    void rejectsNonPositiveSides(String sideA, String sideB, String sideC) {
        assertThrows(InvalidTriangleException.class, () -> triangle(sideA, sideB, sideC));
    }

    @ParameterizedTest(name = "{0}, {1}, {2} is rejected")
    @CsvSource({
            "1, 2, 10",
            "10, 1, 2",
            "1, 10, 2",
    })
    void rejectsSidesViolatingTriangleInequality(String sideA, String sideB, String sideC) {
        assertThrows(InvalidTriangleException.class, () -> triangle(sideA, sideB, sideC));
    }

    @ParameterizedTest(name = "{0}, {1}, {2} is rejected")
    @CsvSource({
            "1, 2, 3",
            "3, 1, 2",
            "2, 3, 1",
            "0.1, 0.2, 0.3",
    })
    void rejectsDegenerateTriangles(String sideA, String sideB, String sideC) {
        assertThrows(InvalidTriangleException.class, () -> triangle(sideA, sideB, sideC));
    }

    @Test
    void treatsTrailingZerosAsTheSameLength() {
        assertEquals(triangle("2", "3", "4"), triangle("2.0", "3.00", "4"));
    }

    @Test
    void rejectsNullSides() {
        assertThrows(NullPointerException.class, () -> new Triangle(null, BigDecimal.ONE, BigDecimal.ONE));
    }

    private static Triangle triangle(String sideA, String sideB, String sideC) {
        return new Triangle(new BigDecimal(sideA), new BigDecimal(sideB), new BigDecimal(sideC));
    }
}
