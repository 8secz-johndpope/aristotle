package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.CountryRegion;

public interface CountryRegionRepository extends JpaRepository<CountryRegion, Long> {

	public abstract CountryRegion getCountryRegionByNameAndCountryId(Long countryId,String countryRegion);
	
	public abstract List<CountryRegion> getCountryRegionsByCountryId(Long countryId);
	
}