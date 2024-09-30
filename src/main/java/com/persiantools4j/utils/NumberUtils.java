package com.persiantools4j.utils;

public class NumberUtils {

    public static int getNumericValue(String input, int index) {
        return Character.getNumericValue(input.charAt(index));
    }

}
