/*
 * Copyright 2024 Imorate (Amir Mohammad Hl) <dev.imorate@gmail.com> and contributors.
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
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("String utils")
class StringUtilsTest {

    private static Stream<Arguments> isBlankCases() {
        return Stream.of(
                Arguments.of(null, true),
                Arguments.of("", true),
                Arguments.of(" ", true),
                Arguments.of("a", false),
                Arguments.of("foo", false),
                Arguments.of("  foo  ", false)
        );
    }

    private static Stream<Arguments> getNumericValueCases() {
        return Stream.of(
                Arguments.of("1", "0", "1"),
                Arguments.of("6104038932", "9", "2"),
                Arguments.of("523", "2", "3")
        );
    }

    private static Stream<Arguments> exceptionalNullOrEmptyGetNumericValueCases() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(""),
                Arguments.of(" ")
        );
    }

    private static Stream<Arguments> exceptionalInvalidGetNumericValueCases() {
        return Stream.of(
                Arguments.of("1", "3"),
                Arguments.of("6104038932", "10"),
                Arguments.of("523", "6"),
                Arguments.of("test", "1")
        );
    }

    private static Stream<Arguments> toEnglishDigitsCases() {
        return Stream.of(
                Arguments.of(null, ""),
                Arguments.of("", ""),
                Arguments.of(" ", ""),
                Arguments.of("123", "123"),
                //Persian cases
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

    private static Stream<Arguments> isPersianStringCases() {
        return Stream.of(
                Arguments.of(null, false),
                Arguments.of("", false),
                Arguments.of(" ", false),
                Arguments.of("۰۱۲۳۴۵۶۷۸۹", true),
                Arguments.of("0123456789", true),
                Arguments.of("سلام! این یک متن \"تست\"، جهت بررسی متون فارسی می‌باشد.", true),
                Arguments.of("صرفاً یک تست", true),
                Arguments.of("تِسُتَ", true),
                Arguments.of("تٍسٌتً", true),
                Arguments.of("گچپژ", true),
                Arguments.of("ؤإأءئ", true),
                Arguments.of("\u200Cـ،«»؛؟٬,؍٫٪", true),
                Arguments.of("!@#$%^&*()_\\-=+\\/{}\\[\\]\"':;?<>|.~`,×÷€£¥", true)
        );
    }

    private static Stream<Arguments> normalizePersianCases() {
        return Stream.of(
                Arguments.of(null, ""),
                Arguments.of("", ""),
                Arguments.of(" ", ""),
                Arguments.of("اين يك تست كاربردي مي باشد", "این یک تست کاربردی می باشد"),
                Arguments.of("٠١٢٣٤٥٦٧٨٩", "۰۱۲۳۴۵۶۷۸۹"),
                Arguments.of("ؠ ؽ ؾ ؿ ي ٸ ۍ ێ ۑ ے ۓ", String.join(" ", Collections.nCopies(11, "ی"))),
                Arguments.of("ٶ ۄ ۊ ۏ", String.join(" ", Collections.nCopies(4, "و"))),
                Arguments.of("ك ڪ ګ ڬ ڮ ػ ؼ", String.join(" ", Collections.nCopies(7, "ک")))
        );
    }

    @ParameterizedTest
    @DisplayName("Is blank")
    @MethodSource("com.persiantools4j.utils.StringUtilsTest#isBlankCases")
    void testIsBlank(String input, boolean expected) {
        assertThat(StringUtils.isBlank(input)).isEqualTo(expected);
    }

    @ParameterizedTest
    @DisplayName("To english digits")
    @MethodSource("com.persiantools4j.utils.StringUtilsTest#toEnglishDigitsCases")
    void testToEnglishDigits(String input, String expected) {
        assertThat(StringUtils.toEnglishDigits(input)).isEqualTo(expected);
    }

    @ParameterizedTest
    @DisplayName("Normalize persian")
    @MethodSource("com.persiantools4j.utils.StringUtilsTest#normalizePersianCases")
    void testNormalizePersian(String input, String expected) {
        assertThat(StringUtils.isPersian(input)).isFalse();
        assertThat(StringUtils.normalizePersian(input)).isEqualTo(expected);
    }

    @ParameterizedTest
    @DisplayName("Is persian")
    @MethodSource("com.persiantools4j.utils.StringUtilsTest#isPersianStringCases")
    void testIsPersian(String input, boolean expected) {
        assertThat(StringUtils.isPersian(input)).isEqualTo(expected);
    }

    @Nested
    @DisplayName("Get numeric value")
    class GetNumericValueTest {

        @ParameterizedTest
        @DisplayName("Valid inputs")
        @MethodSource("com.persiantools4j.utils.StringUtilsTest#getNumericValueCases")
        void testGetNumericValue(String input, int index, int expected) {
            assertThat(StringUtils.getNumericValue(input, index)).isEqualTo(expected);
        }

        @ParameterizedTest
        @DisplayName("Exceptional empty or null inputs")
        @MethodSource("com.persiantools4j.utils.StringUtilsTest#exceptionalNullOrEmptyGetNumericValueCases")
        void testExceptionalEmptyOrNullGetNumericValue(String input) {
            assertThatThrownBy(() -> StringUtils.getNumericValue(input, 1))
                    .isInstanceOf(ValidationException.class)
                    .hasMessage(StringUtils.NULL_OR_EMPTY_EXCEPTION_MESSAGE);
        }

        @ParameterizedTest
        @DisplayName("Exceptional invalid inputs")
        @MethodSource("com.persiantools4j.utils.StringUtilsTest#exceptionalInvalidGetNumericValueCases")
        void testExceptionalInvalidGetNumericValue(String input, int index) {
            assertThatThrownBy(() -> StringUtils.getNumericValue(input, index))
                    .isInstanceOf(ValidationException.class)
                    .hasMessage("Invalid number");
        }

    }

}
