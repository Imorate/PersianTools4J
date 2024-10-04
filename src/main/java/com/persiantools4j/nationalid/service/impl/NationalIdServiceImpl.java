package com.persiantools4j.nationalid.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persiantools4j.exception.ValidationException;
import com.persiantools4j.nationalid.model.Location;
import com.persiantools4j.nationalid.model.LocationData;
import com.persiantools4j.nationalid.service.NationalIdService;
import com.persiantools4j.utils.NumberUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

public class NationalIdServiceImpl implements NationalIdService {

    private static volatile NationalIdServiceImpl instance;
    private Map<String, Location> locationMap;

    private NationalIdServiceImpl() {

    }

    public static NationalIdServiceImpl getInstance() {
        if (instance == null) {
            synchronized (NationalIdServiceImpl.class) {
                if (instance == null) {
                    instance = new NationalIdServiceImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public boolean isValid(String nationalId) {
        try {
            validate(nationalId);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    @Override
    public void validate(String nationalId) {
        validateNationalIdFormat(nationalId);
        int controlDigit = NumberUtils.getNumericValue(nationalId, nationalId.length() - 1);
        BiFunction<Integer, Integer, Integer> digitCalculator = (partialResult, index) -> {
            int digit = NumberUtils.getNumericValue(nationalId, index);
            return partialResult + digit * (10 - index);
        };
        int sum = IntStream.range(0, 9)
                .boxed()
                .reduce(0, digitCalculator, Integer::sum);
        int remainder = sum % 11;
        boolean remainderLessThanTwo = (remainder < 2) && (controlDigit == remainder);
        boolean remainderEqualAndMoreThanTwo = (remainder >= 2) && (remainder + controlDigit == 11);
        if (!remainderLessThanTwo && !remainderEqualAndMoreThanTwo) {
            throw new ValidationException("Invalid NationalId: " + nationalId);
        }
    }

    @Override
    public Optional<Location> findLocation(String nationalId) {
        validate(nationalId);
        String firstThreeDigits = nationalId.substring(0, 3);
        return getLocationMap().entrySet().stream()
                .filter(entry -> entry.getKey().equals(firstThreeDigits))
                .map(Map.Entry::getValue)
                .findFirst();
    }

    @Override
    public Map<String, Location> getLocationMap() {
        if (locationMap == null) {
            locationMap = new HashMap<>();
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("nationalid/location-data.json");
            LocationData locationData = new LocationData();
            try {
                locationData = objectMapper.readValue(inputStream, LocationData.class);
            } catch (IOException ignored) {
            }
            locationData.getLocationMap()
                    .forEach((code, locationDetail) -> {
                        Location location = Location.of(locationDetail.get(0), locationDetail.get(1));
                        locationMap.put(code, location);
                    });
        }
        return locationMap;
    }

    private void validateNationalIdFormat(String nationalId) {
        if (nationalId == null) {
            throw new ValidationException("National ID is null");
        }
        nationalId = nationalId.trim();
        if (!nationalId.matches("\\d{10}") || nationalId.matches("(\\d)\\1{9}")) {
            throw new ValidationException("Invalid National ID format: " + nationalId);
        }
    }

}
