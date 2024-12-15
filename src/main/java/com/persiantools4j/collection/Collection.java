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

package com.persiantools4j.collection;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The {@code Collection} abstract class defines a contract for objects that manage a collection of items
 * of a specified type {@code T}.
 *
 * @param <T> the type of elements contained in the collection
 */
public abstract class Collection<T> {

    /**
     * The internal list that holds the elements of the collection.
     * <p>
     * This list is used as the data source for all operations in the collection.
     */
    protected List<T> collectionList;

    /**
     * Finds all elements in the collection that satisfy the given predicate.
     *
     * @param predicate a {@link Predicate} to filter the elements
     * @return a {@link List} of elements of type {@code T} that match the predicate, or an empty list if none match
     */
    public List<T> findAllBy(Predicate<T> predicate) {
        if (predicate == null) {
            return Collections.emptyList();
        }
        return collectionList.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    /**
     * Finds a single element in the collection that satisfies the given predicate.
     *
     * @param predicate a {@link Predicate} to filter the elements
     * @return an {@link Optional} containing the first matching element, or empty if no element matches
     */
    public Optional<T> findBy(Predicate<T> predicate) {
        if (predicate == null) {
            return Optional.empty();
        }
        return collectionList.stream()
                .filter(predicate)
                .findAny();
    }

}
