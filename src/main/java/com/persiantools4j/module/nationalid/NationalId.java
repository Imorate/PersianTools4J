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

package com.persiantools4j.module.nationalid;

import com.persiantools4j.Generated;
import com.persiantools4j.collection.hometown.Hometown;

import java.util.List;
import java.util.Objects;

/**
 * Represents a National ID in a result of parsing action with various components such as
 * hometown code, personal code, control digit and a {@link List} of {@link Hometown}(s).
 */
@Generated
public final class NationalId {

    private final String id;
    private final String hometownCode;
    private final String personalCode;
    private final int controlDigit;
    private final List<Hometown> hometowns;

    /**
     * Constructs a new {@code NationalId} instance with the specified details.
     *
     * @param id           the national ID string
     * @param hometownCode the code representing the associated hometown
     * @param personalCode the personal code extracted from the national ID
     * @param controlDigit the control digit used for validation
     * @param hometowns    a list of {@link Hometown} instances associated with the hometown code
     */
    NationalId(String id, String hometownCode, String personalCode, int controlDigit, List<Hometown> hometowns) {
        this.id = id;
        this.hometownCode = hometownCode;
        this.personalCode = personalCode;
        this.controlDigit = controlDigit;
        this.hometowns = hometowns;
    }

    /**
     * Retrieves the national ID string.
     *
     * @return the national ID string
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the hometown code of the national ID.
     *
     * @return the hometown code
     */
    public String getHometownCode() {
        return hometownCode;
    }

    /**
     * Gets the personal code segment within the national ID.
     *
     * @return the personal code
     */
    public String getPersonalCode() {
        return personalCode;
    }

    /**
     * Retrieves the control digit used in the national ID.
     *
     * @return the control digit
     */
    public int getControlDigit() {
        return controlDigit;
    }

    /**
     * Retrieves the list of hometowns associated with this national ID.
     *
     * @return a {@link List} of {@link Hometown} instances
     */
    public List<Hometown> getHometowns() {
        return hometowns;
    }

    /**
     * Compares this {@code NationalId} instance with the specified object for equality.
     * <p>
     * Two {@code NationalId} objects are considered equal if they have the same national ID string.
     *
     * @param o the object to compare with
     * @return {@code true} if the specified object is equal to this {@code NationalId}, otherwise {@code false}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NationalId)) {
            return false;
        }
        NationalId that = (NationalId) o;
        return Objects.equals(id, that.id);
    }

    /**
     * Returns a hash code value for this {@code NationalId} instance.
     * <p>
     * The hash code is generated based on the national ID string.
     *
     * @return a hash code value for this {@code NationalId} instance
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    /**
     * Returns a string representation of this {@code NationalId} instance.
     * <p>
     * The string representation includes the national ID, hometownCode segment, personal code segment,
     * control digit segment and {@code Hometown} list
     *
     * @return a string representation of the {@code NationalId} object
     */
    @Override
    public String toString() {
        return "NationalId{" + "id='" + id + '\'' + ", hometownCode='" + hometownCode + '\'' +
                ", personalCode='" + personalCode + '\'' + ", controlDigit=" + controlDigit +
                ", hometowns=" + hometowns + '}';
    }

}
