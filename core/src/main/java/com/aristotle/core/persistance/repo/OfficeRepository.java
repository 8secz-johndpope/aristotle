package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aristotle.core.persistance.Office;

public interface OfficeRepository extends JpaRepository<Office, Long> {
    
    public abstract List<Office> getOfficeByLocationId(Long locationId);

    @Query("select office from Office office where office.national=true")
    public abstract List<Office> getNationalOffices();
    /*
	public abstract List<Office> getNationalOffices();
	
	public abstract List<Office> getStateOffices(Long stateId);
	
	public abstract List<Office> getDistrictOffices(Long districtId);
	
	public abstract List<Office> getAcOffices(Long acId);
	
	public abstract List<Office> getPcOffices(Long pcId);
	
	public abstract List<Office> getCountryOffices(Long countryId);
	
	public abstract List<Office> getCountryRegionOffices(Long countryRegionId);
	
	public abstract List<Office> getCountryRegionAreaOffices(Long countryRegionAreaId);

    */
}