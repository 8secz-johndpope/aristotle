package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.StateRole;

public interface StateRoleRepository extends JpaRepository<StateRole, Long> {

    public StateRole getStateRoleByStateIdAndRoleId(Long stateId, Long roleId);
	
}
