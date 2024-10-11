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

package com.persiantools4j.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@code StringUtils} class provides utility methods for working with String values.
 */
public final class StringUtils {

    private static final Pattern NUMERIC_PATTERN = Pattern.compile("\\d+");
    private static final Pattern PERSIAN_NUMERIC_CHARACTER_CLASS_PATTERN = Pattern.compile("[۰-۹]");

    /**
     * Private constructor to prevent direct instantiation.
     */
    private StringUtils() {

    }

    /**
     * Returns the numeric value of a character at a specified index in the input string.
     *
     * @param input the string representing a number. Must only contain digit.
     * @param index the index of the character to retrieve
     * @return the numeric value of the character at the specified index
     * @throws IllegalArgumentException if the input is not a valid number
     *                                  or if the index is out of bounds
     */
    public static int getNumericValue(String input, int index) {
        if (!NUMERIC_PATTERN.matcher(input).matches() || index >= input.length()) {
            throw new IllegalArgumentException("Invalid number");
        }
        return Character.getNumericValue(input.charAt(index));
    }

    /**
     * Converts all Persian (Farsi) digits in a given string to their equivalent English (Western) digits.
     * <p>
     * Persian digits range from '۰' (U+06F0) to '۹' (U+06F9), and are replaced with their corresponding
     * English digits ('0' to '9'). Any non-digit characters are left unchanged.
     *
     * @param persianStr the input string potentially containing Persian digits.
     * @return a new string where all Persian digits are replaced with English digits,
     * while other characters are unchanged.
     * @throws IllegalArgumentException if the input string is null or empty.
     */
    public static String convertPersianToEnglishDigits(String persianStr) {
        if (persianStr == null || persianStr.isEmpty()) {
            throw new IllegalArgumentException("Input string is empty");
        }
        persianStr = persianStr.trim();
        StringBuffer result = new StringBuffer();
        Matcher matcher = PERSIAN_NUMERIC_CHARACTER_CLASS_PATTERN.matcher(persianStr);
        while (matcher.find()) {
            char persianDigit = matcher.group().charAt(0);
            char englishDigit = (char) (persianDigit - '۰' + '0');
            matcher.appendReplacement(result, Character.toString(englishDigit));
        }
        matcher.appendTail(result);
        return result.toString();
    }

}
