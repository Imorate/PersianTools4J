package com.persiantools4j.nationalid;

import com.persiantools4j.Validator;

import java.util.Map;
import java.util.Optional;

/**
 * The NationalIdService interface defines a contract for services that
 * handle operations related to national IDs and their associated {@link Hometown}.
 * <p>
 * It extends the Validator interface, providing validation functionality for national ID strings.
 */
public interface NationalIdService extends Validator<String> {

    /**
     * Finds the {@link Hometown} associated with the given national ID.
     *
     * @param nationalId The national ID for which to find the {@link Hometown}
     * @return An Optional containing the {@link Hometown} if found; otherwise, an empty Optional
     */
    Optional<Hometown> findHometown(String nationalId);

    /**
     * Retrieves a map of all {@link Hometown}, where the key is the first three digits of
     * national ID that represents province and city code and the value is the corresponding Hometown.
     *
     * @return A Map containing province and city code as keys and {@link Hometown} objects as values
     */
    Map<String, Hometown> getHometownMap();

}
