package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.CountryRole;

public interface CountryRoleRepository extends JpaRepository<CountryRole, Long> {

    public CountryRole getCountryRoleByCountryIdAndRoleId(Long countryId, Long roleId);
	
}
