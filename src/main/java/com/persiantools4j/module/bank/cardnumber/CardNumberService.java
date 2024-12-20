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

import com.persiantools4j.Validatable;
import com.persiantools4j.collection.bank.Bank;
import com.persiantools4j.collection.bank.BankCollection;
import com.persiantools4j.exception.ValidationException;
import com.persiantools4j.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * The {@code CardNumberService} class implements the {@link Validatable} interface, providing functionality
 * for validating card numbers and determining the associated {@link Bank}.
 *
 * @see Validatable
 */
public final class CardNumberService implements Validatable<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CardNumberService.class);
    private static final Pattern CARD_NUMBER_PATTERN = Pattern.compile("\\d{16}");
    private static final Pattern CARD_NUMBER_REPEATED_DIGITS_PATTERN = Pattern.compile("(\\d)\\1{15}");

    /**
     * Validates the format of the provided card number.
     *
     * @param cardNumber the card number to validate
     * @throws ValidationException if the card number is null or in an invalid format
     */
    private void validateFormat(String cardNumber) throws ValidationException {
        if (StringUtils.isBlank(cardNumber)) {
            throw new ValidationException("Card number is null or empty");
        }
        if (!CARD_NUMBER_PATTERN.matcher(cardNumber).matches() ||
                CARD_NUMBER_REPEATED_DIGITS_PATTERN.matcher(cardNumber).matches()) {
            throw new ValidationException("Invalid card number format: " + cardNumber);
        }
    }

    @Override
    public boolean isValid(String cardNumber) {
        try {
            validate(cardNumber);
        } catch (ValidationException e) {
            LOGGER.warn(e.getMessage(), e);
            return false;
        }
        return true;
    }

    @Override
    public void validate(String cardNumber) throws ValidationException {
        validateFormat(cardNumber);
        int sum = IntStream.range(0, 16)
                .boxed()
                .reduce(0, (Integer partialResult, Integer index) -> {
                    int digit = StringUtils.getNumericValue(cardNumber, index);
                    if ((index + 1) % 2 != 0) {
                        int doubledDigit = digit * 2;
                        if (doubledDigit > 9) {
                            return partialResult + (doubledDigit - 9);
                        } else {
                            return partialResult + doubledDigit;
                        }
                    } else {
                        return partialResult + digit;
                    }
                }, Integer::sum);
        if (sum % 10 != 0) {
            throw new ValidationException("Invalid card number: " + cardNumber);
        }
    }

    @Override
    public String normalize(String input) {
        input = input.trim();
        return input;
    }

    /**
     * Finds the {@link Bank} associated with the given card number.
     *
     * @param cardNumber the card number for which to find the {@link Bank}
     * @return an {@link Optional} containing the {@link Bank} if found; otherwise, an empty {@link Optional}
     */
    public Optional<Bank> findBank(String cardNumber) {
        validate(cardNumber);
        String firstSixDigits = cardNumber.substring(0, 6);
        return BankCollection.getInstance()
                .findBy(bank -> bank.getBins().contains(firstSixDigits));
    }

}
