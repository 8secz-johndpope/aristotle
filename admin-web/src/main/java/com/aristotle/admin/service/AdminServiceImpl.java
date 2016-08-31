package com.aristotle.admin.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aristotle.core.enums.AppPermission;
import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Location;
import com.aristotle.core.persistance.LocationRole;
import com.aristotle.core.persistance.LocationType;
import com.aristotle.core.persistance.LoginAccount;
import com.aristotle.core.persistance.Permission;
import com.aristotle.core.persistance.Role;
import com.aristotle.core.persistance.StaticDataPlugin;
import com.aristotle.core.persistance.User;
import com.aristotle.core.persistance.repo.LocationRepository;
import com.aristotle.core.persistance.repo.LocationRoleRepository;
import com.aristotle.core.persistance.repo.LoginAccountRepository;
import com.aristotle.core.persistance.repo.RoleRepository;
import com.aristotle.core.persistance.repo.StaticDataPluginRepository;
import com.aristotle.core.persistance.repo.UserLocationRepository;
import com.aristotle.core.persistance.repo.UserRepository;
import com.aristotle.core.service.PasswordUtil;

@Service
@Transactional(rollbackFor={Throwable.class})
public class AdminServiceImpl implements AdminService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private LoginAccountRepository loginAccountRepository;
    @Autowired
    private PasswordUtil passwordUtil;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StaticDataPluginRepository staticDataPluginRepository;
    @Autowired
    private UserLocationRepository userLocationRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private LocationRoleRepository locationRoleRepository;

    @Override
    public User login(String userName, String password) throws AppException {
        LoginAccount loginAccount = loginAccountRepository.getLoginAccountByUserName(userName.toLowerCase());
        if (loginAccount == null) {
            throw new AppException("Invalid User Credentials");
        }
        boolean passwordOk = passwordUtil.checkPassword(password, loginAccount.getPassword());
        if (!passwordOk) {
            throw new AppException("Invalid User credentials");
        }
        User user = loginAccount.getUser();
        return user;
    }

    @Override
    public List<Location> getUserAdminLocations(Long userId) {
        return locationRepository.getAdminLocationsOfUser(userId);
    }

    @Override
    public Set<AppPermission> getGlobalPermissionsOfUser(Long userId) throws AppException {
        User user = userRepository.findOne(userId);
        Set<Role> allUserRolesAtWorldLevel = user.getAllRoles();
        logger.info("allUserRolesAtWorldLevel=" + allUserRolesAtWorldLevel);
        Set<AppPermission> appPermissions = new HashSet<AppPermission>();
        if (allUserRolesAtWorldLevel != null && !allUserRolesAtWorldLevel.isEmpty()) {
            for (Role oneRole : allUserRolesAtWorldLevel) {
                if (!oneRole.getPermissions().isEmpty()) {
                    appPermissions.addAll(convertPermissionToAppPermission(oneRole.getPermissions()));
                }
            }
        }
        return appPermissions;
    }

    @Override
    public Map<Long, Set<AppPermission>> getLocationPermissionsOfUser(Long userId) throws AppException {
        User user = userRepository.findOne(userId);
        Set<LocationRole> locationRoles = user.getLocationRoles();
        logger.info("locationRoles= {}", locationRoles);

        Map<Long, Set<AppPermission>> map = new HashMap<Long, Set<AppPermission>>();
        if (locationRoles != null && !locationRoles.isEmpty()) {
            for (LocationRole oneLocationRole : locationRoles) {
                if (!oneLocationRole.getRole().getPermissions().isEmpty()) {
                    Set<AppPermission> permissions = convertPermissionToAppPermission(oneLocationRole.getRole().getPermissions());
                    if (map.get(oneLocationRole.getLocation().getId()) == null) {
                        map.put(oneLocationRole.getLocation().getId(), permissions);
                    } else {
                        map.get(oneLocationRole.getLocation().getId()).addAll(permissions);
                    }
                }
            }
        }

        return map;
    }

    private Set<AppPermission> convertPermissionToAppPermission(Set<Permission> permissions) {
        Set<AppPermission> returnPermissions = new HashSet<>();
        if (permissions != null) {
            for (Permission onePermission : permissions) {
                returnPermissions.add(onePermission.getPermission());
            }
        }

        return returnPermissions;
    }

    @Override
    public List<StaticDataPlugin> getAllStaticDataPlugin() throws AppException {
        return staticDataPluginRepository.findAll();
    }

    @Override
    public StaticDataPlugin saveStaticDataPlugin(StaticDataPlugin staticDataPlugin) throws AppException {
        return staticDataPluginRepository.save(staticDataPlugin);
    }

    @Override
    public void addRolesToUserAtLocation(Collection<Role> roles, Long userId, Location location) throws AppException {
        User user = userRepository.findOne(userId);
        if (location == null || location.getId() <= 0) {
            if (user.getAllRoles() == null) {
                user.setAllRoles(new HashSet<Role>());
            }
            // clear all roles
            user.getAllRoles().clear();
            for (Role oneRole : roles) {
                oneRole = roleRepository.findOne(oneRole.getId());
                user.getAllRoles().add(oneRole);
            }
        } else {
            location = locationRepository.findOne(location.getId());
            System.out.println("Adding Roles for Location " + location);
            if (user.getLocationRoles() == null) {
                user.setLocationRoles(new HashSet<LocationRole>());
            }
            // clear existing Location Roles
            user.getLocationRoles().clear();
            for (Role oneRole : roles) {
                LocationRole oneLocationRole = locationRoleRepository.getLocationRoleByLocationIdAndRoleId(location.getId(), oneRole.getId());
                if (oneLocationRole == null) {
                    oneRole = roleRepository.findOne(oneRole.getId());
                    oneLocationRole = new LocationRole();
                    oneLocationRole.setRole(oneRole);
                    oneLocationRole.setLocation(location);
                    oneLocationRole = locationRoleRepository.save(oneLocationRole);
                }
                user.getLocationRoles().add(oneLocationRole);
            }
        }

    }

    @Override
    public List<Role> getLocationRoles(Location location) throws AppException {
        if (location == null) {
            return roleRepository.findAll();
        }
        location = locationRepository.findOne(location.getId());
        LocationType locationType = location.getLocationType();
        return new ArrayList<Role>(locationType.getRoles());
    }

    @Override
    public List<Role> getUserRoles(Long userId, Location location) throws AppException {
        if (location == null) {
            User user = userRepository.findOne(userId);
            return new ArrayList<Role>(user.getAllRoles());
        }
        return roleRepository.getAdminRolesOfUserAndLocation(userId, location.getId());
    }

}
