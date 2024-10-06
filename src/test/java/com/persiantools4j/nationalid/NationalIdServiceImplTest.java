package com.persiantools4j.nationalid;

import com.persiantools4j.exception.ValidationException;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.Optional;
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
                Arguments.of(" 3413857604")
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
            final NationalIdService[] nationalIdServices = new NationalIdService[2];
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
                "com.persiantools4j.nationalid.NationalIdServiceImplTest#invalidNationalIdCases",
                "com.persiantools4j.nationalid.NationalIdServiceImplTest#invalidNationalIdFormatCases"
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
        @MethodSource("com.persiantools4j.nationalid.NationalIdServiceImplTest#invalidNationalIdFormatCases")
        @DisplayName("Exceptional format validation National ID test")
        void testExceptionFormatValidateNationalId(String nationalId) {
            assertThatThrownBy(() -> nationalIdService.validate(nationalId)).isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @MethodSource("com.persiantools4j.nationalid.NationalIdServiceImplTest#invalidNationalIdCases")
        @DisplayName("Exceptional validation National ID test")
        void testExceptionValidateNationalId(String nationalId) {
            assertThatThrownBy(() -> nationalIdService.validate(nationalId)).isInstanceOf(ValidationException.class);
        }

    }

    @Nested
    @DisplayName("Find hometown")
    class FindHometownTests {

        @Test
        @DisplayName("Get hometown populated map test")
        void testHometownPopulatedMap() {
            // Hometown map null check is true
            nationalIdService.getHometownMap();
            // Hometown map null check is false now and it's initialized
            Map<String, Hometown> hometownMap = nationalIdService.getHometownMap();
            assertThat(hometownMap)
                    .isNotNull()
                    .asInstanceOf(InstanceOfAssertFactories.map(String.class, Hometown.class))
                    .allSatisfy((code, hometown) -> {
                        assertThat(code).containsPattern("\\d{3}");
                        assertThat(hometown)
                                .isNotNull()
                                .matches(l -> !l.getProvince().isEmpty() && !l.getCity().isEmpty());
                    })
                    .containsEntry("001", Hometown.of("تهران", "تهران مرکزی"));
        }

        @ParameterizedTest
        @MethodSource("com.persiantools4j.nationalid.NationalIdServiceImplTest#validNationalIdCases")
        @DisplayName("Valid National ID find hometown test")
        void testValidNationalIdFindHometown(String nationalId) {
            Optional<Hometown> hometownOptional = nationalIdService.findHometown(nationalId);
            assertThat(hometownOptional.isPresent()).isTrue();
            hometownOptional.ifPresent(hometown -> {
                assertThat(hometown.getProvince()).isNotBlank();
                assertThat(hometown.getCity()).isNotBlank();
            });
        }

        @Test
        @DisplayName("Single valid National ID find hometown test")
        void testSingleValidNationalIdFindHometown() {
            Hometown expectedHometown = Hometown.of("آذربایجان غربی", "خوی");
            Optional<Hometown> hometownOptional = nationalIdService.findHometown("2791567895");
            assertThat(hometownOptional.isPresent()).isTrue();
            hometownOptional.ifPresent(hometown -> assertThat(hometownOptional.get()).isEqualTo(expectedHometown));
        }

        @ParameterizedTest
        @MethodSource("com.persiantools4j.nationalid.NationalIdServiceImplTest#invalidNationalIdCases")
        @DisplayName("Invalid National ID find hometown test")
        void testInvalidNationalIdFindHometown(String nationalId) {
            assertThatThrownBy(() -> nationalIdService.findHometown(nationalId)).isInstanceOf(ValidationException.class);
        }

        @ParameterizedTest
        @MethodSource("com.persiantools4j.nationalid.NationalIdServiceImplTest#invalidNationalIdFormatCases")
        @DisplayName("Invalid National ID format find hometown test")
        void testInvalidNationalIdFormatFindHometown(String nationalId) {
            assertThatThrownBy(() -> nationalIdService.findHometown(nationalId)).isInstanceOf(IllegalArgumentException.class);
        }

    }
}
