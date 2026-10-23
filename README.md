# Triangle Classifier

A command-line Java application that classifies a triangle by its side lengths as
`Equilateral`, `Isosceles` or `Scalene`.

## Requirements

- Java 21+
- Maven 3.9+

## Build and run

```bash
mvn clean package
java -jar target/triangle-classifier.jar 3 4 5
# Scalene
```

| Input              | Output                                  | Exit code |
|--------------------|-----------------------------------------|-----------|
| `3 3 3`            | `Equilateral`                           | 0         |
| `3 3 4`            | `Isosceles`                             | 0         |
| `3 4 5`            | `Scalene`                               | 0         |
| `1 2 10`           | error on stderr (not a triangle)        | 1         |
| `3 four 5`         | error on stderr (not a number)          | 1         |
| `3 4`              | usage message on stderr                 | 2         |

The classification goes to standard output. Errors go to standard error with a
non-zero exit code, so the tool behaves predictably in scripts and pipelines.

## Structure

```
com.tanzeela.triangle
├── TriangleClassifierApplication  CLI entry point: arguments, output, exit codes
├── SideLengthParser               text -> BigDecimal, with range limits
├── Triangle                       immutable record: validation + classification
├── TriangleType                   EQUILATERAL / ISOSCELES / SCALENE + display name
├── InvalidSideLengthException     input error for text that is not a number or is out of range
└── InvalidTriangleException       domain error for sides that cannot form a triangle
```

The domain (`Triangle`, `TriangleType`) does no I/O, and the CLI layer contains no
geometry. A different front end (REST endpoint, interactive prompt, file input)
could reuse the domain unchanged.

## Design decisions

**`BigDecimal` rather than `double`.** The brief says "numbers", so decimals are
accepted. With binary floating point, `0.1 + 0.2 > 0.3` is true, so `0.1, 0.2, 0.3`
would be wrongly accepted as a triangle. Exact decimals avoid that. Equality uses
`compareTo` so that `2`, `2.0` and `2.00` count as the same length.

**Invalid triangles cannot be constructed.** `Triangle`'s compact constructor rejects
non-positive sides and sides that break the triangle inequality. Any `Triangle`
instance is therefore valid, and `classify()` needs no defensive checks.

**Degenerate triangles are rejected.** Sides such as `1, 2, 3` (where one side equals
the sum of the other two) describe a flat line, not a triangle, so the strict
inequality is used.

**Input range is bounded.** `BigDecimal` accepts `1e999999999`, but adding it to `1`
needs a billion-digit result, which overflows or exhausts memory. The parser limits
inputs to 100 integer digits and 100 decimal places, far beyond any realistic length.

**Classification lives on the `Triangle` record.** It is a property of the triangle
itself and only a few lines long. A separate `TriangleClassifier` service or a
strategy interface would add indirection with no current benefit. If classification
by angle (acute/right/obtuse) were needed later, it could be added as a second
method and enum without changing the existing code.

**Display text lives on the enum.** The required output strings (`"Equilateral"` etc.)
are kept separate from the enum constant names, so the output wording can change
without renaming constants.

**Testable entry point.** `main` only wires up `System.out`/`System.err` and calls
`System.exit`. The logic is in `run(String[])`, which returns an exit code and writes
to injected streams, so it can be unit tested without spawning a process.

## Limitations and possible extensions

- Input comes from command-line arguments only. Interactive or stdin input would be a
  small addition in the application layer.
- Output is English-only text. Localisation could be added by resolving
  `TriangleType` names through a resource bundle.
- No dependencies beyond JUnit. A CLI library such as picocli would help if the
  interface grew more options, but would be overkill for three positional arguments.

## Tests

```bash
mvn test
```

Unit tests cover each classification (including every position of the equal pair
for isosceles), non-positive, degenerate and impossible triangles, number parsing
and range limits, and the CLI's output streams and exit codes.
