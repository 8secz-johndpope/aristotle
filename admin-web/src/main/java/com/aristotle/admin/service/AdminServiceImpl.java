package com.aristotle.admin.service;

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
import com.aristotle.core.persistance.LoginAccount;
import com.aristotle.core.persistance.Permission;
import com.aristotle.core.persistance.Role;
import com.aristotle.core.persistance.StaticDataPlugin;
import com.aristotle.core.persistance.User;
import com.aristotle.core.persistance.repo.LocationRepository;
import com.aristotle.core.persistance.repo.LoginAccountRepository;
import com.aristotle.core.persistance.repo.StaticDataPluginRepository;
import com.aristotle.core.persistance.repo.UserRepository;
import com.aristotle.core.service.PasswordUtil;

@Service
@Transactional
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
                    if (map.get(oneLocationRole.getId()) == null) {
                        map.put(oneLocationRole.getId(), permissions);
                    } else {
                        map.get(oneLocationRole.getId()).addAll(permissions);
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

}
