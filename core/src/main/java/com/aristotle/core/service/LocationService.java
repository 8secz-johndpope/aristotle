package com.aristotle.core.service;

import java.util.List;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Location;
import com.aristotle.core.persistance.LocationType;

public interface LocationService {

    List<Location> getAllLocationsOfType(LocationType locationType, Long parentLocationId) throws AppException;

    List<Location> getAllLocationsOfType(Long locationTypeId, Long parentLocationId) throws AppException;

    List<LocationType> getAllLocationTypes() throws AppException;
}
