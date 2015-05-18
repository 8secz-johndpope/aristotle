package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aristotle.core.persistance.AssemblyConstituency;

public interface AssemblyConstituencyRepository extends JpaRepository<AssemblyConstituency, Long> {

    @Query("select ac from AssemblyConstituency ac where ac.districtId = ?1")
    public List<AssemblyConstituency> getAssemblyConstituencyOfDistrict(long districtId);
	
    @Query("select ac from AssemblyConstituency ac where ac.district.stateId = ?1")
    public List<AssemblyConstituency> getAssemblyConstituencyOfState(long stateId);
	
    @Query("select ac from AssemblyConstituency ac where ac.districtId = ?1 and ac.name = ?2")
    public AssemblyConstituency getAssemblyConstituencyNameAndDistrictId(long districtId, String acName);

}