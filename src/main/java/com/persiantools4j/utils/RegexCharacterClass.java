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

@SuppressWarnings("UnnecessaryUnicodeEscape")
public enum RegexCharacterClass {

    PERSIAN_NUMERIC("\\u06F0-\\u06F9"),
    ARABIC_NUMERIC("\\u0660-\\u0669"),
    PERSIAN_ARABIC_NUMERIC(ARABIC_NUMERIC.classStr + PERSIAN_NUMERIC.classStr),
    PERSIAN_ALPHABET("\\u0621-\\u0628\\u062A-\\u063A\\u0641-\\u0642\\u0644-\\u0649" +
            "\\u06CC\\u06A9\\u06AF\\u0686\\u067E\\u0698");

    private final String classStr;

    RegexCharacterClass(String classStr) {
        this.classStr = classStr;
    }

    public String getClassStr() {
        return classStr;
    }

}
