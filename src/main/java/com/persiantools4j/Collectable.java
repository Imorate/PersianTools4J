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

package com.persiantools4j;

import java.util.List;

/**
 * The {@code Collectable} interface defines a contract for objects that are capable of providing a collection of items
 * of a specified type {@code T}.
 *
 * @param <T> the type of elements contained in the collection
 */
public interface Collectable<T> {

    /**
     * Retrieves the collection of elements.
     *
     * @return a {@link List} of elements of type {@code T}
     */
    List<T> getCollection();

}
