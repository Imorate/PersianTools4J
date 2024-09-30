package com.persiantools4j.nationalid.service.impl;

import com.persiantools4j.exception.ValidationException;
import com.persiantools4j.nationalid.service.NationalIdService;
import com.persiantools4j.utils.NumberUtils;

import java.util.function.BiFunction;
import java.util.stream.IntStream;

public class NationalIdServiceImpl implements NationalIdService {

    public static volatile NationalIdServiceImpl instance;

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
        } catch (ValidationException e) {
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

    private void validateNationalIdFormat(String nationalId) {
        if (nationalId == null) {
            throw new ValidationException("National ID is null");
        }
        if (!nationalId.matches("^\\d{10}$") || nationalId.matches("^(\\d)\\1{9}$")) {
            throw new ValidationException("Invalid National ID format: " + nationalId);
        }
    }

}
