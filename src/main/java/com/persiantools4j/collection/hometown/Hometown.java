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
 * Represents a {@code Hometown}, encapsulating its province, city and code(s).
 * <p>
 * This class is immutable and should be instantiated using the static factory method.
 */
@Generated
public final class Hometown {

    private final String province;
    private final String city;
    private final List<String> codes;

    /**
     * Constructs an immutable {@code Hometown} instance.
     * <p>
     * This constructor is annotated with {@link JsonCreator} to enable Jackson deserialization,
     * mapping the specified JSON properties to the corresponding fields.
     *
     * @param province the name of the province where the city is located,
     *                 mapped from the {@code "province"} JSON property
     * @param city     the name of the city, mapped from the {@code "city"} JSON property
     * @param codes    a list of code(s) associated with the {@code Hometown},
     *                 mapped from the {@code "code"} JSON property
     */
    @JsonCreator
    public Hometown(
            @JsonProperty("province") String province,
            @JsonProperty("city") String city,
            @JsonProperty("code") List<String> codes
    ) {
        this.province = province;
        this.city = city;
        this.codes = codes;
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
     * Returns the list of code(s) associated with this {@code Hometown}.
     *
     * @return the list of code(s)
     */
    public List<String> getCodes() {
        return codes;
    }

    /**
     * Compares this {@code Hometown} instance with the specified object for equality.
     * <p>
     * Two {@code Hometown} objects are considered equal if they have the same province, city and code(s).
     *
     * @param o the object to compare with
     * @return {@code true} if the specified object is equal to this {@code Hometown}, otherwise {@code false}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Hometown)) {
            return false;
        }
        Hometown hometown = (Hometown) o;
        return Objects.equals(province, hometown.province)
                && Objects.equals(city, hometown.city)
                && Objects.equals(codes, hometown.codes);
    }

    /**
     * Returns a hash code value for this {@code Hometown} instance.
     * <p>
     * The hash code is generated based on the province, city and code(s).
     *
     * @return a hash code value for this {@code Hometown} instance
     */
    @Override
    public int hashCode() {
        return Objects.hash(province, city, codes);
    }

    /**
     * Returns a string representation of this {@code Hometown} instance.
     * <p>
     * The string representation includes the province, city, and code(s).
     *
     * @return a string representation of the {@code Hometown} object
     */
    @Override
    public String toString() {
        return "Hometown{" + "province='" + province + '\'' + ", city='" + city + '\'' + ", codes=" + codes + '}';
    }

}
