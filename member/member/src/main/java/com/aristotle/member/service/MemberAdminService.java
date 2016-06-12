package com.aristotle.member.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aristotle.core.enums.AppPermission;
import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Location;

public interface MemberAdminService {

	List<Location> getUserAdminLocations(Long userId) throws AppException;
	
    Set<AppPermission> getGlobalPermissionsOfUser(Long userId) throws AppException;
    
    Map<Long, Set<AppPermission>> getLocationPermissionsOfUser(Long userId) throws AppException;

}
