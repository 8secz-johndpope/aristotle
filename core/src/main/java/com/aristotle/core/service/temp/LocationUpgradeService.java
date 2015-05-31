package com.aristotle.core.service.temp;

public interface LocationUpgradeService {

    void copyCountries();

    void copyCountryRegions();

    void copyCountryRegionAreas();

    void copyStates();

    void copyDistricts();

    void copyAcs();

    void copyPcs();

    void copyUserRoles();

    void copyUserLocations() throws Exception;

    void copyLocationTypeRoles() throws Exception;

    void copyNewsLocations() throws Exception;

    void copyEventLocations() throws Exception;

}