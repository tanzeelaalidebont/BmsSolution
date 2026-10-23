package com.tanzeela.triangle;

import java.io.PrintStream;

/**
 * Command-line entry point.
 *
 * <p>Usage: {@code java -jar triangle-classifier.jar <sideA> <sideB> <sideC>}
 *
 * <p>Prints the triangle type to standard output and exits with {@code 0}. Invalid
 * input is reported on standard error with a non-zero exit code, so the application
 * behaves predictably when used from scripts.
 */
public final class TriangleClassifierApplication {

    static final int EXIT_SUCCESS = 0;
    static final int EXIT_INVALID_INPUT = 1;
    static final int EXIT_USAGE_ERROR = 2;

    private static final int EXPECTED_ARGUMENT_COUNT = 3;
    private static final String USAGE = "Usage: java -jar triangle-classifier.jar <sideA> <sideB> <sideC>";

    private final PrintStream out;
    private final PrintStream err;

    TriangleClassifierApplication(PrintStream out, PrintStream err) {
        this.out = out;
        this.err = err;
    }

    public static void main(String[] args) {
        int exitCode = new TriangleClassifierApplication(System.out, System.err).run(args);
        System.exit(exitCode);
    }

    int run(String[] args) {
        if (args.length != EXPECTED_ARGUMENT_COUNT) {
            err.println("Expected " + EXPECTED_ARGUMENT_COUNT + " side lengths but got " + args.length + ".");
            err.println(USAGE);
            return EXIT_USAGE_ERROR;
        }

        try {
            Triangle triangle = new Triangle(
                    SideLengthParser.parse(args[0]),
                    SideLengthParser.parse(args[1]),
                    SideLengthParser.parse(args[2]));
            out.println(triangle.classify().displayName());
            return EXIT_SUCCESS;
        } catch (InvalidSideLengthException | InvalidTriangleException e) {
            err.println("Error: " + e.getMessage());
            return EXIT_INVALID_INPUT;
        }
    }
}
