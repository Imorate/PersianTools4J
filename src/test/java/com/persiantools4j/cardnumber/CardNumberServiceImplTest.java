package com.persiantools4j.cardnumber;

import com.persiantools4j.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("Card number service")
class CardNumberServiceImplTest {

    private CardNumberService cardNumberService;

    private static Stream<Arguments> validCardNumberCases() {
        return Stream.of(
                Arguments.of("6274129005473742"),
                Arguments.of("6037701689095443"),
                Arguments.of("6219861034529007")
        );
    }

    private static Stream<Arguments> invalidCardNumberFormatCases() {
        return Stream.of(
                Arguments.of(""),
                Arguments.of((Object) null),
                Arguments.of("123"),
                Arguments.of("1234567890123456 "),
                Arguments.of(" 1234567890123456"),
                Arguments.of(" 1234567890123456 ")
        );
    }

    private static Stream<Arguments> invalidCardNumberCases() {
        return Stream.of(
                Arguments.of("6219861034529008"),
                Arguments.of("621986103452900"),
                Arguments.of("0000000000000000"),
                Arguments.of("1111111111111111"),
                Arguments.of("2222222222222222"),
                Arguments.of("3333333333333333"),
                Arguments.of("4444444444444444"),
                Arguments.of("5555555555555555"),
                Arguments.of("6666666666666666"),
                Arguments.of("7777777777777777"),
                Arguments.of("8888888888888888"),
                Arguments.of("9999999999999999")
        );
    }

    @BeforeEach
    void setUp() {
        cardNumberService = CardNumberServiceImpl.getInstance();
    }

    @Nested
    @DisplayName("Get instance tests")
    class GetInstanceTests {

        @Test
        @DisplayName("Non-thread-safe test")
        void testGetInstance() {
            CardNumberService firstCardNumberService = CardNumberServiceImpl.getInstance();
            assertThat(firstCardNumberService).isNotNull();
            CardNumberService secondCardNumberService = CardNumberServiceImpl.getInstance();
            assertThat(secondCardNumberService).isNotNull();
            assertThat(firstCardNumberService).isSameAs(secondCardNumberService);
        }

        @Test
        @DisplayName("Thread-safe test")
        void testGetInstanceThreadSafe() throws InterruptedException {
            CardNumberService[] cardNumberServices = new CardNumberService[2];
            Thread firstThread = new Thread(() -> cardNumberServices[0] = CardNumberServiceImpl.getInstance());
            Thread secondThread = new Thread(() -> cardNumberServices[1] = CardNumberServiceImpl.getInstance());
            firstThread.start();
            secondThread.start();
            firstThread.join();
            secondThread.join();
            assertThat(cardNumberServices[0]).isNotNull();
            assertThat(cardNumberServices[1]).isNotNull();
            assertThat(cardNumberServices[0]).isSameAs(cardNumberServices[1]);
        }

    }

    @Nested
    @DisplayName("Is valid tests")
    class IsValidTests {

        @ParameterizedTest
        @MethodSource("com.persiantools4j.cardnumber.CardNumberServiceImplTest#validCardNumberCases")
        @DisplayName("Valid card number test")
        void testValidCardNumber(String cardNumber) {
            assertThat(cardNumberService.isValid(cardNumber)).isTrue();
        }

        @ParameterizedTest
        @MethodSource({
                "com.persiantools4j.cardnumber.CardNumberServiceImplTest#invalidCardNumberFormatCases",
                "com.persiantools4j.cardnumber.CardNumberServiceImplTest#invalidCardNumberCases"
        })
        @DisplayName("Invalid card number test")
        void testInvalidCardNumber(String cardNumber) {
            assertThat(cardNumberService.isValid(cardNumber)).isFalse();
        }

    }

    @Nested
    @DisplayName("Validation tests")
    class ValidationTests {

        @ParameterizedTest
        @MethodSource("com.persiantools4j.cardnumber.CardNumberServiceImplTest#validCardNumberCases")
        @DisplayName("Card number validation test")
        void testValidateCardNumber(String cardNumber) {
            assertThatCode(() -> cardNumberService.validate(cardNumber)).doesNotThrowAnyException();
        }

        @ParameterizedTest
        @MethodSource({
                "com.persiantools4j.cardnumber.CardNumberServiceImplTest#invalidCardNumberFormatCases",
                "com.persiantools4j.cardnumber.CardNumberServiceImplTest#invalidCardNumberCases"
        })
        @DisplayName("Exceptional format validation card number test")
        void testExceptionFormatValidateCardNumber(String cardNumber) {
            assertThatThrownBy(() -> cardNumberService.validate(cardNumber)).isInstanceOf(ValidationException.class);
        }

    }

}
