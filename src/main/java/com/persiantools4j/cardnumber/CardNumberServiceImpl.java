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

package com.persiantools4j.cardnumber;

import com.persiantools4j.Generated;
import com.persiantools4j.exception.ValidationException;
import com.persiantools4j.utils.StringUtils;

import java.util.stream.IntStream;

public class CardNumberServiceImpl implements CardNumberService {

    private static volatile CardNumberServiceImpl instance;

    private CardNumberServiceImpl() {
    }

    @Generated
    public static CardNumberServiceImpl getInstance() {
        if (instance == null) {
            synchronized (CardNumberServiceImpl.class) {
                if (instance == null) {
                    instance = new CardNumberServiceImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public boolean isValid(String cardNumber) {
        try {
            validate(cardNumber);
        } catch (ValidationException e) {
            return false;
        }
        return true;
    }

    @Override
    public void validate(String cardNumber) throws ValidationException {
        validateCardNumberFormat(cardNumber);
        int sum = IntStream.range(0, 16)
                .boxed()
                .reduce(0, (partialResult, index) -> {
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

    private void validateCardNumberFormat(String cardNumber) throws ValidationException {
        if (cardNumber == null) {
            throw new ValidationException("Card number is null");
        }
        if (!cardNumber.matches("\\d{16}") || cardNumber.matches("(\\d)\\1{15}")) {
            throw new ValidationException("Invalid card number format: " + cardNumber);
        }
    }

}
