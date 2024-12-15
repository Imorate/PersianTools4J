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

package com.persiantools4j.collection.bank;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.persiantools4j.collection.Collection;
import com.persiantools4j.objectmapper.ObjectMapperWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * The {@code BankCollection} class extends the {@link Collection} abstract class to provide a collection
 * of {@link Bank} objects. It follows the Singleton design pattern to ensure that only a single instance
 * of this class is used throughout the application.
 * <p>
 * The bank data is loaded from a JSON file ({@code bank/banks-data.json}) the first time the
 * collection is accessed.
 */
public final class BankCollection extends Collection<Bank> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankCollection.class);

    /**
     * Private constructor to prevent direct instantiation. The data is read from the
     * {@code bank/banks-data.json} file and parsed into a {@link List} of {@link Bank} objects.
     */
    private BankCollection() {
        ObjectMapper objectMapper = ObjectMapperWrapper.getInstance();
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream inputStream = contextClassLoader.getResourceAsStream("bank/banks-data.json")) {
            collectionList = objectMapper.readValue(inputStream, new TypeReference<List<Bank>>() {
            });
        } catch (IOException e) {
            LOGGER.error("Bank collection cannot be parsed", e);
            collectionList = Collections.emptyList();
        }
    }

    /**
     * Retrieves the singleton instance of {@code BankCollection}.
     *
     * @return the singleton instance of {@code BankCollection}
     */
    public static BankCollection getInstance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * Private static helper class to implement the Singleton design pattern.
     */
    private static class InstanceHolder {
        private static final BankCollection INSTANCE = new BankCollection();
    }

}
