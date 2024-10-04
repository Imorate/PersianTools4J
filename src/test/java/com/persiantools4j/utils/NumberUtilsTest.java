package com.persiantools4j.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("Number utils")
class NumberUtilsTest {

    private static Stream<Arguments> validPersianToEnglishDigits() {
        return Stream.of(
                Arguments.of("123", "123"),
                Arguments.of("۱۲۳۴۵۶۷۸۹۰", "1234567890"),
                Arguments.of("۱" + "test" + "۲۳", "1test23"),
                Arguments.of("۱" + "تست" + "۲", "1" + "تست" + "2")
        );
    }

    @Nested
    @DisplayName("Get Numeric Value Tests")
    class GetNumericValueTests {

        @ParameterizedTest(name = "[{index}] {0} {1}nth numeric character value is {2}")
        @DisplayName("Valid Inputs")
        @CsvSource({"1,0,1", "6104038932,9,2", "523,2,3"})
        void getNumericValueTest(String str, int index, int expected) {
            assertThat(NumberUtils.getNumericValue(str, index)).isEqualTo(expected);
        }

        @ParameterizedTest
        @DisplayName("Exceptional Inputs")
        @CsvSource({"1,3", "6104038932,10", "523,6", "test,1"})
        void getNumericValueExceptionalTest(String str, int index) {
            assertThatThrownBy(() -> NumberUtils.getNumericValue(str, index)).isInstanceOf(IllegalArgumentException.class);
        }

    }

    @Nested
    @DisplayName("Convert persian to english digits")
    class ConvertPersianToEnglishDigitsTests {

        @ParameterizedTest(name = "[{index}] {0} convert result is {1}")
        @DisplayName("Valid Inputs")
        @MethodSource("com.persiantools4j.utils.NumberUtilsTest#validPersianToEnglishDigits")
        void getNumericValueTest(String str, String expected) {
            assertThat(NumberUtils.convertPersianToEnglishDigits(str)).isEqualTo(expected);
        }

        @ParameterizedTest
        @DisplayName("Exceptional Inputs")
        @NullAndEmptySource
        void convertPersianToEnglishDigitsEmptyAndNullTest(String str) {
            assertThatThrownBy(() -> NumberUtils.convertPersianToEnglishDigits(str)).isInstanceOf(IllegalArgumentException.class);
        }
    }

}
