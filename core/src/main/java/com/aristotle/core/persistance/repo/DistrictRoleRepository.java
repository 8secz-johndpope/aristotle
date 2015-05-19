package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.DistrictRole;

public interface DistrictRoleRepository extends JpaRepository<DistrictRole, Long> {

	public abstract DistrictRole getDistrictRoleByDistrictIdAndRoleId(Long districtid,Long roleId);
	
}
