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

package com.persiantools4j.module.bank.cardnumber;

import com.persiantools4j.collection.bank.Bank;
import com.persiantools4j.exception.ValidationException;
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
class CardNumberServiceTest {

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
        cardNumberService = new CardNumberService();
    }

    @Nested
    @DisplayName("Is valid")
    class IsValidTest {

        @ParameterizedTest
        @DisplayName("Valid card number")
        @MethodSource("com.persiantools4j.module.bank.cardnumber.CardNumberServiceTest#validCardNumberCases")
        void testValidCardNumber(String cardNumber) {
            assertThat(cardNumberService.isValid(cardNumber)).isTrue();
        }

        @ParameterizedTest
        @DisplayName("Invalid card number")
        @MethodSource({
                "com.persiantools4j.module.bank.cardnumber.CardNumberServiceTest#invalidCardNumberFormatCases",
                "com.persiantools4j.module.bank.cardnumber.CardNumberServiceTest#invalidCardNumberCases"
        })
        void testInvalidCardNumber(String cardNumber) {
            assertThat(cardNumberService.isValid(cardNumber)).isFalse();
        }

    }

    @Nested
    @DisplayName("Validation")
    class ValidationTest {

        @ParameterizedTest
        @DisplayName("Card number validation")
        @MethodSource("com.persiantools4j.module.bank.cardnumber.CardNumberServiceTest#validCardNumberCases")
        void testValidateCardNumber(String cardNumber) {
            assertThatCode(() -> cardNumberService.validate(cardNumber)).doesNotThrowAnyException();
        }

        @ParameterizedTest
        @DisplayName("Exceptional format validation card number")
        @MethodSource({
                "com.persiantools4j.module.bank.cardnumber.CardNumberServiceTest#invalidCardNumberFormatCases",
                "com.persiantools4j.module.bank.cardnumber.CardNumberServiceTest#invalidCardNumberCases"
        })
        void testExceptionFormatValidateCardNumber(String cardNumber) {
            assertThatThrownBy(() -> cardNumberService.validate(cardNumber)).isInstanceOf(ValidationException.class);
        }

    }

    @Nested
    @DisplayName("Find bank")
    class FindBankTest {

        @ParameterizedTest
        @DisplayName("Valid card number find bank")
        @MethodSource("com.persiantools4j.module.bank.cardnumber.CardNumberServiceTest#validCardNumberCases")
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
        @DisplayName("Single valid card number find bank")
        void testSingleValidCardNumberFindBank() {
            Bank expectedHometown = new Bank("keshavarzi", "Keshavarzi", "بانک کشاورزی",
                    Collections.singletonList("016"), Arrays.asList("603770", "639217"));
            Optional<Bank> bankOptional = cardNumberService.findBank("6037701689095443");
            assertThat(bankOptional).isPresent();
            bankOptional.ifPresent(bank -> assertThat(bank).isEqualTo(expectedHometown));
        }

        @ParameterizedTest
        @DisplayName("Invalid card number find bank")
        @MethodSource({
                "com.persiantools4j.module.bank.cardnumber.CardNumberServiceTest#invalidCardNumberFormatCases",
                "com.persiantools4j.module.bank.cardnumber.CardNumberServiceTest#invalidCardNumberCases"
        })
        void testInvalidCardNumberFindBank(String cardNumber) {
            assertThatThrownBy(() -> cardNumberService.findBank(cardNumber)).isInstanceOf(ValidationException.class);
        }

    }

}
