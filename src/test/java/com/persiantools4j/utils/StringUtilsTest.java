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
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.Collections;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("String utils")
class StringUtilsTest {

    private static Stream<Arguments> getNumericValueCases() {
        return Stream.of(
                Arguments.of("1", "0", "1"),
                Arguments.of("6104038932", "9", "2"),
                Arguments.of("523", "2", "3")
        );
    }

    private static Stream<Arguments> exceptionalGetNumericValueCases() {
        return Stream.of(
                Arguments.of("1", "3"),
                Arguments.of("6104038932", "10"),
                Arguments.of("523", "6"),
                Arguments.of("test", "1")
        );
    }

    private static Stream<Arguments> toEnglishDigitsCases() {
        return Stream.of(
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

    private static Stream<Arguments> persianStringCases() {
        return Stream.of(
                Arguments.of("۰۱۲۳۴۵۶۷۸۹"),
                Arguments.of("0123456789"),
                Arguments.of("سلام! این یک متن \"تست\"، جهت بررسی متون فارسی می‌باشد."),
                Arguments.of("صرفاً یک تست"),
                Arguments.of("تِسُتَ"),
                Arguments.of("تٍسٌتً"),
                Arguments.of("گچپژ"),
                Arguments.of("ؤإأءئ"),
                Arguments.of("\u200Cـ،«»؛؟٬,؍٫٪"),
                Arguments.of("!@#$%^&*()_\\-=+\\/{}\\[\\]\"':;?<>|.~`,×÷€£¥")
        );
    }

    private static Stream<Arguments> normalizePersianCases() {
        return Stream.of(
                Arguments.of("اين يك تست كاربردي مي باشد", "این یک تست کاربردی می باشد"),
                Arguments.of("٠١٢٣٤٥٦٧٨٩", "۰۱۲۳۴۵۶۷۸۹"),
                Arguments.of("ؠ ؽ ؾ ؿ ي ٸ ۍ ێ ۑ ے ۓ", String.join(" ", Collections.nCopies(11, "ی"))),
                Arguments.of("ٶ ۄ ۊ ۏ", String.join(" ", Collections.nCopies(4, "و"))),
                Arguments.of("ك ڪ ګ ڬ ڮ ػ ؼ", String.join(" ", Collections.nCopies(7, "ک")))
        );
    }

    @Nested
    @DisplayName("Get numeric value")
    class GetNumericValueTest {

        @ParameterizedTest
        @DisplayName("Valid inputs")
        @MethodSource("com.persiantools4j.utils.StringUtilsTest#getNumericValueCases")
        void testGetNumericValue(String str, int index, int expected) {
            assertThat(StringUtils.getNumericValue(str, index)).isEqualTo(expected);
        }

        @ParameterizedTest
        @DisplayName("Exceptional inputs")
        @MethodSource("com.persiantools4j.utils.StringUtilsTest#exceptionalGetNumericValueCases")
        void testExceptionalGetNumericValue(String str, int index) {
            assertThatThrownBy(() -> StringUtils.getNumericValue(str, index))
                    .isInstanceOf(ValidationException.class)
                    .hasMessage("Invalid number");
        }

    }

    @Nested
    @DisplayName("Convert Persian/Arabic to english digits")
    class ConvertPersianArabicToEnglishDigitsTest {

        @ParameterizedTest
        @DisplayName("Valid inputs")
        @MethodSource("com.persiantools4j.utils.StringUtilsTest#toEnglishDigitsCases")
        void testToEnglishDigits(String str, String expected) {
            assertThat(StringUtils.toEnglishDigits(str)).isEqualTo(expected);
        }

        @ParameterizedTest
        @DisplayName("Exceptional inputs")
        @NullAndEmptySource
        void testEmptyAndNullToEnglishDigits(String str) {
            assertThatThrownBy(() -> StringUtils.toEnglishDigits(str))
                    .isInstanceOf(ValidationException.class)
                    .hasMessage("Input string is null or empty");
        }

    }

    @Nested
    @DisplayName("isPersian verification")
    class IsPersianVerificationTest {

        @ParameterizedTest
        @DisplayName("Valid inputs")
        @MethodSource("com.persiantools4j.utils.StringUtilsTest#persianStringCases")
        void testIsPersian(String str) {
            assertThat(StringUtils.isPersian(str)).isTrue();
        }

        @ParameterizedTest
        @DisplayName("Exceptional inputs")
        @NullAndEmptySource
        void testEmptyAndNullIsPersian(String str) {
            assertThatThrownBy(() -> StringUtils.isPersian(str))
                    .isInstanceOf(ValidationException.class)
                    .hasMessage("Input string is null or empty");
        }

    }

    @Nested
    @DisplayName("Normalize persian")
    class NormalizePersianTest {

        @ParameterizedTest
        @DisplayName("Valid inputs")
        @MethodSource("com.persiantools4j.utils.StringUtilsTest#normalizePersianCases")
        void testNormalizePersian(String str, String expected) {
            assertThat(StringUtils.isPersian(str)).isFalse();
            assertThat(StringUtils.normalizePersian(str)).isEqualTo(expected);
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
