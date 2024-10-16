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

package com.persiantools4j.nationalid;

import com.persiantools4j.Validator;

import java.util.Optional;

/**
 * The {@code NationalIdService} interface defines a contract for services that handle operations
 * related to national IDs and their associated {@link Hometown}.
 * <p>
 * This interface extends the {@link Validator} interface, providing validation functionality for national ID strings.
 */
public interface NationalIdService extends Validator<String> {

    /**
     * Finds the {@link Hometown} associated with the given national ID.
     *
     * @param nationalId the national ID for which to find the {@link Hometown}
     * @return an {@link Optional} containing the {@link Hometown} if found; otherwise, an empty {@link Optional}
     */
    Optional<Hometown> findHometown(String nationalId);

}
