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

package com.persiantools4j.cardnumber;

import com.persiantools4j.bank.Bank;
import com.persiantools4j.bank.BankCollection;
import com.persiantools4j.exception.ValidationException;
import com.persiantools4j.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * The {@code CardNumberServiceImpl} class implements the {@link CardNumberService} interface
 * and provides functionality for validating card numbers and finding associated {@link Bank}.
 * <p>
 * This class follows the Singleton design pattern to ensure that a single instance is used throughout the application.
 */
public final class CardNumberServiceImpl implements CardNumberService {

    private static final Pattern CARD_NUMBER_PATTERN = Pattern.compile("\\d{16}");
    private static final Pattern CARD_NUMBER_REPEATED_DIGITS_PATTERN = Pattern.compile("(\\d)\\1{15}");
    private static final Logger LOGGER = LoggerFactory.getLogger(CardNumberServiceImpl.class);

    /**
     * Private constructor to prevent direct instantiation.
     */
    private CardNumberServiceImpl() {

    }

    /**
     * Returns the singleton instance of {@code CardNumberServiceImpl}.
     *
     * @return the single instance of {@code CardNumberServiceImpl}
     */
    public static CardNumberServiceImpl getInstance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * Validates the format of the provided card number.
     *
     * @param cardNumber the card number to validate
     * @throws ValidationException if the card number is null or in an invalid format
     */
    private static void validateFormat(String cardNumber) throws ValidationException {
        if (cardNumber == null) {
            throw new ValidationException("Card number is null");
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
    public Optional<Bank> findBank(String cardNumber) {
        validate(cardNumber);
        int firstSixDigits = Integer.parseInt(cardNumber.substring(0, 6));
        return BankCollection.getInstance()
                .getCollection().stream()
                .filter(bank -> bank.getBins().contains(firstSixDigits))
                .findFirst();
    }

    /**
     * Private static helper class to implement the Singleton design pattern.
     */
    private static class InstanceHolder {
        private static final CardNumberServiceImpl INSTANCE = new CardNumberServiceImpl();
    }

}
