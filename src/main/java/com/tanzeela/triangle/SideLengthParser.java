package com.tanzeela.triangle;

import java.math.BigDecimal;

/**
 * Parses a textual side length into an exact decimal value.
 *
 * <p>Accepts plain and scientific notation (e.g. {@code 3}, {@code 2.5}, {@code 1e3}).
 * Values are bounded in magnitude and precision: {@link BigDecimal} can represent
 * inputs such as {@code 1e999999999}, but adding it to a small number would require
 * a billion-digit result and either exhaust memory or overflow.
 */
final class SideLengthParser {

    static final int MAX_INTEGER_DIGITS = 100;
    static final int MAX_FRACTION_DIGITS = 100;

    private SideLengthParser() {
    }

    static BigDecimal parse(String text) {
        BigDecimal value;
        try {
            value = new BigDecimal(text.strip());
        } catch (NumberFormatException e) {
            throw new InvalidSideLengthException("'" + text + "' is not a valid number", e);
        }

        BigDecimal normalised = value.stripTrailingZeros();
        // long, because the scale of an input such as 1e2147483647 would overflow int here.
        long integerDigits = (long) normalised.precision() - normalised.scale();
        if (integerDigits > MAX_INTEGER_DIGITS || normalised.scale() > MAX_FRACTION_DIGITS) {
            throw new InvalidSideLengthException(
                    "'%s' is outside the supported range (at most %d integer digits and %d decimal places)"
                            .formatted(text, MAX_INTEGER_DIGITS, MAX_FRACTION_DIGITS));
        }
        return value;
    }
}
