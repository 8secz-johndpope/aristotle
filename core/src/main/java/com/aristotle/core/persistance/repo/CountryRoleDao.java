package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.CountryRole;

public interface CountryRoleDao extends JpaRepository<CountryRole, Long> {

	public abstract CountryRole saveCountryRole(CountryRole countryRole);
	
	public abstract void deleteCountryRole(CountryRole countryRole);
	
	public abstract CountryRole getCountryRoleById(Long id);
	
	public abstract CountryRole getCountryRoleByCountryIdAndRoleId(Long countryId, Long roleId);
	
	public abstract List<CountryRole> getAllCountryRoles(int totalItems, int pageNumber);
	
	public abstract List<CountryRole> getAllCountryRoles();
	
	public abstract List<CountryRole> getCountryRoleItemsAfterId(long countryRoleId);

}
