package com.persiantools4j.nationalid;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persiantools4j.Generated;
import com.persiantools4j.exception.ValidationException;
import com.persiantools4j.utils.NumberUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * The NationalIdServiceImpl class implements the {@link NationalIdService} interface
 * and provides functionality for validating national IDs and finding associated {@link Hometown}.
 * <p>
 * This class uses the Singleton design pattern to ensure a single instance is used throughout the application.
 */
public class NationalIdServiceImpl implements NationalIdService {

    private static volatile NationalIdServiceImpl instance;
    private Map<String, Hometown> hometownMap;

    /**
     * Private constructor to prevent direct instantiation.
     */
    private NationalIdServiceImpl() {

    }

    /**
     * Returns the singleton instance of {@link NationalIdServiceImpl}.
     *
     * @return The single instance of {@link NationalIdServiceImpl}
     */
    @Generated
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
        int sum = IntStream.range(0, 9)
                .boxed()
                .reduce(0, (partialResult, index) -> {
                    int digit = NumberUtils.getNumericValue(nationalId, index);
                    return partialResult + digit * (10 - index);
                }, Integer::sum);
        int remainder = sum % 11;
        boolean remainderLessThanTwo = (remainder < 2) && (controlDigit == remainder);
        boolean remainderEqualAndMoreThanTwo = (remainder >= 2) && (remainder + controlDigit == 11);
        if (!remainderLessThanTwo && !remainderEqualAndMoreThanTwo) {
            throw new ValidationException("Invalid national ID: " + nationalId);
        }
    }

    @Override
    public Optional<Hometown> findHometown(String nationalId) {
        validate(nationalId);
        String firstThreeDigits = nationalId.substring(0, 3);
        return getHometownMap().entrySet().stream()
                .filter(entry -> entry.getKey().equals(firstThreeDigits))
                .map(Map.Entry::getValue)
                .findFirst();
    }

    @Override
    public Map<String, Hometown> getHometownMap() {
        if (hometownMap == null) {
            hometownMap = new HashMap<>();
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("nationalid/hometown-data.json");
            HometownData hometownData = new HometownData();
            try {
                hometownData = objectMapper.readValue(inputStream, HometownData.class);
            } catch (IOException ignored) {
            }
            hometownData.getHometownData()
                    .forEach((code, hometownDetail) -> {
                        Hometown hometown = Hometown.of(hometownDetail.get(0), hometownDetail.get(1));
                        hometownMap.put(code, hometown);
                    });
        }
        return hometownMap;
    }

    /**
     * Validates the format of the provided national ID.
     *
     * @param nationalId The national ID to validate
     * @throws ValidationException If the national ID is null or in an invalid format
     */
    private void validateNationalIdFormat(String nationalId) {
        if (nationalId == null) {
            throw new ValidationException("National ID is null");
        }
        if (!nationalId.matches("\\d{10}") || nationalId.matches("(\\d)\\1{9}")) {
            throw new ValidationException("Invalid national ID format: " + nationalId);
        }
    }

}
