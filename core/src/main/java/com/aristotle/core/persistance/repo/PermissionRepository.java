package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.enums.AppPermission;
import com.aristotle.core.persistance.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    public Permission getPermissionByPermission(AppPermission name);
	
}
