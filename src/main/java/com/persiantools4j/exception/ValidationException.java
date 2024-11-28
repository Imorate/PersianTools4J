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

package com.persiantools4j.exception;

/**
 * The {@code ValidationException} class represents an exception that is thrown when validation of an object fails.
 * <p>
 * This exception extends {@link IllegalArgumentException}, indicating that the input or argument provided
 * does not meet the required validation criteria.
 */
public final class ValidationException extends IllegalArgumentException {

    /**
     * Constructs a new {@code ValidationException} with the specified detail message.
     * <p>
     * The detail message provides more information about why the validation failed.
     *
     * @param s the detail message explaining the reason for the validation failure
     */
    public ValidationException(String s) {
        super(s);
    }

}
