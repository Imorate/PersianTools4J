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

package com.persiantools4j.objectmapper;

/**
 * Simple JSON object class for testing purpose.
 */
class JsonObject {

    /**
     * JSON input.
     */
    private String input;

    /**
     * No-args constructor.
     */
    @SuppressWarnings("unused")
    public JsonObject() {

    }

    /**
     * Required constructor.
     *
     * @param input JSON input
     */
    public JsonObject(String input) {
        this.input = input;
    }

    /**
     * Returns input value.
     *
     * @return value
     */
    public String getInput() {
        return input;
    }

}
