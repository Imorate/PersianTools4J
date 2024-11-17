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

import com.persiantools4j.exception.ValidationException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@code StringUtils} class provides utility methods for working with String values.
 */
@SuppressWarnings("UnnecessaryUnicodeEscape")
public final class StringUtils {

    public static final Pattern NUMERIC_PATTERN = Pattern.compile("\\d+");
    public static final Pattern PERSIAN_ARABIC_NUMERIC_CHARACTER_CLASS_PATTERN =
            Pattern.compile(String.format("[%s]", RegexCharacterClass.PERSIAN_ARABIC_NUMERIC.getClassStr()));
    public static final Pattern PERSIAN_NUMERIC_CHARACTER_CLASS_PATTERN =
            Pattern.compile(String.format("[%s]", RegexCharacterClass.PERSIAN_NUMERIC.getClassStr()));
    public static final String NULL_OR_EMPTY_EXCEPTION_MESSAGE = "Input string is null or empty";

    /**
     * Private constructor to prevent direct instantiation.
     */
    private StringUtils() {

    }

    /**
     * Returns the numeric value of a character at a specified index in the input string.
     *
     * @param input the string representing a number. Must only contain digit
     * @param index the index of the character to retrieve
     * @return the numeric value of the character at the specified index
     * @throws ValidationException if the input is not a valid number or if the index is out of bounds
     */
    public static int getNumericValue(String input, int index) {
        if (!NUMERIC_PATTERN.matcher(input).matches() || index >= input.length()) {
            throw new ValidationException("Invalid number");
        }
        return Character.getNumericValue(input.charAt(index));
    }

    /**
     * Converts all Persian (Farsi) and Arabic digits in a given string to their equivalent English (Western) digits.
     * <p>
     * Persian digits range from '۰' (U+06F0) to '۹' (U+06F9) and Arabic digits range from '۰' (U+0660) to '٩' (U+0669)
     * are replaced with their corresponding English digits ('0' to '9'). Any non-digit characters are left unchanged.
     *
     * @param input the input string potentially containing Persian and Arabic digits
     * @return a new string where all Persian and Arabic digits are replaced with English digits,
     * while other characters are unchanged
     * @throws ValidationException if the input string is null or empty
     */
    public static String toEnglishDigits(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new ValidationException(NULL_OR_EMPTY_EXCEPTION_MESSAGE);
        }
        input = input.trim();
        StringBuffer result = new StringBuffer();
        Matcher matcher = PERSIAN_ARABIC_NUMERIC_CHARACTER_CLASS_PATTERN.matcher(input);
        while (matcher.find()) {
            String digitStr = matcher.group();
            char digit = digitStr.charAt(0);
            char englishDigit;
            if (PERSIAN_NUMERIC_CHARACTER_CLASS_PATTERN.matcher(digitStr).matches()) {
                englishDigit = (char) (digit - '۰' + '0');
            } else {
                englishDigit = (char) (digit - '٠' + '0');
            }
            matcher.appendReplacement(result, Character.toString(englishDigit));
        }
        matcher.appendTail(result);
        return result.toString();
    }

    /**
     * Determines if the input string consists only of Persian characters, numerics, and symbols.
     * <p>
     * The method trims the input string and validates it against a regex pattern that matches Persian
     * numerics, alphabet, short vowels, Tanvin, and symbols, including whitespace.
     *
     * @param input the string to check
     * @return {@code true} if the input string consists only of Persian characters
     * and valid symbols; {@code false} otherwise
     * @throws ValidationException if the input is {@code null} or empty
     */
    public static boolean isPersian(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new ValidationException(NULL_OR_EMPTY_EXCEPTION_MESSAGE);
        }
        input = input.trim();
        String regex = RegexCharacterClass.PERSIAN.getClassStr();
        return input.matches(String.format("[%s\\s]+", regex));
    }

    /**
     * Normalizes the input string by replacing Arabic characters with their Persian equivalents.
     * <p>
     * Specifically, it replaces Arabic 'ي' with Persian 'ی' and Arabic 'ك' with Persian 'ک'.
     * The input string is trimmed before processing.
     *
     * @param input the string to normalize
     * @return a normalized string with Persian characters
     * @throws ValidationException if the input is {@code null} or empty
     */
    public static String normalizePersian(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new ValidationException(NULL_OR_EMPTY_EXCEPTION_MESSAGE);
        }
        input = input.trim();
        return input
                .replace('ي', 'ی')
                .replace('ك', 'ک');
    }

}
