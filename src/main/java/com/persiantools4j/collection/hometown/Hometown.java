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

package com.persiantools4j.collection.hometown;

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
     * Constructs an immutable {@code Hometown} instance.
     * <p>
     * This constructor is annotated with {@link JsonCreator} to enable Jackson deserialization,
     * mapping the specified JSON properties to the corresponding fields.
     *
     * @param city     the name of the city, mapped from the {@code "city"} JSON property
     * @param province the name of the province where the city is located,
     *                 mapped from the {@code "province"} JSON property
     * @param code     a list of code(s) associated with the {@code Hometown},
     *                 mapped from the {@code "code"} JSON property
     */
    @JsonCreator
    public Hometown(
            @JsonProperty("city") String city,
            @JsonProperty("province") String province,
            @JsonProperty("code") List<String> code
    ) {
        this.city = city;
        this.province = province;
        this.code = code;
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