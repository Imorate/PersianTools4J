/*
 * Copyright 2024 Imorate <dev.imorate@gmail.com> and contributors.
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

package com.persiantools4j.nationalid;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.persiantools4j.exception.ValidationException;
import com.persiantools4j.utils.NumberUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * The {@code NationalIdServiceImpl} class implements the {@link NationalIdService} interface
 * and provides functionality for validating national IDs and finding associated {@link Hometown}.
 * <p>
 * This class follows the Singleton design pattern to ensure that a single instance is used throughout the application.
 */
public final class NationalIdServiceImpl implements NationalIdService {

    private static final Pattern NATIONAL_ID_PATTERN = Pattern.compile("\\d{10}");
    private static final Pattern REPEATED_DIGITS_PATTERN = Pattern.compile("(\\d)\\1{9}");
    private List<Hometown> hometownList;

    /**
     * Private constructor to prevent direct instantiation.
     */
    private NationalIdServiceImpl() {

    }

    /**
     * Returns the singleton instance of {@code NationalIdServiceImpl}.
     *
     * @return The single instance of {@code NationalIdServiceImpl}
     */
    public static NationalIdServiceImpl getInstance() {
        return NationalIdServiceImplHelper.INSTANCE;
    }

    /**
     * Validates the format of the provided national ID.
     *
     * @param nationalId the national ID to validate
     * @throws ValidationException if the national ID is null or in an invalid format
     */
    private static void validateNationalIdFormat(String nationalId) {
        if (nationalId == null) {
            throw new ValidationException("National ID is null");
        }
        if (!NATIONAL_ID_PATTERN.matcher(nationalId).matches() ||
                REPEATED_DIGITS_PATTERN.matcher(nationalId).matches()
        ) {
            throw new ValidationException("Invalid national ID format: " + nationalId);
        }
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
        int length = nationalId.length();
        int sum = IntStream.range(0, length - 1)
                .boxed()
                .reduce(0, (Integer partialResult, Integer index) -> {
                    int digit = NumberUtils.getNumericValue(nationalId, index);
                    return partialResult + digit * (length - index);
                }, Integer::sum);
        int remainder = sum % (length + 1);
        int controlDigit = NumberUtils.getNumericValue(nationalId, length - 1);
        boolean remainderLessThanTwo = (remainder < 2) && (controlDigit == remainder);
        boolean remainderEqualAndMoreThanTwo = (remainder >= 2) && (remainder + controlDigit == (length + 1));
        if (!remainderLessThanTwo && !remainderEqualAndMoreThanTwo) {
            throw new ValidationException("Invalid national ID: " + nationalId);
        }
    }

    @Override
    public Optional<Hometown> findHometown(String nationalId) {
        validate(nationalId);
        String firstThreeDigits = nationalId.substring(0, 3);
        return getHometownList().stream()
                .filter(hometown -> hometown.getCode().contains(firstThreeDigits))
                .findFirst();
    }

    @Override
    public List<Hometown> getHometownList() {
        if (hometownList == null) {
            ObjectMapper objectMapper = new ObjectMapper();
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            try (InputStream inputStream = contextClassLoader.getResourceAsStream("nationalid/hometown-data.json")) {
                hometownList = objectMapper.readValue(inputStream, new TypeReference<List<Hometown>>() {
                });
            } catch (IOException e) {
                hometownList = Collections.emptyList();
            }
        }
        return hometownList;
    }

    /**
     * Private static helper class to implement the Singleton design pattern.
     */
    private static class NationalIdServiceImplHelper {
        private static final NationalIdServiceImpl INSTANCE = new NationalIdServiceImpl();
    }

}
