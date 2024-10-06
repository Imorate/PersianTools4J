package com.persiantools4j.nationalid;

import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The HometownData class represents a collection of  information,
 * where each entry is associated with a unique code.
 * <p>
 * This class allows for dynamic addition of hometown entries using the code as the key and
 * a list of hometown names as the value.
 */
public class HometownData {

    private final Map<String, List<String>> hometownData;

    /**
     * Constructs a new HometownData instance with an empty map to store hometown entries.
     */
    public HometownData() {
        hometownData = new HashMap<>();
    }

    /**
     * Returns the map of hometown data.
     *
     * @return A map where the key is a code and the value is a list of hometown names
     */
    public Map<String, List<String>> getHometownData() {
        return hometownData;
    }

    /**
     * Adds a hometown entry to the map using the specified code.
     * <p>
     * The method is annotated with @JsonAnySetter to allow dynamic property handling
     * during JSON deserialization.
     *
     * @param code     The unique code for the hometown entry
     * @param hometown A list of hometown names associated with the code
     */
    @JsonAnySetter
    public void addHometown(String code, List<String> hometown) {
        hometownData.put(code, hometown);
    }

}
