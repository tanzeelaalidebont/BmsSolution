package com.tanzeela.triangle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class SideLengthParserTest {

    @ParameterizedTest(name = "''{0}'' parses to {1}")
    @CsvSource(delimiter = '|', value = {
            "3       | 3",
            "2.5     | 2.5",
            "' 4 '   | 4",
            "1e3     | 1000",
            "1.5E-2  | 0.015",
            "1000e-5 | 0.01",
    })
    void parsesValidNumbers(String text, String expected) {
        assertEquals(0, new BigDecimal(expected).compareTo(SideLengthParser.parse(text)));
    }

    @ParameterizedTest(name = "''{0}'' is rejected")
    @ValueSource(strings = {"", "abc", "3cm", "1,5", "NaN", "Infinity", "0x10"})
    void rejectsNonNumericInput(String text) {
        assertThrows(InvalidSideLengthException.class, () -> SideLengthParser.parse(text));
    }

    @ParameterizedTest(name = "''{0}'' is rejected")
    @ValueSource(strings = {"1e101", "1e-101", "1e999999999", "1e-999999999", "1e2147483647"})
    void rejectsValuesOutsideSupportedRange(String text) {
        assertThrows(InvalidSideLengthException.class, () -> SideLengthParser.parse(text));
    }

    @ParameterizedTest(name = "''{0}'' is accepted")
    @ValueSource(strings = {"1e99", "1e-100"})
    void acceptsValuesAtEdgeOfSupportedRange(String text) {
        SideLengthParser.parse(text);
    }
}
