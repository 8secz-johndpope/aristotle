package com.aristotle.admin.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aristotle.core.enums.AppPermission;
import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Location;
import com.aristotle.core.persistance.User;

public interface AdminService {

    User login(String userName, String password) throws AppException;

    List<Location> getUserAdminLocations(Long userId) throws AppException;

    Set<AppPermission> getGlobalPermissionsOfUser(Long userId) throws AppException;

    Map<Long, Set<AppPermission>> getLocationPermissionsOfUser(Long userId) throws AppException;
}
