package com.aristotle.admin.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aristotle.core.enums.AppPermission;
import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Location;
import com.aristotle.core.persistance.Role;
import com.aristotle.core.persistance.StaticDataPlugin;
import com.aristotle.core.persistance.User;

public interface AdminService {

    User login(String userName, String password) throws AppException;

    List<Location> getUserAdminLocations(Long userId) throws AppException;

    Set<AppPermission> getGlobalPermissionsOfUser(Long userId) throws AppException;

    Map<Long, Set<AppPermission>> getLocationPermissionsOfUser(Long userId) throws AppException;

    List<StaticDataPlugin> getAllStaticDataPlugin() throws AppException;

    StaticDataPlugin saveStaticDataPlugin(StaticDataPlugin staticDataPlugin) throws AppException;

    void addRolesToUserAtLocation(Collection<Role> roles, Long userId, Location location) throws AppException;

    List<Role> getLocationRoles(Location location) throws AppException;

    List<Role> getUserRoles(Long userId, Location location) throws AppException;

}
