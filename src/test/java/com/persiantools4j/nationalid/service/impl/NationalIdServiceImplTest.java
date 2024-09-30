package com.persiantools4j.nationalid.service.impl;

import com.persiantools4j.exception.ValidationException;
import com.persiantools4j.nationalid.service.NationalIdService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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
                Arguments.of("9731001018"),
                Arguments.of("3520082780")
        );
    }

    private static Stream<Arguments> invalidNationalIdCases() {
        return Stream.of(
                Arguments.of(""),
                Arguments.of((Object) null),
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

    @Test
    @DisplayName("Get instance test")
    void testGetInstance() {
        NationalIdService firstNationalIdService = NationalIdServiceImpl.getInstance();
        assertThat(firstNationalIdService).isNotNull();
        NationalIdService secondNationalIdService = NationalIdServiceImpl.getInstance();
        assertThat(secondNationalIdService).isNotNull();
        assertThat(firstNationalIdService).isSameAs(secondNationalIdService);
    }

    @Test
    @DisplayName("Get instance thread-safe test")
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

    @ParameterizedTest
    @MethodSource("validNationalIdCases")
    @DisplayName("Valid National ID test")
    void testValidNationalId(String nationalId) {
        assertThat(nationalIdService.isValid(nationalId)).isTrue();
    }

    @ParameterizedTest
    @MethodSource("invalidNationalIdCases")
    @DisplayName("Invalid National ID test")
    void testInvalidNationalId(String nationalId) {
        assertThat(nationalIdService.isValid(nationalId)).isFalse();
    }

    @ParameterizedTest
    @MethodSource("validNationalIdCases")
    @DisplayName("National ID validation test")
    void testValidateNationalId(String nationalId) {
        assertThatCode(() -> nationalIdService.validate(nationalId)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource("invalidNationalIdCases")
    @DisplayName("Exceptional validation National ID test")
    void testExceptionValidateNationalId(String nationalId) {
        assertThatThrownBy(() -> nationalIdService.validate(nationalId)).isInstanceOf(ValidationException.class);
    }
}
