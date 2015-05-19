package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.CountryRegionRole;

public interface CountryRegionRoleRepository extends JpaRepository<CountryRegionRole, Long> {

	public abstract CountryRegionRole getCountryRegionRoleById(Long id);
	
	public abstract CountryRegionRole getCountryRegionRoleByCountryRegionIdAndRoleId(Long countryRegionId, Long roleId);
	
}
