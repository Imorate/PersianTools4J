package com.persiantools4j.nationalid;

import com.persiantools4j.Generated;

import java.util.Objects;

/**
 * The Hometown class represents a hometown with a province and city.
 * <p>
 * Instances of this class are immutable and can be created using the static factory method.
 */
public class Hometown {

    private final String province;
    private final String city;

    /**
     * Private constructor to initialize the province and city.
     * Use the static factory method {@link Hometown#of(String, String)} to create instances.
     *
     * @param province The name of the province
     * @param city     The name of the city
     */
    private Hometown(String province, String city) {
        this.province = province;
        this.city = city;
    }

    /**
     * Static factory method to create a new Hometown instance.
     *
     * @param province The name of the province
     * @param city     The name of the city
     * @return A new instance of Hometown with the specified province and city
     */
    public static Hometown of(String province, String city) {
        return new Hometown(province, city);
    }

    /**
     * Returns the name of the province.
     *
     * @return The province name
     */
    public String getProvince() {
        return province;
    }

    /**
     * Returns the name of the city.
     *
     * @return The city name
     */
    public String getCity() {
        return city;
    }

    /**
     * Compares this Hometown object with another object for equality.
     * <p>
     * Two Hometown objects are equal if they have the same province and city.
     *
     * @param o The object to compare with
     * @return True if the specified object is equal to this object, false otherwise
     */
    @Override
    @Generated
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hometown)) return false;
        Hometown hometown = (Hometown) o;
        return Objects.equals(province, hometown.province) && Objects.equals(city, hometown.city);
    }

    /**
     * Returns a hash code value for the object based on province and city.
     *
     * @return The hash code value for this Hometown object
     */
    @Override
    @Generated
    public int hashCode() {
        return Objects.hash(province, city);
    }

    /**
     * Returns a string representation of the Hometown object, including province and city.
     *
     * @return A string representation of the object
     */
    @Override
    @Generated
    public String toString() {
        return "Hometown{" +
                "province='" + province + '\'' +
                ", city='" + city + '\'' +
                '}';
    }

}
