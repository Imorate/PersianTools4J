package com.persiantools4j.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The NumberUtils class provides utility methods for working with numeric strings.
 * <p>
 * This class contains static methods that can be used to perform operations on
 * strings representing numbers.
 */
public class NumberUtils {

    /**
     * Private constructor to prevent direct instantiation.
     */
    private NumberUtils() {

    }

    /**
     * Returns the numeric value of a character at a specified index in the input string.
     *
     * @param input The string representing a number
     * @param index The index of the character to retrieve
     * @return The numeric value of the character at the specified index
     * @throws IllegalArgumentException If the input is not a valid number
     *                                  or if the index is out of bounds
     */
    public static int getNumericValue(String input, int index) {
        if (!input.matches("^\\d+$") || index >= input.length()) {
            throw new IllegalArgumentException("Invalid number");
        }
        return Character.getNumericValue(input.charAt(index));
    }

    /**
     * Converts all Persian (Farsi) digits in a given string to their equivalent English (Western) digits.
     * <p>
     * Persian digits range from '۰' (U+06F0) to '۹' (U+06F9), and are replaced with their corresponding
     * English digits ('0' to '9'). Any non-digit characters are left unchanged.
     * </p>
     *
     * @param persianStr The input string potentially containing Persian digits.
     * @return A new string where all Persian digits are replaced with English digits, while other characters are unchanged.
     * @throws IllegalArgumentException If the input string is null or empty.
     */
    public static String convertPersianToEnglishDigits(String persianStr) {
        if (persianStr == null || persianStr.isEmpty()) {
            throw new IllegalArgumentException("Input string is empty");
        }
        persianStr = persianStr.trim();
        Matcher matcher = Pattern.compile("[۰-۹]")
                .matcher(persianStr);
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            char persianDigit = matcher.group().charAt(0);
            char englishDigit = (char) (persianDigit - '۰' + '0');
            matcher.appendReplacement(result, Character.toString(englishDigit));
        }
        matcher.appendTail(result);
        return result.toString();
    }

}
