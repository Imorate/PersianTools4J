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

package com.persiantools4j;

import com.persiantools4j.exception.ValidationException;

/**
 * The {@code Validatable} interface defines a contract for validating, normalizing, and ensuring the conformity
 * of objects of type {@code T}. Implementations of this interface should provide mechanisms to check the validity
 * of an object, normalize it to a standard format, and handle any validation exceptions.
 *
 * @param <T> the type of object that this {@code Validatable} can validate and normalize
 */
public interface Validatable<T> {

    /**
     * Checks if the specified object is valid.
     *
     * @param t the object to validate
     * @return {@code true} if the object is valid, {@code false} otherwise
     */
    boolean isValid(T t);

    /**
     * Validates the specified object.
     * <p>
     * If the object does not meet validation criteria, a {@link ValidationException} is thrown.
     *
     * @param t the object to validate
     * @throws ValidationException if the object is not valid
     */
    void validate(T t) throws ValidationException;

    /**
     * Normalizes the object to ensure it conforms to a standard format or structure.
     *
     * @param t the object to normalize
     * @return a normalized instance of type {@code T}
     */
    T normalize(T t);

}
