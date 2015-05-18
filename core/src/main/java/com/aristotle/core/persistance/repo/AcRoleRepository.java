package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.AcRole;

public interface AcRoleRepository extends JpaRepository<AcRole, Long> {

    public AcRole getAcRoleByAssemblyConstituencyIdAndRoleId(Long acId, Long roleId);

}
