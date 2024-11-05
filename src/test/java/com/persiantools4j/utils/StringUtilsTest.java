/*
 * Copyright 2024 Imorate <dev.imorate@gmail.com> and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.persiantools4j.utils;

import com.persiantools4j.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("String utils")
class StringUtilsTest {

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
        void testGetNumericValue(String str, int index, int expected) {
            assertThat(StringUtils.getNumericValue(str, index)).isEqualTo(expected);
        }

        @ParameterizedTest
        @DisplayName("Exceptional Inputs")
        @CsvSource({"1,3", "6104038932,10", "523,6", "test,1"})
        void testGetNumericValueExceptional(String str, int index) {
            assertThatThrownBy(() -> StringUtils.getNumericValue(str, index)).isInstanceOf(ValidationException.class);
        }

    }

    @Nested
    @DisplayName("Convert persian to english digits")
    class ConvertPersianToEnglishDigitsTests {

        @ParameterizedTest(name = "[{index}] {0} convert result is {1}")
        @DisplayName("Valid Inputs")
        @MethodSource("com.persiantools4j.utils.StringUtilsTest#validPersianToEnglishDigits")
        void testGetNumericValue(String str, String expected) {
            assertThat(StringUtils.convertPersianToEnglishDigits(str)).isEqualTo(expected);
        }

        @ParameterizedTest
        @DisplayName("Exceptional Inputs")
        @NullAndEmptySource
        void testConvertPersianToEnglishDigitsEmptyAndNull(String str) {
            assertThatThrownBy(() -> StringUtils.convertPersianToEnglishDigits(str)).isInstanceOf(ValidationException.class);
        }
    }

}
