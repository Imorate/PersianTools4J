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

package com.persiantools4j.objectmapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.persiantools4j.utils.StringUtils;

import java.io.IOException;

/**
 * A custom Jackson deserializer for normalizing Persian strings during JSON deserialization.
 * <p>
 * This deserializer ensures that any incoming Persian strings are normalized by converting Arabic characters
 * to their Persian equivalents.
 */
public final class PersianStringDeserializer extends JsonDeserializer<String> {

    /**
     * Deserializes and normalizes Persian strings.
     *
     * @param jsonParser             the {@link JsonParser} to read the string value
     * @param deserializationContext the {@link DeserializationContext} for handling deserialization
     * @return a normalized Persian string or {@code null} if the input is empty or {@code null}
     * @throws IOException if an I/O error occurs during parsing
     */
    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String value = jsonParser.getValueAsString();
        if (value == null || value.isEmpty()) {
            return value;
        }
        return StringUtils.normalizePersian(value);
    }

}
