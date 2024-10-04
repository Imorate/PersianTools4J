package com.persiantools4j.nationalid.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationData {

    private final Map<String, List<String>> locationMap;

    public LocationData() {
        locationMap = new HashMap<>();
    }

    public Map<String, List<String>> getLocationMap() {
        return locationMap;
    }

    @JsonAnySetter
    public void addLocation(String code, List<String> location) {
        locationMap.put(code, location);
    }

}
