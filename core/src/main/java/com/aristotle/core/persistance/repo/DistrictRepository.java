package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.District;

public interface DistrictRepository extends JpaRepository<District, Long> {


    public List<District> getDistrictByStateId(long stateId);

    public District getDistrictByNameAndStateId(Long stateId, String name);

}