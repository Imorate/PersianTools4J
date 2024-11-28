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

/**
 * Enum representing various character classes for regex patterns.
 * Specifically tailored for Persian and Arabic characters, numerics, symbols, and their combinations.
 */
public enum RegexCharacterClass {

    /**
     * Persian numeric characters (۰-۹).
     */
    PERSIAN_NUMERIC("\\u06F0-\\u06F9"),

    /**
     * Arabic numeric characters (٠-٩).
     */
    ARABIC_NUMERIC("\\u0660-\\u0669"),

    /**
     * Arabic yeh characters.
     */
    ARABIC_YEH("\\u0620\\u063D-\\u063F\\u064A\\u0678\\u06CD-\\u06CE\\u06D1-\\u06D3"),

    /**
     * Arabic waw characters.
     */
    ARABIC_WAW("\\u0676\\u06C4\\u06CA\\u06CF"),

    /**
     * Arabic kaf characters
     */
    ARABIC_KAF("\\u0643\\u06AA-\\u06AC\\u06AE\\u063B-\\u063C"),

    /**
     * Numeric characters including Latin digits (0-9) and Persian digits.
     */
    NUMERIC("\\d" + PERSIAN_NUMERIC.classStr),

    /**
     * Persian alphabet characters.
     */
    PERSIAN_ALPHABET("\\u0621-\\u0628\\u062A-\\u063A\\u0641-\\u0642\\u0644-\\u0649" +
            "\\u06CC\\u06A9\\u06AF\\u0686\\u067E\\u0698"),

    /**
     * Persian and Arabic short vowels.
     */
    PERSIAN_ARABIC_SHORT_VOWEL("\\u064E\\u0650\\u064F"),

    /**
     * Persian and Arabic Tanvin symbols.
     */
    PERSIAN_ARABIC_TANVIN("\\u064B\\u064C\\u064D"),

    /**
     * Various Persian and Arabic symbols, including punctuation and special characters.
     */
    PERSIAN_ARABIC_SYMBOL("\\u200C\\u0640\\u060C\\u00AB\\u00BB\\u061B\\u061F\\u066C" +
            "\\u002C\\u060D\\u066B\\u066A"),

    /**
     * Common symbols used in regex patterns.
     */
    SYMBOL("!@#$%^&*()_\\-=+\\\\/{}\\[\\]\"':;?<>|.~`×÷€£¥"),

    /**
     * A comprehensive character class including Persian and English numerics, alphabets, vowels, symbols,
     * common symbols and whitespace.
     */
    PERSIAN(NUMERIC.classStr + PERSIAN_ALPHABET.classStr + PERSIAN_ARABIC_SHORT_VOWEL.classStr +
            PERSIAN_ARABIC_TANVIN.classStr + PERSIAN_ARABIC_SYMBOL.classStr + SYMBOL.classStr);

    /**
     * The string representation of the regex character class.
     */
    private final String classStr;

    /**
     * Constructs a {@code RegexCharacterClass} with the specified regex string.
     *
     * @param classStr the string representation of the regex character class
     */
    RegexCharacterClass(String classStr) {
        this.classStr = classStr;
    }

    /**
     * Retrieves the regex string for this character class.
     *
     * @return the regex string
     */
    public String getClassStr() {
        return classStr;
    }

}
