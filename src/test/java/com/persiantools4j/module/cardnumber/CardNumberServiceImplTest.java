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

package com.persiantools4j.module.cardnumber;

import com.persiantools4j.exception.ValidationException;
import com.persiantools4j.module.bank.Bank;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Card number service")
class CardNumberServiceImplTest {

    private static CardNumberService cardNumberService;

    private static Stream<Arguments> validCardNumberCases() {
        return Stream.of(
                Arguments.of("6037701689095443"),
                Arguments.of("6219861034529007"),
                Arguments.of("6274129005473742")
        );
    }

    private static Stream<Arguments> invalidCardNumberFormatCases() {
        return Stream.of(
                Arguments.of(""),
                Arguments.of((Object) null),
                Arguments.of("0"),
                Arguments.of("123"),
                Arguments.of("50222"),
                Arguments.of("610433"),
                Arguments.of("50222919"),
                Arguments.of("621986103452900"),
                Arguments.of("1234567890123456 "),
                Arguments.of(" 1234567890123456"),
                Arguments.of(" 1234567890123456 ")
        );
    }

    private static Stream<Arguments> invalidCardNumberCases() {
        return Stream.of(
                Arguments.of("6219861034529008"),
                Arguments.of("9999991034529007"),
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

    @BeforeAll
    static void beforeAll() {
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
        @MethodSource("com.persiantools4j.module.cardnumber.CardNumberServiceImplTest#validCardNumberCases")
        @DisplayName("Valid card number test")
        void testValidCardNumber(String cardNumber) {
            assertThat(cardNumberService.isValid(cardNumber)).isTrue();
        }

        @ParameterizedTest
        @MethodSource({
                "com.persiantools4j.module.cardnumber.CardNumberServiceImplTest#invalidCardNumberFormatCases",
                "com.persiantools4j.module.cardnumber.CardNumberServiceImplTest#invalidCardNumberCases"
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
        @MethodSource("com.persiantools4j.module.cardnumber.CardNumberServiceImplTest#validCardNumberCases")
        @DisplayName("Card number validation test")
        void testValidateCardNumber(String cardNumber) {
            assertThatCode(() -> cardNumberService.validate(cardNumber)).doesNotThrowAnyException();
        }

        @ParameterizedTest
        @MethodSource({
                "com.persiantools4j.module.cardnumber.CardNumberServiceImplTest#invalidCardNumberFormatCases",
                "com.persiantools4j.module.cardnumber.CardNumberServiceImplTest#invalidCardNumberCases"
        })
        @DisplayName("Exceptional format validation card number test")
        void testExceptionFormatValidateCardNumber(String cardNumber) {
            assertThatThrownBy(() -> cardNumberService.validate(cardNumber)).isInstanceOf(ValidationException.class);
        }

    }

    @Nested
    @DisplayName("Find bank")
    class FindBankTests {

        @ParameterizedTest
        @MethodSource("com.persiantools4j.module.cardnumber.CardNumberServiceImplTest#validCardNumberCases")
        @DisplayName("Valid card number find bank test")
        void testValidCardNumberFindBank(String cardNumber) {
            Optional<Bank> bankOptional = cardNumberService.findBank(cardNumber);
            assertThat(bankOptional).isPresent();
            bankOptional.ifPresent(bank -> {
                assertThat(bank.getId()).isNotBlank();
                assertThat(bank.getName()).isNotBlank();
                assertThat(bank.getPersianName()).isNotBlank();
                assertThat(bank.getBins()).isNotEmpty();
                assertThat(bank.getCodes()).isNotEmpty();
            });
        }

        @Test
        @DisplayName("Single valid card number find bank test")
        void testSingleValidCardNumberFindBank() {
            Bank expectedHometown = Bank.of("keshavarzi", "Keshavarzi", "بانک کشاورزی",
                    Collections.singletonList("016"), Arrays.asList(603770, 639217));
            Optional<Bank> bankOptional = cardNumberService.findBank("6037701689095443");
            assertThat(bankOptional).isPresent();
            bankOptional.ifPresent(bank -> assertThat(bank).isEqualTo(expectedHometown));
        }

        @ParameterizedTest
        @MethodSource({
                "com.persiantools4j.module.cardnumber.CardNumberServiceImplTest#invalidCardNumberFormatCases",
                "com.persiantools4j.module.cardnumber.CardNumberServiceImplTest#invalidCardNumberCases"
        })
        @DisplayName("Invalid card number find bank test")
        void testInvalidCardNumberFindBank(String cardNumber) {
            assertThatThrownBy(() -> cardNumberService.findBank(cardNumber)).isInstanceOf(ValidationException.class);
        }

    }

}
