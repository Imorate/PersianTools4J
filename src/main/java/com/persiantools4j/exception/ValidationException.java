package com.persiantools4j.exception;

/**
 * The ValidationException class represents an exception that is thrown when validation of an object fails.
 * <p>
 * This class extends {@link ValidationException}, indicating that the exception is related to an
 * invalid argument or input.
 */
public class ValidationException extends IllegalArgumentException {

    /**
     * Constructs a new ValidationException with the specified detail message.
     *
     * @param s the detail message explaining the reason for the validation failure
     */
    public ValidationException(String s) {
        super(s);
    }

}
