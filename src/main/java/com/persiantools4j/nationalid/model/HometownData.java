package com.persiantools4j.nationalid.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HometownData {

    private final Map<String, List<String>> hometownData;

    public HometownData() {
        hometownData = new HashMap<>();
    }

    public Map<String, List<String>> getHometownData() {
        return hometownData;
    }

    @JsonAnySetter
    public void addHometown(String code, List<String> hometown) {
        hometownData.put(code, hometown);
    }

}
