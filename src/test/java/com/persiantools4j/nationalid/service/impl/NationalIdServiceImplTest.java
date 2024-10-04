package com.persiantools4j.nationalid.service.impl;

import com.persiantools4j.exception.ValidationException;
import com.persiantools4j.nationalid.model.Location;
import com.persiantools4j.nationalid.service.NationalIdService;
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
        @MethodSource("com.persiantools4j.nationalid.service.impl.NationalIdServiceImplTest#validNationalIdCases")
        @DisplayName("Valid National ID test")
        void testValidNationalId(String nationalId) {
            assertThat(nationalIdService.isValid(nationalId)).isTrue();
        }

        @ParameterizedTest
        @MethodSource({
                "com.persiantools4j.nationalid.service.impl.NationalIdServiceImplTest#invalidNationalIdCases",
                "com.persiantools4j.nationalid.service.impl.NationalIdServiceImplTest#invalidNationalIdFormatCases"
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
        @MethodSource("com.persiantools4j.nationalid.service.impl.NationalIdServiceImplTest#validNationalIdCases")
        @DisplayName("National ID validation test")
        void testValidateNationalId(String nationalId) {
            assertThatCode(() -> nationalIdService.validate(nationalId)).doesNotThrowAnyException();
        }

        @ParameterizedTest
        @MethodSource("com.persiantools4j.nationalid.service.impl.NationalIdServiceImplTest#invalidNationalIdFormatCases")
        @DisplayName("Exceptional format validation National ID test")
        void testExceptionFormatValidateNationalId(String nationalId) {
            assertThatThrownBy(() -> nationalIdService.validate(nationalId)).isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @MethodSource("com.persiantools4j.nationalid.service.impl.NationalIdServiceImplTest#invalidNationalIdCases")
        @DisplayName("Exceptional validation National ID test")
        void testExceptionValidateNationalId(String nationalId) {
            assertThatThrownBy(() -> nationalIdService.validate(nationalId)).isInstanceOf(ValidationException.class);
        }

    }

    @Nested
    @DisplayName("Find location")
    class FindLocationTests {

        private Location mockedLocation;

        @BeforeEach
        void setUp() {
            mockedLocation = Location.of("Test province", "Test city");
        }

        @Test
        @DisplayName("Get location populated map test")
        void testLocationPopulatedMap() {
            // Location map null check is true
            nationalIdService.getLocationMap();
            // Location map null check is false now and it's initialized
            Map<String, Location> locationMap = nationalIdService.getLocationMap();
            assertThat(locationMap)
                    .isNotNull()
                    .asInstanceOf(InstanceOfAssertFactories.map(String.class, Location.class))
                    .allSatisfy((code, location) -> {
                        assertThat(code).containsPattern("\\d{3}");
                        assertThat(location)
                                .isNotNull()
                                .matches(l -> !l.getProvince().isEmpty() && !l.getCity().isEmpty());
                    })
                    .containsEntry("001", Location.of("تهران", "تهران مرکزی"));
        }

        @ParameterizedTest
        @MethodSource("com.persiantools4j.nationalid.service.impl.NationalIdServiceImplTest#validNationalIdCases")
        @DisplayName("Valid National ID find location test")
        void testValidNationalIdFindLocation(String nationalId) {
            Optional<Location> locationOptional = nationalIdService.findLocation(nationalId);
            assertThat(locationOptional.isPresent()).isTrue();
            locationOptional.ifPresent(location -> {
                assertThat(location.getProvince()).isNotBlank();
                assertThat(location.getCity()).isNotBlank();
            });
        }

        @Test
        @DisplayName("Single valid National ID find location test")
        void testSingleValidNationalIdFindLocation() {
            Location expectedLocation = Location.of("آذربایجان غربی", "خوی");
            Optional<Location> locationOptional = nationalIdService.findLocation("2791567895");
            assertThat(locationOptional.isPresent()).isTrue();
            locationOptional.ifPresent(location -> assertThat(locationOptional.get()).isEqualTo(expectedLocation));
        }

        @ParameterizedTest
        @MethodSource("com.persiantools4j.nationalid.service.impl.NationalIdServiceImplTest#invalidNationalIdCases")
        @DisplayName("Invalid National ID find location test")
        void testInvalidNationalIdFindLocation(String nationalId) {
            assertThatThrownBy(() -> nationalIdService.findLocation(nationalId)).isInstanceOf(ValidationException.class);
        }

        @ParameterizedTest
        @MethodSource("com.persiantools4j.nationalid.service.impl.NationalIdServiceImplTest#invalidNationalIdFormatCases")
        @DisplayName("Invalid National ID format find location test")
        void testInvalidNationalIdFormatFindLocation(String nationalId) {
            assertThatThrownBy(() -> nationalIdService.findLocation(nationalId)).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Location methods test")
        @SuppressWarnings({"EqualsBetweenInconvertibleTypes", "EqualsWithItself"})
        void testLocationMethods() {
            assertThat(mockedLocation.hashCode()).isNotZero();
            assertThat(mockedLocation.toString()).contains(mockedLocation.getProvince(), mockedLocation.getCity());
            assertThat(mockedLocation.equals(mockedLocation)).isTrue();
            assertThat(mockedLocation.equals("")).isFalse();
            assertThat(mockedLocation.equals(Location.of("123", "231"))).isFalse();
        }

    }
}
