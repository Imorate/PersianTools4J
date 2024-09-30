package com.persiantools4j.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("Number utils")
class NumberUtilsTest {

    @Nested
    @DisplayName("Get Numeric Value Tests")
    class GetNumericValueTests {

        @ParameterizedTest(name = "[{index}] {0} {1}nth numeric character value is {2}")
        @DisplayName("Valid Inputs")
        @CsvSource({"1,0,1", "6104038932,9,2", "523,2,3"})
        void getNumericValueTest(String str, int index, int expected) {
            assertThat(NumberUtils.getNumericValue(str, index)).isEqualTo(expected);
        }

        @ParameterizedTest(name = "[{index}] {0} {1}nth numeric character value")
        @DisplayName("Exceptional Inputs")
        @CsvSource({"1,3", "6104038932,10", "523,6", "test,1"})
        void getNumericValueExceptionalTest(String str, int index) {
            assertThatThrownBy(() -> NumberUtils.getNumericValue(str, index)).isInstanceOf(IllegalArgumentException.class);
        }

    }

}
