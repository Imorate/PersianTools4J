package com.persiantools4j;

import com.persiantools4j.exception.ValidationException;

/**
 * The Validator interface provides a contract for validating objects of type T.
 * It defines methods for checking if an object is valid and for performing
 * validation with potential exception handling.
 *
 * @param <T> the type of objects that this Validator can validate
 * @since 1.0
 */
public interface Validator<T> {

    /**
     * Checks if the given object is valid.
     *
     * @param t the object to validate
     * @return True if the object is valid
     */
    boolean isValid(T t);

    /**
     * Validates the given object and throws a {@link ValidationException}.
     * If the object does not meet validation criteria.
     *
     * @param t The object to validate
     * @throws ValidationException If the object is not valid
     */
    void validate(T t) throws ValidationException;

}
