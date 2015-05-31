package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.LocationRole;

public interface LocationRoleRepository extends JpaRepository<LocationRole, Long> {

    public abstract LocationRole getLocationRoleByLocationIdAndRoleId(Long locationId, Long roleId);

}