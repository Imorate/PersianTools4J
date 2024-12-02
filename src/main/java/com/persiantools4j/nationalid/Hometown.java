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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.persiantools4j.Generated;

import java.util.List;
import java.util.Objects;

/**
 * Represents a {@code Hometown}, encapsulating its code(s), province, and city.
 * <p>
 * This class is immutable and should be instantiated using the static factory method.
 */
public final class Hometown {

    private final List<String> code;
    private final String province;
    private final String city;

    /**
     * Private constructor for creating an immutable {@code Hometown} instance and also Jackson creator.
     * <p>
     * To instantiate this class, use the static factory method {@link #of(List, String, String)}.
     *
     * @param code     a list of code(s) associated with the {@code Hometown}
     * @param province the name of the province where the city is located
     * @param city     the name of the city
     */
    @JsonCreator
    private Hometown(
            @JsonProperty("code") List<String> code,
            @JsonProperty("province") String province,
            @JsonProperty("city") String city
    ) {
        this.code = code;
        this.province = province;
        this.city = city;
    }

    /**
     * Creates a new {@code Hometown} instance with the specified code(s), province, and city.
     *
     * @param code     a list of code(s) representing the hometown
     * @param province the name of the province where the city is located
     * @param city     the name of the city
     * @return a new {@code Hometown} instance
     */
    public static Hometown of(List<String> code, String province, String city) {
        return new Hometown(code, province, city);
    }

    /**
     * Returns the list of code(s) associated with this {@code Hometown}.
     *
     * @return the list of code(s)
     */
    public List<String> getCode() {
        return code;
    }

    /**
     * Returns the name of the province for this {@code Hometown}.
     *
     * @return the province name
     */
    public String getProvince() {
        return province;
    }

    /**
     * Returns the name of the city for this {@code Hometown}.
     *
     * @return the city name
     */
    public String getCity() {
        return city;
    }

    /**
     * Compares this {@code Hometown} instance with the specified object for equality.
     * <p>
     * Two {@code Hometown} objects are considered equal if they have the same code(s), province, and city.
     *
     * @param o the object to compare with
     * @return {@code true} if the specified object is equal to this {@code Hometown}, otherwise {@code false}
     */
    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Hometown)) {
            return false;
        }
        Hometown hometown = (Hometown) o;
        return Objects.equals(code, hometown.code)
                && Objects.equals(province, hometown.province)
                && Objects.equals(city, hometown.city);
    }

    /**
     * Returns a hash code value for this {@code Hometown} instance.
     * <p>
     * The hash code is generated based on the code(s), province and city names.
     *
     * @return a hash code value for this {@code Hometown} instance
     */
    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(code, province, city);
    }

    /**
     * Returns a string representation of this {@code Hometown} instance.
     * <p>
     * The string representation includes the code(s), province, and city names.
     *
     * @return a string representation of the {@code Hometown} object
     */
    @Generated
    @Override
    public String toString() {
        return "Hometown{" + "code=" + code + ", province='" + province + '\'' + ", city='" + city + '\'' + '}';
    }

}
