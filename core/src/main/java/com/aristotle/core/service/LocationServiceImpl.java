package com.aristotle.core.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Location;
import com.aristotle.core.persistance.LocationType;
import com.aristotle.core.persistance.repo.LocationRepository;
import com.aristotle.core.persistance.repo.LocationTypeRepository;

@Service
@Transactional(rollbackFor={Throwable.class})
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
            return locationRepository.getLocationsByLocationTypeIdOrderByNameAsc(locationTypeId);
        }
        return locationRepository.getLocationsByLocationTypeIdAndParentLocationIdOrderByNameAsc(locationTypeId, parentLocationId);
    }

    @Override
    public List<LocationType> getAllLocationTypes() throws AppException {
        return locationTypeRepository.findAll();
    }

    @Override
    public List<Location> getAllCountries() throws AppException {
        LocationType locationType = locationTypeRepository.getLocationTypeByName("Country");
        return locationRepository.getLocationsByLocationTypeIdOrderByNameAsc(locationType.getId());
    }

    @Override
    public List<Location> getAllStates() throws AppException {
        LocationType locationType = locationTypeRepository.getLocationTypeByName("State");
        return locationRepository.getLocationsByLocationTypeIdOrderByNameAsc(locationType.getId());
    }

    @Override
    public List<Location> getAllParliamentConstituenciesOfState(Long stateId) throws AppException {
        LocationType locationType = locationTypeRepository.getLocationTypeByName("ParliamentConstituency");
        System.out.println("locationType = "+locationType);
        return locationRepository.getLocationsByLocationTypeIdAndParentLocationIdOrderByNameAsc(locationType.getId(), stateId);
    }

    @Override
    public List<Location> getAllDistrictOfState(Long stateId) throws AppException {
        LocationType locationType = locationTypeRepository.getLocationTypeByName("District");
        return locationRepository.getLocationsByLocationTypeIdAndParentLocationIdOrderByNameAsc(locationType.getId(), stateId);
    }

    @Override
    public List<Location> getAllAssemblyConstituenciesOfDistrict(Long districtId) throws AppException {
        LocationType locationType = locationTypeRepository.getLocationTypeByName("AssemblyConstituency");
        return locationRepository.getLocationsByLocationTypeIdAndParentLocationIdOrderByNameAsc(locationType.getId(), districtId);
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

	@Override
	public LocationType saveLocationType(LocationType locationType) throws AppException {
		return locationTypeRepository.save(locationType);
	}
	
	@Override
	public Location saveLocation(Location location) throws AppException {
		if(location.getLocationType() == null){
			throw new AppException("Location Type can not be null");
		}
		LocationType locationType = locationTypeRepository.findOne(location.getLocationType().getId());
		location.setLocationType(locationType);
		location.setNameUp(location.getName().toUpperCase());
		if(location.getParentLocation() != null){
			Location parentLocation = locationRepository.findOne(location.getParentLocation().getId());
			location.setParentLocation(parentLocation);
		}
		return locationRepository.save(location);
	}

}
