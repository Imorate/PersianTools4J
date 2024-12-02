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

package com.persiantools4j.nationalid;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.persiantools4j.Collectable;
import com.persiantools4j.Generated;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * The {@code HometownCollection} class implements the {@link Collectable} interface to provide a collection
 * of {@link Hometown} objects. This class follows the Singleton design pattern to ensure a single instance
 * is used throughout the application.
 * <p>
 * The hometown data is read from a JSON file ({@code nationalid/hometown-data.json}) when the collection
 * is accessed for the first time.
 */
public final class HometownCollection implements Collectable<Hometown> {

    private List<Hometown> collection;

    /**
     * Private constructor to prevent direct instantiation.
     */
    private HometownCollection() {
    }

    /**
     * Returns the singleton instance of {@code HometownCollection}.
     *
     * @return the single instance of {@code HometownCollection}
     */
    public static HometownCollection getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @Generated
    @Override
    public List<Hometown> getCollection() {
        if (collection == null) {
            ObjectMapper objectMapper = new ObjectMapper();
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            try (InputStream inputStream = contextClassLoader.getResourceAsStream("nationalid/hometown-data.json")) {
                collection = objectMapper.readValue(inputStream, new TypeReference<List<Hometown>>() {
                });
            } catch (IOException e) {
                collection = Collections.emptyList();
            }
        }
        return collection;
    }

    /**
     * Private static helper class to implement the Singleton design pattern.
     */
    private static class InstanceHolder {
        private static final HometownCollection INSTANCE = new HometownCollection();
    }

}
