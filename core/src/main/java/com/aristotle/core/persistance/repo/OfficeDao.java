package com.aristotle.core.persistance.repo;

import java.util.List;

import com.aristotle.core.persistance.Office;

public interface OfficeDao {

	public abstract Office saveOffice(Office office);

	public abstract Office getOfficeById(Long id);

	public abstract List<Office> getNationalOffices();
	
	public abstract List<Office> getStateOffices(Long stateId);
	
	public abstract List<Office> getDistrictOffices(Long districtId);
	
	public abstract List<Office> getAcOffices(Long acId);
	
	public abstract List<Office> getPcOffices(Long pcId);
	
	public abstract List<Office> getCountryOffices(Long countryId);
	
	public abstract List<Office> getCountryRegionOffices(Long countryRegionId);
	
	public abstract List<Office> getCountryRegionAreaOffices(Long countryRegionAreaId);


}