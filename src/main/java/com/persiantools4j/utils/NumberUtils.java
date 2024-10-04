package com.persiantools4j.utils;

/**
 * The NumberUtils class provides utility methods for working with numeric strings.
 * <p>
 * This class contains static methods that can be used to perform operations on
 * strings representing numbers.
 */
public class NumberUtils {

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

}
