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

package com.persiantools4j.module.nationalid;

import com.persiantools4j.collection.hometown.Hometown;
import com.persiantools4j.exception.ParseException;
import com.persiantools4j.exception.ValidationException;
import com.persiantools4j.util.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@DisplayName("National ID service")
class NationalIdServiceTest {

    private static Hometown expectedHometown;
    private static Predicate<Hometown> hometownPredicate;
    private static NationalIdService nationalIdService;

    private static Stream<Arguments> validCases() {
        return Stream.of(
                Arguments.of("6104038931"),
                Arguments.of("3111001008"),
                Arguments.of("3413857606"),
                Arguments.of("2791567895"),
                Arguments.of("1583250689"),
                Arguments.of("0499370899"),
                Arguments.of("0790419904"),
                Arguments.of("0084575948"),
                Arguments.of("0963695398"),
                Arguments.of("0684159414"),
                Arguments.of("68415941"),
                Arguments.of("0067749828"),
                Arguments.of("0650451252"),
                Arguments.of("4032152314"),
                Arguments.of("3520082780"),
                Arguments.of("0076229645"),
                Arguments.of("4271467685"),
                Arguments.of("0200203241"),
                Arguments.of("1111111111"),
                Arguments.of("787833770"),
                Arguments.of("11537027"),
                Arguments.of("068415941"),
                Arguments.of(" 068415941 ")
        );
    }

    private static Stream<Arguments> validWithMultipleHometownCases() {
        return Stream.of(
                Arguments.of("2530251339"),
                Arguments.of("2884532013"),
                Arguments.of("3138159281"),
                Arguments.of("3371980421"),
                Arguments.of("3824176602"),
                Arguments.of("3857663741"),
                Arguments.of("3868929630"),
                Arguments.of("3955082385"),
                Arguments.of("4830608560"),
                Arguments.of("5934546074"),
                Arguments.of("6232055667"),
                Arguments.of("6359963809")
        );
    }

    private static Stream<Arguments> normalizeCases() {
        return Stream.of(
                Arguments.of("6104038931", "6104038931"),
                Arguments.of("68415941", "0068415941"),
                Arguments.of("067749828", "0067749828"),
                Arguments.of(" 1583250689 ", "1583250689")
        );
    }

    private static Stream<Arguments> invalidFormatCases() {
        return Stream.of(
                Arguments.of("0"),
                Arguments.of("000000"),
                Arguments.of("0000000000"),
                Arguments.of("2222222222"),
                Arguments.of("3333333333"),
                Arguments.of("4444444444"),
                Arguments.of("5555555555"),
                Arguments.of("6666666666"),
                Arguments.of("7777777777"),
                Arguments.of("8888888888"),
                Arguments.of("9999999999"),
                Arguments.of("123abc"),
                Arguments.of(" 123 "),
                Arguments.of("12345")
        );
    }

    private static Stream<Arguments> invalidCases() {
        return Stream.of(
                Arguments.of("6104038932"),
                Arguments.of("3111001003"),
                Arguments.of("3413857604"),
                Arguments.of("9731001011"),
                Arguments.of("3520082782"),
                Arguments.of("0123456789"),
                Arguments.of("1234567890"),
                Arguments.of("0684159415"),
                Arguments.of(" 6104038932 "),
                Arguments.of("12300000")
        );
    }

    @BeforeAll
    static void beforeAll() {
        nationalIdService = new NationalIdService();
        expectedHometown = new Hometown("خوی", "آذربایجان غربی", Arrays.asList("279", "280"));
        hometownPredicate = hometown -> !StringUtils.isBlank(hometown.getProvince())
                && !StringUtils.isBlank(hometown.getCity()) && !hometown.getCode().isEmpty();
    }

    @ParameterizedTest
    @DisplayName("Normalize")
    @MethodSource("normalizeCases")
    void normalizeTest(String nationalId, String expected) {
        assertThat(nationalIdService.normalize(nationalId)).isEqualTo(expected);
    }

    @Nested
    @DisplayName("Is valid")
    class IsValidTest {

        @ParameterizedTest
        @DisplayName("Valid national ID")
        @MethodSource("com.persiantools4j.module.nationalid.NationalIdServiceTest#validCases")
        void validNationalIdTest(String nationalId) {
            assertThat(nationalIdService.isValid(nationalId)).isTrue();
        }

        @ParameterizedTest
        @DisplayName("Invalid national ID")
        @MethodSource({
                "com.persiantools4j.module.nationalid.NationalIdServiceTest#invalidFormatCases",
                "com.persiantools4j.module.nationalid.NationalIdServiceTest#invalidCases"
        })
        void invalidNationalIdTest(String nationalId) {
            assertThat(nationalIdService.isValid(nationalId)).isFalse();
        }

    }

    @Nested
    @DisplayName("Validate")
    class ValidateTest {

        @ParameterizedTest
        @DisplayName("Valid national ID")
        @MethodSource("com.persiantools4j.module.nationalid.NationalIdServiceTest#validCases")
        void validateNationalIdTest(String nationalId) {
            assertThatCode(() -> nationalIdService.validate(nationalId)).doesNotThrowAnyException();
        }

        @ParameterizedTest
        @DisplayName("Exceptional national ID with null and empty")
        @ValueSource(strings = " ")
        @NullAndEmptySource
        void validateExceptionalNationalIdWithNullAndEmptyTest(String nationalId) {
            assertThatThrownBy(() -> nationalIdService.validate(nationalId))
                    .isInstanceOf(ValidationException.class)
                    .hasMessage("National ID is null or empty");
        }

        @ParameterizedTest
        @DisplayName("Exceptional national ID")
        @MethodSource("com.persiantools4j.module.nationalid.NationalIdServiceTest#invalidCases")
        void validateExceptionalNationalIdTest(String nationalId) {
            assertThatThrownBy(() -> nationalIdService.validate(nationalId))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Invalid national ID");
        }

        @ParameterizedTest
        @DisplayName("Exceptional national ID format")
        @MethodSource("com.persiantools4j.module.nationalid.NationalIdServiceTest#invalidFormatCases")
        void validateExceptionalNationalIdFormatTest(String nationalId) {
            assertThatThrownBy(() -> nationalIdService.validate(nationalId))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Invalid national ID format");
        }

    }

    @Nested
    @DisplayName("Find hometown")
    class FindHometownTest {

        @ParameterizedTest
        @DisplayName("Find hometown with valid national ID")
        @MethodSource("com.persiantools4j.module.nationalid.NationalIdServiceTest#validCases")
        void findHometownWithValidNationalIdTest(String nationalId) {
            assertThat(nationalIdService.findHometown(nationalId))
                    .isNotNull()
                    .isNotEmpty()
                    .allMatch(hometownPredicate);
        }

        @Test
        @DisplayName("Find single hometown with valid national ID")
        void findSingleHometownWithValidNationalIdTest() {
            assertThat(nationalIdService.findHometown("2791567895"))
                    .isNotNull()
                    .hasSize(1)
                    .containsOnly(expectedHometown);
        }

        @ParameterizedTest
        @DisplayName("Find multiple hometowns with valid national ID")
        @MethodSource("com.persiantools4j.module.nationalid.NationalIdServiceTest#validWithMultipleHometownCases")
        void findMultipleHometownsWithValidNationalIdTest(String nationalId) {
            assertThat(nationalIdService.findHometown(nationalId))
                    .isNotNull()
                    .hasSizeGreaterThanOrEqualTo(2);
        }

        @ParameterizedTest
        @DisplayName("Find hometown with exceptional national ID")
        @MethodSource("com.persiantools4j.module.nationalid.NationalIdServiceTest#invalidCases")
        void findHometownWithExceptionalNationalIdTest(String nationalId) {
            assertThatThrownBy(() -> nationalIdService.findHometown(nationalId))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Invalid national ID");
        }

        @ParameterizedTest
        @DisplayName("Find hometown with exceptional national ID format")
        @MethodSource("com.persiantools4j.module.nationalid.NationalIdServiceTest#invalidFormatCases")
        void findHometownWithExceptionalNationalIdFormatTest(String nationalId) {
            assertThatThrownBy(() -> nationalIdService.findHometown(nationalId))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Invalid national ID format");
        }

    }

    @Nested
    @DisplayName("Parse")
    class ParseTest {

        @ParameterizedTest
        @DisplayName("Parse with valid national ID")
        @MethodSource("com.persiantools4j.module.nationalid.NationalIdServiceTest#validCases")
        void parseWithValidNationalIdTest(String nationalId) {
            NationalId actualNationalId = nationalIdService.parse(nationalId);
            assertThat(actualNationalId).isNotNull();
            assertThat(actualNationalId.getId()).isNotBlank();
            assertThat(actualNationalId.getHometownCode())
                    .isNotBlank()
                    .hasSize(3);
            assertThat(actualNationalId.getPersonalCode())
                    .isNotBlank()
                    .hasSize(6);
            assertThat(actualNationalId.getHometownList())
                    .isNotNull()
                    .isNotEmpty()
                    .allMatch(hometownPredicate);
        }

        @Test
        @DisplayName("Parse with single valid national ID")
        void parseWithSingleValidNationalIdTest() {
            NationalId actualNationalId = nationalIdService.parse("2791567895");
            assertThat(actualNationalId).isNotNull();
            assertThat(actualNationalId.getId()).isNotBlank();
            assertThat(actualNationalId.getHometownCode()).isEqualTo("279");
            assertThat(actualNationalId.getPersonalCode()).isEqualTo("156789");
            assertThat(actualNationalId.getControlDigit()).isEqualTo(5);
            assertThat(actualNationalId.getHometownList())
                    .isNotNull()
                    .hasSize(1)
                    .containsOnly(expectedHometown);
        }

        @ParameterizedTest
        @DisplayName("Parse with exceptional national ID")
        @MethodSource("com.persiantools4j.module.nationalid.NationalIdServiceTest#invalidCases")
        void parseWithExceptionalNationalIdTest(String nationalId) {
            assertThatThrownBy(() -> nationalIdService.parse(nationalId))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Invalid national ID");
        }

        @ParameterizedTest
        @DisplayName("Parse with exceptional national ID format")
        @MethodSource("com.persiantools4j.module.nationalid.NationalIdServiceTest#invalidFormatCases")
        void parseWithExceptionalNationalIdFormatTest(String nationalId) {
            assertThatThrownBy(() -> nationalIdService.parse(nationalId))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Invalid national ID format");
        }

        @Test
        @DisplayName("Parse with national ID with missing hometown")
        void parseWithNationalIdWithMissingHometownTest() {
            assertThatThrownBy(() -> nationalIdService.parse("8908563210"))
                    .isInstanceOf(ParseException.class)
                    .hasMessageContaining("Unable to find hometown associated to the national ID");
        }

    }

}
