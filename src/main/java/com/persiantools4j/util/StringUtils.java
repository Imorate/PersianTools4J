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

package com.persiantools4j.util;

import com.persiantools4j.enums.RegexCharacterClass;
import com.persiantools4j.exception.ValidationException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@code StringUtils} class provides utility methods for working with String values.
 */
@SuppressWarnings("UnnecessaryUnicodeEscape")
public final class StringUtils {

    public static final String NULL_OR_EMPTY_EXCEPTION_MESSAGE = "Input string is null or empty";
    private static final Pattern ENGLISH_NUMERIC_PATTERN = Pattern.compile("\\d+");
    private static final Pattern PERSIAN_ARABIC_NUMERIC_PATTERN =
            Pattern.compile("[" + RegexCharacterClass.PERSIAN_NUMERIC.getClassStr() +
                    RegexCharacterClass.ARABIC_NUMERIC.getClassStr() + "]");
    private static final Pattern PERSIAN_NUMERIC_PATTERN =
            Pattern.compile("[" + RegexCharacterClass.PERSIAN_NUMERIC.getClassStr() + "]");
    private static final Pattern ARABIC_NUMERIC_PATTERN =
            Pattern.compile("[" + RegexCharacterClass.ARABIC_NUMERIC.getClassStr() + "]");

    /**
     * Private constructor to prevent direct instantiation.
     */
    private StringUtils() {

    }

    /**
     * Checks if the input string is blank (null, empty or only contains whitespace).
     *
     * @param input the string to check
     * @return {@code true} if the input is null, empty, or contains only whitespace, otherwise {@code false}
     */
    public static boolean isBlank(String input) {
        return input == null || input.trim().isEmpty();
    }

    /**
     * Returns the numeric value of a character at a specified index in the input string.
     *
     * @param input the string representing a number. Must only contain digit
     * @param index the index of the character to retrieve
     * @return the numeric value of the character at the specified index
     * @throws ValidationException if the input is not a valid number, input is {@code null}, input is empty or
     *                             the index is out of bounds
     */
    public static int getNumericValue(String input, int index) {
        if (isBlank(input)) {
            throw new ValidationException(NULL_OR_EMPTY_EXCEPTION_MESSAGE);
        }
        input = input.trim();
        if (index >= input.length() || !ENGLISH_NUMERIC_PATTERN.matcher(input).matches()) {
            throw new ValidationException("Invalid number");
        } else {
            return Character.getNumericValue(input.charAt(index));
        }
    }

    /**
     * Converts all Persian (Farsi) and Arabic digits in a given string to their equivalent English (Western) digits.
     * <p>
     * Persian digits (e.g., '۰' (U+06F0) to '۹' (U+06F9)) and Arabic digits (e.g., '۰' (U+0660) to '٩' (U+0669))
     * are mapped to '0' to '9'. Any non-digit characters remain unchanged.
     *
     * @param input the input string containing potential Persian or Arabic digits
     * @return a string with all digits converted to English; non-digits remain unchanged.
     * Returns an empty string if input is blank.
     */
    public static String toEnglishDigits(String input) {
        if (isBlank(input)) {
            return "";
        }
        input = input.trim();
        StringBuffer result = new StringBuffer();
        Matcher matcher = PERSIAN_ARABIC_NUMERIC_PATTERN.matcher(input);
        while (matcher.find()) {
            String digitStr = matcher.group();
            char digit = digitStr.charAt(0);
            char englishDigit;
            if (PERSIAN_NUMERIC_PATTERN.matcher(digitStr).matches()) {
                englishDigit = (char) (digit - '\u06F0' + '0');
            } else {
                englishDigit = (char) (digit - '\u0660' + '0');
            }
            matcher.appendReplacement(result, String.valueOf(englishDigit));
        }
        matcher.appendTail(result);
        return result.toString();
    }

    /**
     * Determines if the input string contains only valid Persian characters, numerics, and symbols.
     * <p>
     * The method trims the input string before validation. Validates against a regex pattern that includes
     * Persian alphabet, numerics, short vowels, Tanvin, symbols, and whitespace.
     *
     * @param input the string to be validated
     * @return {@code true} if the string consists only of valid Persian characters and symbols; {@code false} otherwise
     */
    public static boolean isPersian(String input) {
        if (isBlank(input)) {
            return false;
        }
        input = input.trim();
        String regex = RegexCharacterClass.PERSIAN.getClassStr();
        return input.matches("[" + regex + "\\s]+");
    }

    /**
     * Normalizes the trimmed string by converting Arabic characters and digits to their Persian equivalents.
     * <ul>
     *     <li>Arabic yeh is replaced with Persian 'ی'.</li>
     *     <li>Arabic kaf is replaced with Persian 'ک'.</li>
     *     <li>Arabic digits ('۰' (U+0660) to '٩' (U+0669)) are converted to
     *     Persian digits ('۰' (U+06F0) to '۹' (U+06F9)).</li>
     *     <li>Arabic waw is replaced with Persian 'و'.</li>
     * </ul>
     *
     * @param input the string to normalize
     * @return a normalized string with Persian characters
     */
    public static String normalizePersian(String input) {
        if (isBlank(input)) {
            return "";
        }
        input = input.trim();
        StringBuffer result = new StringBuffer();
        Matcher matcher = ARABIC_NUMERIC_PATTERN.matcher(input);
        while (matcher.find()) {
            char digit = matcher.group().charAt(0);
            char persianDigit = (char) (digit - '\u0660' + '\u06F0');
            matcher.appendReplacement(result, String.valueOf(persianDigit));
        }
        matcher.appendTail(result);
        return result.toString()
                .replaceAll("[" + RegexCharacterClass.ARABIC_YEH.getClassStr() + "]", "ی")
                .replaceAll("[" + RegexCharacterClass.ARABIC_KAF.getClassStr() + "]", "ک")
                .replaceAll("[" + RegexCharacterClass.ARABIC_WAW.getClassStr() + "]", "و");
    }

}
