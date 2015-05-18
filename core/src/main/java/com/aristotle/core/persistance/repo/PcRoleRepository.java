package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.PcRole;

public interface PcRoleRepository extends JpaRepository<PcRole, Long> {

    public abstract PcRole getPcRoleByParliamentConstituencyIdAndRoleId(Long pcId, Long roleId);
	
}
