package com.aristotle.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Location;
import com.aristotle.core.persistance.LocationType;
import com.aristotle.core.persistance.repo.LocationRepository;
import com.aristotle.core.persistance.repo.LocationTypeRepository;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationTypeRepository locationTypeRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Override
    public List<Location> getAllLocationsOfType(LocationType locationType, Long parentLocationId) throws AppException {
        return getAllLocationsOfType(locationType.getId(), parentLocationId);
    }

    @Override
    public List<Location> getAllLocationsOfType(Long locationTypeId, Long parentLocationId) throws AppException {
        if (parentLocationId == null) {
            return locationRepository.getLocationsByLocationTypeId(locationTypeId);
        }
        return locationRepository.getLocationsByLocationTypeIdAndParentLocationId(locationTypeId, parentLocationId);
    }

    @Override
    public List<LocationType> getAllLocationTypes() throws AppException {
        return locationTypeRepository.findAll();
    }

}
