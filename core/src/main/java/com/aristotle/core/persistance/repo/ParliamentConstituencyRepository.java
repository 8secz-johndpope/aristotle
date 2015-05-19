package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.ParliamentConstituency;

public interface ParliamentConstituencyRepository extends JpaRepository<ParliamentConstituency, Long> {

    public abstract List<ParliamentConstituency> getParliamentConstituencyByStateId(long stateId);
	
    // public abstract ParliamentConstituency getParliamentConstituencyByNameAndStateId(long districtId, String urlName);

}