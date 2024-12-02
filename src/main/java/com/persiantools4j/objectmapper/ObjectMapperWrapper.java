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


import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * A utility class providing a singleton instance of {@link ObjectMapper}, configured with custom deserializers.
 * <p>
 * This wrapper ensures that all JSON deserialization leverages the {@link PersianStringDeserializer} to normalize
 * Persian strings.
 */
public final class ObjectMapperWrapper {

    /**
     * Private constructor to prevent direct instantiation.
     * <p>
     * Registers the {@link PersianStringDeserializer} to handle string normalization during JSON deserialization.
     */
    private ObjectMapperWrapper() {

    }

    /**
     * Returns the singleton instance of {@link ObjectMapper}.
     *
     * @return the single {@link ObjectMapper} instance
     */
    public static ObjectMapper getInstance() {
        return ObjectMapperWrapper.InstanceHolder.INSTANCE;
    }

    /**
     * Private static helper class to implement the Singleton design pattern for the {@link ObjectMapper} instance.
     */
    private static class InstanceHolder {
        private static final ObjectMapper INSTANCE = createInstance();

        private static ObjectMapper createInstance() {
            ObjectMapper objectMapper = new ObjectMapper();
            Version version = new Version(1, 0, 0, null, null, null);
            SimpleModule module = new SimpleModule("Persian-Module", version);
            module.addDeserializer(String.class, new PersianStringDeserializer());
            objectMapper.registerModule(module);
            return objectMapper;
        }
    }

}
