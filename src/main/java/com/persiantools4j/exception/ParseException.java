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

package com.persiantools4j.exception;

/**
 * The {@code ParseException} thrown when an error occurs during the parsing of an input.
 * <p>
 * This exception indicates that a parsing operation failed, usually because the input was invalid, malformed or
 * did not meet the required format. The exception may provide details on the reason for failure or which part of the
 * input caused the issue.
 */
public class ParseException extends IllegalArgumentException {

    /**
     * Constructs a new {@code ParseException} with the specified detail message.
     *
     * @param s the detail message providing more context about the parsing failure
     */
    public ParseException(String s) {
        super(s);
    }

}
