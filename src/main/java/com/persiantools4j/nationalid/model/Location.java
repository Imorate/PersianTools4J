package com.persiantools4j.nationalid.model;

import java.util.Objects;

public class Location {

    private final String province;
    private final String city;

    private Location(String province, String city) {
        this.province = province;
        this.city = city;
    }

    public static Location of(String province, String city) {
        return new Location(province, city);
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
        if (!(o instanceof Location)) return false;
        Location location = (Location) o;
        return Objects.equals(province, location.province) && Objects.equals(city, location.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(province, city);
    }

    @Override
    public String toString() {
        return "Location{" +
                "province='" + province + '\'' +
                ", city='" + city + '\'' +
                '}';
    }

}
