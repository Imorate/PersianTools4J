package com.persiantools4j.nationalid.service;

import com.persiantools4j.Validator;
import com.persiantools4j.nationalid.model.Location;

import java.util.Map;
import java.util.Optional;

public interface NationalIdService extends Validator<String> {

    Optional<Location> findLocation(String nationalId);

    Map<String, Location> getLocationMap();

}
