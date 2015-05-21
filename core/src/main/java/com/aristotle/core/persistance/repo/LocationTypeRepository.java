package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.LocationType;

public interface LocationTypeRepository extends JpaRepository<LocationType, Long> {

    LocationType getLocationTypeByName(String name);
}
