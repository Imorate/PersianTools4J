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

package com.persiantools4j.module.nationalid;

import com.persiantools4j.Parsable;
import com.persiantools4j.Validatable;
import com.persiantools4j.collection.hometown.Hometown;
import com.persiantools4j.collection.hometown.HometownCollection;
import com.persiantools4j.exception.ParseException;
import com.persiantools4j.exception.ValidationException;
import com.persiantools4j.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * The {@code NationalIdService} class implements the {@link Validatable} and {@link Parsable} interfaces,
 * providing functionality for validating national IDs and parsing them to extract associated {@link Hometown}
 * and other relevant information.
 *
 * @see Validatable
 * @see Parsable
 */
public final class NationalIdService implements Validatable<String>, Parsable<String, NationalId> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NationalIdService.class);
    private static final Pattern NATIONAL_ID_PATTERN = Pattern.compile("\\d{10}");

    /**
     * Regex pattern to detect invalid national IDs made up of the same digit repeated 10 times.
     * Except for "1111111111", which is considered valid.
     */
    private static final Pattern NATIONAL_ID_REPEATED_DIGITS_PATTERN = Pattern.compile("([02-9])\\1{9}");

    /**
     * Validates the format of the provided national ID.
     *
     * @param nationalId the national ID to validate
     * @throws ValidationException if the national ID is null or in an invalid format
     */
    private static void validateFormat(String nationalId) {
        if (nationalId == null) {
            throw new ValidationException("National ID is null");
        }
        if (!NATIONAL_ID_PATTERN.matcher(nationalId).matches() ||
                NATIONAL_ID_REPEATED_DIGITS_PATTERN.matcher(nationalId).matches()
        ) {
            throw new ValidationException("Invalid national ID format: " + nationalId);
        }
    }

    @Override
    public boolean isValid(String nationalId) {
        try {
            validate(nationalId);
        } catch (ValidationException e) {
            LOGGER.warn(e.getMessage(), e);
            return false;
        }
        return true;
    }

    @Override
    public void validate(String nationalId) {
        validateFormat(nationalId);
        int length = nationalId.length();
        int sum = IntStream.range(0, length - 1)
                .boxed()
                .reduce(0, (Integer partialResult, Integer index) -> {
                    int digit = StringUtils.getNumericValue(nationalId, index);
                    return partialResult + digit * (length - index);
                }, Integer::sum);
        int remainder = sum % (length + 1);
        int controlDigit = StringUtils.getNumericValue(nationalId, length - 1);
        boolean remainderLessThanTwo = (remainder < 2) && (controlDigit == remainder);
        boolean remainderEqualAndMoreThanTwo = (remainder >= 2) && (remainder + controlDigit == (length + 1));
        if (!remainderLessThanTwo && !remainderEqualAndMoreThanTwo) {
            throw new ValidationException("Invalid national ID: " + nationalId);
        }
    }

    /**
     * Finds the {@link List} of {@link Hometown} associated with the given national ID.
     *
     * @param nationalId the national ID for which to find the {@link Hometown}(s)
     * @return a {@link List} containing the {@link Hometown}(s) if found; otherwise, an empty {@link List}
     */
    public List<Hometown> findHometown(String nationalId) {
        validate(nationalId);
        String firstThreeDigits = nationalId.substring(0, 3);
        return HometownCollection.getInstance()
                .findAllBy(hometown -> hometown.getCode().contains(firstThreeDigits));
    }

    @Override
    public NationalId parse(String nationalId) {
        List<Hometown> hometownList = findHometown(nationalId);
        if (hometownList.isEmpty()) {
            throw new ParseException("Unable to find hometown associated to the national ID: " + nationalId);
        }
        String controlDigit = nationalId.substring(9);
        NationalId nationalIdObj = new NationalId(nationalId);
        nationalIdObj.setHometownCode(nationalId.substring(0, 3));
        nationalIdObj.setPersonalCode(nationalId.substring(3, nationalId.length() - 1));
        nationalIdObj.setControlDigit(Integer.parseInt(controlDigit));
        nationalIdObj.setHometownList(hometownList);
        return nationalIdObj;
    }

}
