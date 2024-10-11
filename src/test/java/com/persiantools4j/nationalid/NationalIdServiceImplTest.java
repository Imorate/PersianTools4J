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

package com.persiantools4j.nationalid;

import com.persiantools4j.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DisplayName("National ID service")
class NationalIdServiceImplTest {

    private NationalIdService nationalIdService;

    private static Stream<Arguments> validNationalIdCases() {
        return Stream.of(
                Arguments.of("6104038931"),
                Arguments.of("3111001008"),
                Arguments.of("3413857606"),
                Arguments.of("2791567895"),
                Arguments.of("3520082780")
        );
    }

    private static Stream<Arguments> invalidNationalIdFormatCases() {
        return Stream.of(
                Arguments.of(""),
                Arguments.of((Object) null),
                Arguments.of(" 6104038932 "),
                Arguments.of("3111001003 "),
                Arguments.of(" 3413857604"),
                Arguments.of("123abc"),
                Arguments.of("123")
        );
    }

    private static Stream<Arguments> invalidNationalIdCases() {
        return Stream.of(
                Arguments.of("6104038932"),
                Arguments.of("3111001003"),
                Arguments.of("3413857604"),
                Arguments.of("9731001011"),
                Arguments.of("3520082782"),
                Arguments.of("0000000000"),
                Arguments.of("1111111111"),
                Arguments.of("2222222222"),
                Arguments.of("3333333333"),
                Arguments.of("4444444444"),
                Arguments.of("5555555555"),
                Arguments.of("6666666666"),
                Arguments.of("7777777777"),
                Arguments.of("8888888888"),
                Arguments.of("9999999999")
        );
    }

    @BeforeEach
    void setUp() {
        nationalIdService = NationalIdServiceImpl.getInstance();
    }

    @Nested
    @DisplayName("Get instance tests")
    class GetInstanceTests {

        @Test
        @DisplayName("Non-thread-safe test")
        void testGetInstance() {
            NationalIdService firstNationalIdService = NationalIdServiceImpl.getInstance();
            assertThat(firstNationalIdService).isNotNull();
            NationalIdService secondNationalIdService = NationalIdServiceImpl.getInstance();
            assertThat(secondNationalIdService).isNotNull();
            assertThat(firstNationalIdService).isSameAs(secondNationalIdService);
        }

        @Test
        @DisplayName("Thread-safe test")
        void testGetInstanceThreadSafe() throws InterruptedException {
            NationalIdService[] nationalIdServices = new NationalIdService[2];
            Thread firstThread = new Thread(() -> nationalIdServices[0] = NationalIdServiceImpl.getInstance());
            Thread secondThread = new Thread(() -> nationalIdServices[1] = NationalIdServiceImpl.getInstance());
            firstThread.start();
            secondThread.start();
            firstThread.join();
            secondThread.join();
            assertThat(nationalIdServices[0]).isNotNull();
            assertThat(nationalIdServices[1]).isNotNull();
            assertThat(nationalIdServices[0]).isSameAs(nationalIdServices[1]);
        }

    }

    @Nested
    @DisplayName("Is valid tests")
    class IsValidTests {

        @ParameterizedTest
        @MethodSource("com.persiantools4j.nationalid.NationalIdServiceImplTest#validNationalIdCases")
        @DisplayName("Valid National ID test")
        void testValidNationalId(String nationalId) {
            assertThat(nationalIdService.isValid(nationalId)).isTrue();
        }

        @ParameterizedTest
        @MethodSource({
                "com.persiantools4j.nationalid.NationalIdServiceImplTest#invalidNationalIdFormatCases",
                "com.persiantools4j.nationalid.NationalIdServiceImplTest#invalidNationalIdCases"
        })
        @DisplayName("Invalid National ID test")
        void testInvalidNationalId(String nationalId) {
            assertThat(nationalIdService.isValid(nationalId)).isFalse();
        }

    }

    @Nested
    @DisplayName("Validation tests")
    class ValidationTests {

        @ParameterizedTest
        @MethodSource("com.persiantools4j.nationalid.NationalIdServiceImplTest#validNationalIdCases")
        @DisplayName("National ID validation test")
        void testValidateNationalId(String nationalId) {
            assertThatCode(() -> nationalIdService.validate(nationalId)).doesNotThrowAnyException();
        }

        @ParameterizedTest
        @MethodSource({
                "com.persiantools4j.nationalid.NationalIdServiceImplTest#invalidNationalIdFormatCases",
                "com.persiantools4j.nationalid.NationalIdServiceImplTest#invalidNationalIdCases"
        })
        @DisplayName("Exceptional validation National ID test")
        void testExceptionValidateNationalId(String nationalId) {
            assertThatThrownBy(() -> nationalIdService.validate(nationalId)).isInstanceOf(ValidationException.class);
        }

    }

    @Nested
    @DisplayName("Find hometown")
    class FindHometownTests {

        @Test
        @DisplayName("Get hometown populated list test")
        void testHometownPopulatedList() {
            // Hometown list null check is true
            nationalIdService.getHometownList();
            // Hometown list null check is false now and it's initialized
            List<Hometown> hometownList = nationalIdService.getHometownList();
            Pattern hometownPattern = Pattern.compile("\\d{3}");
            assertThat(hometownList)
                    .isNotNull()
                    .contains(Hometown.of(
                            Arrays.asList("001", "002", "003", "004", "005", "006", "007", "008"),
                            "تهران", "تهران مرکزی")
                    ).allSatisfy(hometown -> {
                        assertThat(hometown.getCode()).isNotEmpty()
                                .allMatch(code -> hometownPattern.matcher(code).matches());
                        assertThat(hometown.getCity()).isNotBlank();
                        assertThat(hometown.getProvince()).isNotBlank();
                    });
        }

        @ParameterizedTest
        @MethodSource("com.persiantools4j.nationalid.NationalIdServiceImplTest#validNationalIdCases")
        @DisplayName("Valid National ID find hometown test")
        void testValidNationalIdFindHometown(String nationalId) {
            Optional<Hometown> hometownOptional = nationalIdService.findHometown(nationalId);
            assertThat(hometownOptional).isPresent();
            hometownOptional.ifPresent(hometown -> {
                assertThat(hometown.getProvince()).isNotBlank();
                assertThat(hometown.getCity()).isNotBlank();
            });
        }

        @Test
        @DisplayName("Single valid National ID find hometown test")
        void testSingleValidNationalIdFindHometown() {
            Hometown expectedHometown = Hometown.of(Arrays.asList("279", "280"), "آذربایجان غربی", "خوی");
            Optional<Hometown> hometownOptional = nationalIdService.findHometown("2791567895");
            assertThat(hometownOptional).isPresent();
            hometownOptional.ifPresent(hometown -> assertThat(hometown).isEqualTo(expectedHometown));
        }

        @ParameterizedTest
        @MethodSource({
                "com.persiantools4j.nationalid.NationalIdServiceImplTest#invalidNationalIdFormatCases",
                "com.persiantools4j.nationalid.NationalIdServiceImplTest#invalidNationalIdCases"
        })
        @DisplayName("Invalid National ID find hometown test")
        void testInvalidNationalIdFindHometown(String nationalId) {
            assertThatThrownBy(() -> nationalIdService.findHometown(nationalId)).isInstanceOf(ValidationException.class);
        }

    }
}
