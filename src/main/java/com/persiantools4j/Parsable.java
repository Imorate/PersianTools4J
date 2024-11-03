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

package com.persiantools4j;

import com.persiantools4j.exception.ParseException;

/**
 * A generic interface for parsing input data into meaningful structured parts.
 * <p>
 * The {@code Parsable} interface is intended to be implemented by classes
 * that take an input of a specified type and return a parsed output of another
 * specified type.
 *
 * @param <T> the type of input to be parsed
 * @param <R> the type of the result, which represents the structured output after parsing
 */
public interface Parsable<T, R> {

    /**
     * Parses the given input into a structured result.
     * <p>
     * This method takes an input of type {@code T} and processes it to extract
     * meaningful parts, returning them as an instance of type {@code R}. Implementing
     * classes define the parsing logic and the specific parts to be extracted.</p>
     *
     * @param input the input data to be parsed
     * @return an instance of type {@code R} containing the parsed and structured parts
     * of the input
     * @throws ParseException if the input is invalid or cannot be parsed
     */
    R parse(T input);

}
