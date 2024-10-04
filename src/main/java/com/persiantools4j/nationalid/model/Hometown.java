package com.persiantools4j.nationalid.model;

import java.util.Objects;

public class Hometown {

    private final String province;
    private final String city;

    private Hometown(String province, String city) {
        this.province = province;
        this.city = city;
    }

    public static Hometown of(String province, String city) {
        return new Hometown(province, city);
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hometown)) return false;
        Hometown hometown = (Hometown) o;
        return Objects.equals(province, hometown.province) && Objects.equals(city, hometown.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(province, city);
    }

    @Override
    public String toString() {
        return "Hometown{" +
                "province='" + province + '\'' +
                ", city='" + city + '\'' +
                '}';
    }

}
