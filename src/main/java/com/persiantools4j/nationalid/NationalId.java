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

import com.persiantools4j.Generated;

import java.util.List;
import java.util.Objects;

/**
 * Represents a National ID in a result of parsing action with various components such as
 * hometown code, personal code, control digit and a {@link List} of {@link Hometown}(s).
 */
public class NationalId {

    private final String id;

    private String hometownCode;

    private String personalCode;

    private int controlDigit;

    private List<Hometown> hometownList;

    /**
     * Constructs a new {@code NationalId} instance with the specified national ID string.
     *
     * @param nationalId the national ID string
     */
    public NationalId(String nationalId) {
        this.id = nationalId;
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
     * Sets the hometown code for this national ID.
     *
     * @param hometownCode the new hometown code
     */
    public void setHometownCode(String hometownCode) {
        this.hometownCode = hometownCode;
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
     * Sets the personal code for this national ID.
     *
     * @param personalCode the new personal code
     */
    public void setPersonalCode(String personalCode) {
        this.personalCode = personalCode;
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
     * Sets the control digit for this national ID.
     *
     * @param controlDigit the new control digit
     */
    public void setControlDigit(int controlDigit) {
        this.controlDigit = controlDigit;
    }

    /**
     * Retrieves the list of hometowns associated with this national ID.
     *
     * @return a {@link List} of {@link Hometown} instances
     */
    public List<Hometown> getHometownList() {
        return hometownList;
    }

    /**
     * Sets the list of {@link Hometown}s for this national ID.
     *
     * @param hometownList the new {@link List} of {@link Hometown}
     */
    public void setHometownList(List<Hometown> hometownList) {
        this.hometownList = hometownList;
    }

    /**
     * Compares this {@code NationalId} instance with the specified object for equality.
     * <p>
     * Two {@code NationalId} objects are considered equal if they have the same national ID string.
     *
     * @param o the object to compare with
     * @return {@code true} if the specified object is equal to this {@code NationalId}, otherwise {@code false}
     */
    @Generated
    @Override
    public final boolean equals(Object o) {
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
    @Generated
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
    @Generated
    @Override
    public String toString() {
        return "NationalId{" + "id='" + id + '\'' + ", hometownCode='" + hometownCode + '\'' +
                ", personalCode='" + personalCode + '\'' + ", controlDigit=" + controlDigit +
                ", hometownList=" + hometownList + '}';
    }

}
