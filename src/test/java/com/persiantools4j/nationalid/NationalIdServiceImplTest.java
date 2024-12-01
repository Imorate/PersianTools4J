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

import com.persiantools4j.exception.ParseException;
import com.persiantools4j.exception.ValidationException;
import org.assertj.core.api.AssertionsForClassTypes;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;


@DisplayName("National ID service")
class NationalIdServiceImplTest {

    private static Hometown testHometown;
    private static Predicate<Hometown> hometownPredicate;
    private NationalIdService nationalIdService;

    private static Stream<Arguments> validNationalIdCases() {
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
                Arguments.of("0067749828"),
                Arguments.of("0650451252"),
                Arguments.of("4032152314"),
                Arguments.of("3520082780"),
                Arguments.of("0076229645"),
                Arguments.of("4271467685"),
                Arguments.of("0200203241"),
                Arguments.of("1111111111")
        );
    }

    private static Stream<Arguments> validNationalIdWithMultipleHometownCases() {
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

    private static Stream<Arguments> invalidNationalIdFormatCases() {
        return Stream.of(
                Arguments.of(""),
                Arguments.of((Object) null),
                Arguments.of("0"),
                Arguments.of("000000"),
                Arguments.of("0000000000"),
                Arguments.of("4444444444"),
                Arguments.of("9999999999"),
                Arguments.of("12300000"),
                Arguments.of("123000000"),
                Arguments.of("1230000000"),
                Arguments.of("0684159415"),
                Arguments.of(" 6104038932 "),
                Arguments.of("3111001003 "),
                Arguments.of(" 3413857604"),
                Arguments.of("68415941"),
                Arguments.of("787833770"),
                Arguments.of("123abc"),
                Arguments.of("11537027"),
                Arguments.of("068415941"),
                Arguments.of("123"),
                Arguments.of("12345")
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

    private static Stream<Arguments> notFoundHometownCases() {
        return Stream.of(Arguments.of("8908563210"));
    }

    @BeforeAll
    static void beforeAll() {
        testHometown = Hometown.of(Arrays.asList("279", "280"), "آذربایجان غربی", "خوی");
        hometownPredicate = hometown -> !hometown.getProvince().isEmpty() && !hometown.getCity().isEmpty() && !hometown.getCode().isEmpty();
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
        @DisplayName("Valid national ID test")
        void testValidNationalId(String nationalId) {
            assertThat(nationalIdService.isValid(nationalId)).isTrue();
        }

        @ParameterizedTest
        @MethodSource({
                "com.persiantools4j.nationalid.NationalIdServiceImplTest#invalidNationalIdFormatCases",
                "com.persiantools4j.nationalid.NationalIdServiceImplTest#invalidNationalIdCases"
        })
        @DisplayName("Invalid national ID test")
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
        @DisplayName("Exceptional validation national ID test")
        void testExceptionValidateNationalId(String nationalId) {
            assertThatThrownBy(() -> nationalIdService.validate(nationalId)).isInstanceOf(ValidationException.class);
        }

    }

    @Nested
    @DisplayName("Find hometown")
    class FindHometownTests {

        @ParameterizedTest
        @MethodSource("com.persiantools4j.nationalid.NationalIdServiceImplTest#validNationalIdCases")
        @DisplayName("Valid national ID find hometown test")
        void testValidNationalIdFindHometown(String nationalId) {
            List<Hometown> hometownList = nationalIdService.findHometown(nationalId);
            assertThat(hometownList)
                    .isNotEmpty()
                    .allMatch(hometownPredicate);
        }

        @Test
        @DisplayName("Single valid national ID find single hometown test")
        void testSingleValidNationalIdFindHometown() {
            List<Hometown> hometownList = nationalIdService.findHometown("2791567895");
            assertThat(hometownList)
                    .hasSize(1)
                    .containsOnly(testHometown);
        }

        @ParameterizedTest
        @MethodSource("com.persiantools4j.nationalid.NationalIdServiceImplTest#validNationalIdWithMultipleHometownCases")
        @DisplayName("Valid national ID with multiple hometowns")
        void testValidNationalIdWithMultipleHometowns(String nationalId) {
            List<Hometown> hometownList = nationalIdService.findHometown(nationalId);
            assertThat(hometownList)
                    .hasSizeGreaterThanOrEqualTo(2);
        }

        @ParameterizedTest
        @MethodSource({
                "com.persiantools4j.nationalid.NationalIdServiceImplTest#invalidNationalIdFormatCases",
                "com.persiantools4j.nationalid.NationalIdServiceImplTest#invalidNationalIdCases"
        })
        @DisplayName("Invalid national ID find hometown test")
        void testInvalidNationalIdFindHometown(String nationalId) {
            assertThatThrownBy(() -> nationalIdService.findHometown(nationalId)).isInstanceOf(ValidationException.class);
        }

    }

    @Nested
    @DisplayName("Parse national ID")
    class ParseNationalIdTests {

        @ParameterizedTest
        @MethodSource("com.persiantools4j.nationalid.NationalIdServiceImplTest#validNationalIdCases")
        @DisplayName("Valid national ID parse test")
        void testValidNationalIdParse(String nationalId) {
            NationalId actualNationalId = nationalIdService.parse(nationalId);
            assertThat(actualNationalId).isNotNull();
            assertThat(actualNationalId.getId()).isNotBlank();
            assertThat(actualNationalId.getHometownCode()).isNotBlank().hasSize(3);
            assertThat(actualNationalId.getPersonalCode()).isNotBlank().hasSize(6);
            assertThat(actualNationalId.getHometownList())
                    .isNotEmpty()
                    .allMatch(hometownPredicate);
        }

        @Test
        @DisplayName("Single valid national ID parse test")
        void testSingleValidNationalIdParse() {
            NationalId nationalId = nationalIdService.parse("2791567895");
            assertThat(nationalId).isNotNull();
            assertThat(nationalId.getId()).isNotBlank();
            assertThat(nationalId.getHometownCode()).isNotBlank().hasSize(3).isEqualTo("279");
            assertThat(nationalId.getPersonalCode()).isNotBlank().hasSize(6).isEqualTo("156789");
            assertThat(nationalId.getControlDigit()).isNotZero().isEqualTo(5);
            assertThat(nationalId.getHometownList())
                    .hasSize(1)
                    .containsOnly(testHometown);
        }

        @ParameterizedTest
        @MethodSource({
                "com.persiantools4j.nationalid.NationalIdServiceImplTest#invalidNationalIdFormatCases",
                "com.persiantools4j.nationalid.NationalIdServiceImplTest#invalidNationalIdCases"
        })
        @DisplayName("Invalid national ID parse test")
        void testInvalidNationalIdParse(String nationalId) {
            assertThatThrownBy(() -> nationalIdService.parse(nationalId)).isInstanceOf(ValidationException.class);
        }

        @ParameterizedTest
        @MethodSource("com.persiantools4j.nationalid.NationalIdServiceImplTest#notFoundHometownCases")
        @DisplayName("Not found national ID parse test")
        void testNotFoundNationalIdParse(String nationalId) {
            assertThatThrownBy(() -> nationalIdService.parse(nationalId)).isInstanceOf(ParseException.class);
        }

    }

    @Nested
    @DisplayName("Hometown collection")
    class HometownCollectionTests {

        private HometownCollection hometownCollection;

        @BeforeEach
        void setUp() {
            hometownCollection = HometownCollection.getInstance();
        }

        @Test
        @DisplayName("Get populated hometown list test")
        void testHometownPopulatedList() {
            // Hometown list null check is true
            hometownCollection.getCollection();
            // Hometown list null check is false now and it's initialized
            List<Hometown> hometownList = hometownCollection.getCollection();
            Pattern hometownCodePattern = Pattern.compile("\\d{3}");
            AssertionsForInterfaceTypes.assertThat(hometownList)
                    .isNotNull()
                    .contains(Hometown.of(
                            Arrays.asList("001", "002", "003", "004", "005", "006", "007", "008"),
                            "تهران", "تهران مرکزی")
                    ).allSatisfy(hometown -> {
                        AssertionsForInterfaceTypes.assertThat(hometown.getCode())
                                .isNotEmpty()
                                .allMatch(code -> hometownCodePattern.matcher(code).matches());
                        AssertionsForClassTypes.assertThat(hometown.getCity()).isNotBlank();
                        AssertionsForClassTypes.assertThat(hometown.getProvince()).isNotBlank();
                    });
        }

        @Nested
        @DisplayName("Get instance tests")
        class GetInstanceTests {

            @Test
            @DisplayName("Non-thread-safe test")
            void testGetInstance() {
                HometownCollection firstHometownCollection = HometownCollection.getInstance();
                assertThat(firstHometownCollection).isNotNull();
                HometownCollection secondHometownCollection = HometownCollection.getInstance();
                assertThat(secondHometownCollection).isNotNull();
                assertThat(firstHometownCollection).isSameAs(secondHometownCollection);
            }

            @Test
            @DisplayName("Thread-safe test")
            void testGetInstanceThreadSafe() throws InterruptedException {
                HometownCollection[] hometownCollections = new HometownCollection[2];
                Thread firstThread = new Thread(() -> hometownCollections[0] = HometownCollection.getInstance());
                Thread secondThread = new Thread(() -> hometownCollections[1] = HometownCollection.getInstance());
                firstThread.start();
                secondThread.start();
                firstThread.join();
                secondThread.join();
                assertThat(hometownCollections[0]).isNotNull();
                assertThat(hometownCollections[1]).isNotNull();
                assertThat(hometownCollections[0]).isSameAs(hometownCollections[1]);
            }

        }

    }

}
