package com.aristotle.core.service;

import java.util.ArrayList;
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
            return locationRepository.getLocationsByLocationTypeIdOrderByName(locationTypeId);
        }
        return locationRepository.getLocationsByLocationTypeIdAndParentLocationIdOrderByName(locationTypeId, parentLocationId);
    }

    @Override
    public List<LocationType> getAllLocationTypes() throws AppException {
        return locationTypeRepository.findAll();
    }

    @Override
    public List<Location> getAllCountries() throws AppException {
        LocationType locationType = locationTypeRepository.getLocationTypeByName("Country");
        return locationRepository.getLocationsByLocationTypeIdOrderByName(locationType.getId());
    }

    @Override
    public List<Location> getAllStates() throws AppException {
        LocationType locationType = locationTypeRepository.getLocationTypeByName("State");
        return locationRepository.getLocationsByLocationTypeIdOrderByName(locationType.getId());
    }

    @Override
    public List<Location> getAllParliamentConstituenciesOfState(Long stateId) throws AppException {
        LocationType locationType = locationTypeRepository.getLocationTypeByName("ParliamentConstituency");
        return locationRepository.getLocationsByLocationTypeIdAndParentLocationIdOrderByName(locationType.getId(), stateId);
    }

    @Override
    public List<Location> getAllDistrictOfState(Long stateId) throws AppException {
        LocationType locationType = locationTypeRepository.getLocationTypeByName("District");
        return locationRepository.getLocationsByLocationTypeIdAndParentLocationIdOrderByName(locationType.getId(), stateId);
    }

    @Override
    public List<Location> getAllAssemblyConstituenciesOfDistrict(Long districtId) throws AppException {
        LocationType locationType = locationTypeRepository.getLocationTypeByName("AssemblyConstituency");
        return locationRepository.getLocationsByLocationTypeIdAndParentLocationIdOrderByName(locationType.getId(), districtId);
    }

    @Override
    public List<Location> getAllChildLocations(Long locationId) throws AppException {
        return locationRepository.getLocationsByParentLocationId(locationId);
    }

    @Override
    public List<LocationType> getAllLocationUnderLocationType(Long locationTypeId) throws AppException {
        LocationType locationType = locationTypeRepository.findOne(locationTypeId);
        List<LocationType> locationTypes = new ArrayList<LocationType>();
        locationTypes.add(locationType);
        addChildLocationTypes(locationType, locationTypes);
        return locationTypes;
    }

    private void addChildLocationTypes(LocationType locationType, List<LocationType> locationTypes) {
        if (locationType.getChildLocationTypes() == null || locationType.getChildLocationTypes().isEmpty()) {
            return;
        }
        for (LocationType oneChilLocationType : locationType.getChildLocationTypes()) {
            locationTypes.add(oneChilLocationType);
            addChildLocationTypes(oneChilLocationType, locationTypes);
        }
    }

    @Override
    public Location findLocationById(Long locationId) throws AppException {
        return locationRepository.findOne(locationId);
    }

}
