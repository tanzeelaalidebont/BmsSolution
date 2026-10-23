package com.tanzeela.triangle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class TriangleClassifierApplicationTest {

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final ByteArrayOutputStream err = new ByteArrayOutputStream();
    private final TriangleClassifierApplication application = new TriangleClassifierApplication(
            new PrintStream(out, true, StandardCharsets.UTF_8),
            new PrintStream(err, true, StandardCharsets.UTF_8));

    @Test
    void printsEquilateral() {
        assertClassifiedAs("Equilateral", "3", "3", "3");
    }

    @Test
    void printsIsosceles() {
        assertClassifiedAs("Isosceles", "3", "3", "4");
    }

    @Test
    void printsScalene() {
        assertClassifiedAs("Scalene", "3", "4", "5");
    }

    @Test
    void reportsUsageErrorWhenArgumentCountIsWrong() {
        int exitCode = application.run(new String[] {"3", "4"});

        assertEquals(TriangleClassifierApplication.EXIT_USAGE_ERROR, exitCode);
        assertEquals("", stdout());
        assertTrue(stderr().contains("Usage:"));
    }

    @Test
    void reportsNonNumericInput() {
        int exitCode = application.run(new String[] {"3", "four", "5"});

        assertEquals(TriangleClassifierApplication.EXIT_INVALID_INPUT, exitCode);
        assertEquals("", stdout());
        assertTrue(stderr().contains("'four' is not a valid number"));
    }

    @Test
    void reportsSidesThatDoNotFormATriangle() {
        int exitCode = application.run(new String[] {"1", "2", "10"});

        assertEquals(TriangleClassifierApplication.EXIT_INVALID_INPUT, exitCode);
        assertEquals("", stdout());
        assertTrue(stderr().contains("do not form a triangle"));
    }

    private void assertClassifiedAs(String expectedOutput, String... args) {
        int exitCode = application.run(args);

        assertEquals(TriangleClassifierApplication.EXIT_SUCCESS, exitCode);
        assertEquals(expectedOutput, stdout().strip());
        assertEquals("", stderr());
    }

    private String stdout() {
        return out.toString(StandardCharsets.UTF_8);
    }

    private String stderr() {
        return err.toString(StandardCharsets.UTF_8);
    }
}
