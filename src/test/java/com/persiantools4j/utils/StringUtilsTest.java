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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("String utils")
class StringUtilsTest {

    private static Stream<Arguments> validNumericValueCases() {
        return Stream.of(
                Arguments.of("1", "0", "1"),
                Arguments.of("6104038932", "9", "2"),
                Arguments.of("523", "2", "3")
        );
    }

    private static Stream<Arguments> invalidNumericValueCases() {
        return Stream.of(
                Arguments.of("1", "3"),
                Arguments.of("6104038932", "10"),
                Arguments.of("523", "6"),
                Arguments.of("test", "1")
        );
    }

    private static Stream<Arguments> validToEnglishDigitsCases() {
        return Stream.of(
                Arguments.of("123", "123"),
                //Persian cases
                Arguments.of("۱۲۳", "123"),
                Arguments.of(" ۱۲۳ ", "123"),
                Arguments.of("۰۱۲۳۴۵۶۷۸۹", "0123456789"),
                Arguments.of("۱" + "test" + "۲۳", "1test23"),
                Arguments.of("۱" + "تست" + "۲", "1" + "تست" + "2"),
                //Arabic cases
                Arguments.of("٠١٢٣٤٥٦٧٨٩", "0123456789"),
                Arguments.of("١" + "test" + "٢٣", "1test23"),
                Arguments.of("١" + "تست" + "٢", "1" + "تست" + "2")
        );
    }

    @Nested
    @DisplayName("Get Numeric Value Tests")
    class GetNumericValueTests {

        @ParameterizedTest
        @DisplayName("Valid inputs")
        @MethodSource("com.persiantools4j.utils.StringUtilsTest#validNumericValueCases")
        void testGetNumericValue(String str, int index, int expected) {
            assertThat(StringUtils.getNumericValue(str, index)).isEqualTo(expected);
        }

        @ParameterizedTest
        @DisplayName("Exceptional inputs")
        @MethodSource("com.persiantools4j.utils.StringUtilsTest#invalidNumericValueCases")
        void testGetNumericValueExceptional(String str, int index) {
            assertThatThrownBy(() -> StringUtils.getNumericValue(str, index))
                    .isInstanceOf(ValidationException.class)
                    .hasMessage("Invalid number");
        }

    }

    @Nested
    @DisplayName("Convert Persian/Arabic to english digits")
    class ConvertPersianArabicToEnglishDigitsTests {

        @ParameterizedTest
        @DisplayName("Valid inputs")
        @MethodSource("com.persiantools4j.utils.StringUtilsTest#validToEnglishDigitsCases")
        void testToEnglishDigits(String str, String expected) {
            assertThat(StringUtils.toEnglishDigits(str)).isEqualTo(expected);
        }

        @ParameterizedTest
        @DisplayName("Exceptional inputs")
        @NullAndEmptySource
        void testToEnglishDigitsWithEmptyAndNull(String str) {
            assertThatThrownBy(() -> StringUtils.toEnglishDigits(str))
                    .isInstanceOf(ValidationException.class)
                    .hasMessage("Input string is null or empty");
        }

    }

    @Nested
    @DisplayName("isPersian tests")
    class IsPersianMethodTests {

        @Test
        @DisplayName("Valid inputs")
        void testIsPersian() {
            String str = "سلام! این یک متن \"تست\"، جهت بررسی متون فارسی می‌باشد.";
            str += "۰۱۲۳۴۵۶۷۸۹";
            str += "0123456789";
            str += "صرفاً یک تست";
            assertThat(StringUtils.isPersian(str)).isTrue();
        }

        @ParameterizedTest
        @DisplayName("Exceptional inputs")
        @NullAndEmptySource
        void testIsPersianWithEmptyAndNull(String str) {
            assertThatThrownBy(() -> StringUtils.isPersian(str))
                    .isInstanceOf(ValidationException.class)
                    .hasMessage("Input string is null or empty");
        }

    }

    @Nested
    @DisplayName("Normalize persian tests")
    class NormalizePersianTests {

        @Test
        @DisplayName("Valid inputs")
        void testNormalizePersian() {
            String actual = "اين يك تست كاربردي مي باشد";
            String expected = "این یک تست کاربردی می باشد";
            assertThat(StringUtils.normalizePersian(actual)).isEqualTo(expected);
        }

        @ParameterizedTest
        @DisplayName("Exceptional inputs")
        @NullAndEmptySource
        void testNormalizePersianWithEmptyAndNull(String str) {
            assertThatThrownBy(() -> StringUtils.normalizePersian(str))
                    .isInstanceOf(ValidationException.class)
                    .hasMessage("Input string is null or empty");
        }

    }

}
