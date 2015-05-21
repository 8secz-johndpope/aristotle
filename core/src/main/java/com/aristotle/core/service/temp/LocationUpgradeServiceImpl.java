package com.aristotle.core.service.temp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aristotle.core.persistance.AssemblyConstituency;
import com.aristotle.core.persistance.Country;
import com.aristotle.core.persistance.CountryRegion;
import com.aristotle.core.persistance.CountryRegionArea;
import com.aristotle.core.persistance.District;
import com.aristotle.core.persistance.Location;
import com.aristotle.core.persistance.LocationType;
import com.aristotle.core.persistance.ParliamentConstituency;
import com.aristotle.core.persistance.State;
import com.aristotle.core.persistance.repo.AssemblyConstituencyRepository;
import com.aristotle.core.persistance.repo.CountryRegionAreaRepository;
import com.aristotle.core.persistance.repo.CountryRegionRepository;
import com.aristotle.core.persistance.repo.CountryRepository;
import com.aristotle.core.persistance.repo.DistrictRepository;
import com.aristotle.core.persistance.repo.LocationRepository;
import com.aristotle.core.persistance.repo.LocationTypeRepository;
import com.aristotle.core.persistance.repo.ParliamentConstituencyRepository;
import com.aristotle.core.persistance.repo.StateRepository;

@Service
@Transactional
public class LocationUpgradeServiceImpl implements LocationUpgradeService {

    private final String COUNTRY = "Country";
    private final String COUNTRY_REGION = "CountryRegion";
    private final String COUNTRY_REGION_AREA = "CountryRegionArea";
    private final String STATE = "State";
    private final String DISTRICT = "District";
    private final String AC = "AssemblyConstituency";
    private final String PC = "ParliamentConstituency";
    @Autowired
    private CountryRepository countryRepository;
    
    @Autowired
    private CountryRegionRepository countryRegionRepository;

    @Autowired
    private CountryRegionAreaRepository countryRegionAreaRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private AssemblyConstituencyRepository assemblyConstituencyRepository;

    @Autowired
    private ParliamentConstituencyRepository parliamentConstituencyRepository;

    @Autowired
    private LocationTypeRepository locationTypeRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public void copyCountries() {
        LocationType countryLocationType = getOrCreateLocationType(COUNTRY, null);
        List<Country> allCountries = countryRepository.findAll();


        for (Country oneCountry : allCountries) {
            Location oneLocation = new Location();
            oneLocation.setLocationType(countryLocationType);
            oneLocation.setName(oneCountry.getName());
            oneLocation.setNameUp(oneCountry.getName().toUpperCase());
            oneLocation.setIsdCode(oneCountry.getIsdCode());
            oneLocation = locationRepository.save(oneLocation);
            oneCountry.setLocation(oneLocation);
        }

    }

    private LocationType getOrCreateLocationType(String locationTypeName, LocationType parentLocationType) {
        LocationType locationType = locationTypeRepository.getLocationTypeByName(locationTypeName);
        if (locationType != null) {
            return locationType;
        }
        locationType = new LocationType();
        locationType.setName(locationTypeName);
        locationType.setParentLocationType(parentLocationType);
        locationType = locationTypeRepository.save(locationType);
        return locationType;
    }

    @Override
    public void copyCountryRegions() {
        LocationType countryLocationType = getOrCreateLocationType(COUNTRY, null);
        LocationType countryRegionLocationType = getOrCreateLocationType(COUNTRY_REGION, countryLocationType);

        List<CountryRegion> allCountryRegion = countryRegionRepository.findAll();

        for (CountryRegion oneCountryRegion : allCountryRegion) {
            Location oneLocation = new Location();
            oneLocation.setLocationType(countryRegionLocationType);
            oneLocation.setName(oneCountryRegion.getName());
            oneLocation.setNameUp(oneCountryRegion.getName().toUpperCase());
            oneLocation.setParentLocation(oneCountryRegion.getCountry().getLocation());
            oneLocation = locationRepository.save(oneLocation);
            oneCountryRegion.setLocation(oneLocation);
        }

    }

    @Override
    public void copyCountryRegionAreas() {
        LocationType countryLocationType = getOrCreateLocationType(COUNTRY, null);
        LocationType countryRegionLocationType = getOrCreateLocationType(COUNTRY_REGION, countryLocationType);
        LocationType countryRegionAreaLocationType = getOrCreateLocationType(COUNTRY_REGION_AREA, countryRegionLocationType);

        List<CountryRegionArea> allCountryRegionAreas = countryRegionAreaRepository.findAll();

        for (CountryRegionArea oneCountryRegionArea : allCountryRegionAreas) {
            Location oneLocation = new Location();
            oneLocation.setLocationType(countryRegionAreaLocationType);
            oneLocation.setName(oneCountryRegionArea.getName());
            oneLocation.setNameUp(oneCountryRegionArea.getName().toUpperCase());
            oneLocation.setParentLocation(oneCountryRegionArea.getCountryRegion().getLocation());
            oneLocation = locationRepository.save(oneLocation);
            oneCountryRegionArea.setLocation(oneLocation);
        }
    }

    @Override
    public void copyStates() {
        LocationType countryLocationType = getOrCreateLocationType(COUNTRY, null);
        LocationType stateLocationType = getOrCreateLocationType(STATE, countryLocationType);

        Location india = locationRepository.getLocationByNameUpAndLocationTypeId("INDIA", countryLocationType.getId());

        List<State> allStates = stateRepository.findAll();

        for (State oneState : allStates) {
            Location oneLocation = new Location();
            oneLocation.setLocationType(stateLocationType);
            oneLocation.setName(oneState.getName());
            oneLocation.setNameUp(oneState.getName().toUpperCase());
            oneLocation.setParentLocation(india);
            oneLocation = locationRepository.save(oneLocation);
            oneState.setLocation(oneLocation);
        }
    }

    @Override
    public void copyDistricts() {
        LocationType countryLocationType = getOrCreateLocationType(COUNTRY, null);
        LocationType stateLocationType = getOrCreateLocationType(STATE, countryLocationType);
        LocationType districtLocationType = getOrCreateLocationType(DISTRICT, stateLocationType);

        List<District> allDistricts = districtRepository.findAll();

        for (District oneDistrict : allDistricts) {
            Location oneLocation = new Location();
            oneLocation.setLocationType(districtLocationType);
            oneLocation.setName(oneDistrict.getName());
            oneLocation.setNameUp(oneDistrict.getName().toUpperCase());
            oneLocation.setParentLocation(oneDistrict.getState().getLocation());
            oneLocation = locationRepository.save(oneLocation);
            oneDistrict.setLocation(oneLocation);
        }
    }

    @Override
    public void copyAcs() {
        LocationType countryLocationType = getOrCreateLocationType(COUNTRY, null);
        LocationType stateLocationType = getOrCreateLocationType(STATE, countryLocationType);
        LocationType districtLocationType = getOrCreateLocationType(DISTRICT, stateLocationType);
        LocationType acLocationType = getOrCreateLocationType(AC, districtLocationType);

        List<AssemblyConstituency> allAcs = assemblyConstituencyRepository.findAll();

        for (AssemblyConstituency oneAc : allAcs) {
            Location oneLocation = new Location();
            oneLocation.setLocationType(acLocationType);
            oneLocation.setName(oneAc.getName());
            oneLocation.setNameUp(oneAc.getName().toUpperCase());
            oneLocation.setParentLocation(oneAc.getDistrict().getLocation());
            oneLocation = locationRepository.save(oneLocation);
            oneAc.setLocation(oneLocation);
        }

    }

    @Override
    public void copyPcs() {
        LocationType countryLocationType = getOrCreateLocationType(COUNTRY, null);
        LocationType stateLocationType = getOrCreateLocationType(STATE, countryLocationType);
        LocationType pcLocationType = getOrCreateLocationType(PC, stateLocationType);

        List<ParliamentConstituency> allPcs = parliamentConstituencyRepository.findAll();

        for (ParliamentConstituency onePc : allPcs) {
            Location oneLocation = new Location();
            oneLocation.setLocationType(pcLocationType);
            oneLocation.setName(onePc.getName());
            oneLocation.setNameUp(onePc.getName().toUpperCase());
            oneLocation.setParentLocation(onePc.getState().getLocation());
            oneLocation = locationRepository.save(oneLocation);
            onePc.setLocation(oneLocation);
        }
    }

    @Override
    public void copyUserRoles() {
        // TODO Auto-generated method stub

    }

    @Override
    public void copyUserLocations() {
        // TODO Auto-generated method stub

    }

}
