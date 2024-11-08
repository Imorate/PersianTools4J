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

import com.persiantools4j.Validator;
import com.persiantools4j.bank.Bank;

import java.util.Optional;

/**
 * The {@code CardNumberService} interface defines a contract for services that handle operations
 * related to card numbers and their associated {@link Bank}.
 * <p>
 * The validation process is based on Luhn algorithm
 * <p>
 * This interface extends the {@link Validator} interface, providing validation functionality for card number strings.
 */
public interface CardNumberService extends Validator<String> {

    /**
     * Finds the {@link Bank} associated with the given card number.
     *
     * @param cardNumber the card number for which to find the {@link Bank}
     * @return an {@link Optional} containing the {@link Bank} if found; otherwise, an empty {@link Optional}
     */
    Optional<Bank> findBank(String cardNumber);

}
