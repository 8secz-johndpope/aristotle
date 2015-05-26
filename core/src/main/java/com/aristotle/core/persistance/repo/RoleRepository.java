package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aristotle.core.persistance.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role getRoleByName(String name);

    @Query("select distinct role from Role role, User user join user.locationRoles lr where  user.id=?1 and lr.locationId=?2")
    List<Role> getAdminRolesOfUserAndLocation(Long userId, Long locationId);

	/*
	public abstract List<Role> getUserGlobalRoles(long userId);
	
	public abstract List<Role> getUserStateRoles(long userId, long stateId);
	
	public abstract List<Role> getUserDistrictRoles(long userId, long districtId);
	
	public abstract List<Role> getUserAcRoles(long userId, long acId);
	
	public abstract List<Role> getUserPcRoles(long userId, long pcId);
	
	public abstract List<Role> getUserCountryRoles(long userId, long countryId);
	
	public abstract List<Role> getUserCountryRegionRoles(long userId, long countryRegionId);
	
	public abstract List<Role> getUserCountryRegionAreaRoles(long userId, long countryRegionAreaId);
	
	public abstract List<Role> getGlobalRoles();
	
	public abstract List<Role> getStateRoles(long stateId);
	
	public abstract List<Role> getDistrictRoles(long districtId);
	
	public abstract List<Role> getAcRoles(long acId);
	
	public abstract List<Role> getPcRoles(long pcId);

	public abstract List<Role> getCountryRoles(long countryId);
	
	public abstract List<Role> getCountryRegionRoles(long countryRegionId);
	
	public abstract List<Role> getCountryRegionAreaRoles(long countryRegionAreaId);
	*/
}
