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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.persiantools4j.Generated;

import java.util.List;
import java.util.Objects;

/**
 * Represents a Bank, encapsulating its ID, name, persian name, code(s), bin(s).
 * <p>
 * This class is immutable and should be instantiated using the static factory method.
 */
@Generated
public final class Bank {

    private final String id;
    private final String name;
    private final String persianName;
    private final List<String> codes;
    private final List<String> bins;

    /**
     * Constructs an immutable {@code Bank} instance.
     * <p>
     * This constructor is annotated with {@link JsonCreator} to enable Jackson deserialization,
     * mapping the specified JSON properties to the corresponding fields.
     *
     * @param id          the bank ID, mapped from the {@code "id"} JSON property
     * @param name        the name of the bank in English, mapped from the {@code "name"} JSON property
     * @param persianName the name of the bank in Persian (Farsi), mapped from the {@code "persianName"} JSON property
     * @param codes       a list of code(s) associated to the bank, mapped from the {@code "codes"} JSON property
     * @param bins        a list of BIN(s) associated to the bank, mapped from the {@code "bins"} JSON property
     */
    @JsonCreator
    public Bank(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("persianName") String persianName,
            @JsonProperty("code") List<String> codes,
            @JsonProperty("bin") List<String> bins
    ) {
        this.id = id;
        this.name = name;
        this.persianName = persianName;
        this.codes = codes;
        this.bins = bins;
    }

    /**
     * Returns the ID for this bank.
     *
     * @return the bank ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the English name for this bank.
     *
     * @return the bank ID
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the Persian (Farsi) name for this bank.
     *
     * @return the bank ID
     */
    public String getPersianName() {
        return persianName;
    }

    /**
     * Returns the list of code(s) associated with this bank.
     *
     * @return the list of code(s)
     */
    public List<String> getCodes() {
        return codes;
    }

    /**
     * Returns the list of code(s) associated with this bank.
     *
     * @return the list of BIN(s)
     */
    public List<String> getBins() {
        return bins;
    }

    /**
     * Compares this {@code Bank} instance with the specified object for equality.
     * <p>
     * Two {@code Bank} objects are considered equal if they have the same ID, name, persian name, code(s), bin(s).
     *
     * @param o the object to compare with
     * @return {@code true} if the specified object is equal to this {@code Bank}, otherwise {@code false}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bank)) {
            return false;
        }
        Bank bank = (Bank) o;
        return Objects.equals(id, bank.id) &&
                Objects.equals(name, bank.name) &&
                Objects.equals(persianName, bank.persianName) &&
                Objects.equals(codes, bank.codes) &&
                Objects.equals(bins, bank.bins);
    }

    /**
     * Returns a hash code value for this {@code Bank} instance.
     * <p>
     * The hash code is generated based on the ID, name, persian name, code(s), bin(s).
     *
     * @return a hash code value for this {@code Bank} instance
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, persianName, codes, bins);
    }

    /**
     * Returns a string representation of this {@code Bank} instance.
     * <p>
     * The string representation includes the ID, name, persian name, code(s), bin(s).
     *
     * @return a string representation of the {@code Bank} object
     */
    @Override
    public String toString() {
        return "Bank{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", persianName='" + persianName + '\'' +
                ", codes=" + codes + ", bins=" + bins + '}';
    }

}
