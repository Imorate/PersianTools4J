package com.persiantools4j.nationalid.service;

import com.persiantools4j.Validator;
import com.persiantools4j.nationalid.model.Hometown;

import java.util.Map;
import java.util.Optional;

public interface NationalIdService extends Validator<String> {

    Optional<Hometown> findHometown(String nationalId);

    Map<String, Hometown> getHometownMap();

}
