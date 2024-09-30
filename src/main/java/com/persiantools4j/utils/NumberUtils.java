package com.persiantools4j.utils;

public class NumberUtils {

    public static int getNumericValue(String input, int index) {
        if (!input.matches("^\\d+$") || index >= input.length()) {
            throw new IllegalArgumentException("Invalid number");
        }
        return Character.getNumericValue(input.charAt(index));
    }

}
