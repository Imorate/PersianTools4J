package com.persiantools4j;

import com.persiantools4j.exception.ValidationException;

public interface Validator<T> {

    boolean isValid(T t);

    void validate(T t) throws ValidationException;

}
