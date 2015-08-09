package com.aristotle.core.service.temp;

import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aristotle.core.enums.AppPermission;
import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.AssemblyConstituency;
import com.aristotle.core.persistance.Country;
import com.aristotle.core.persistance.CountryRegion;
import com.aristotle.core.persistance.CountryRegionArea;
import com.aristotle.core.persistance.District;
import com.aristotle.core.persistance.Location;
import com.aristotle.core.persistance.LocationType;
import com.aristotle.core.persistance.News;
import com.aristotle.core.persistance.ParliamentConstituency;
import com.aristotle.core.persistance.Permission;
import com.aristotle.core.persistance.Role;
import com.aristotle.core.persistance.State;
import com.aristotle.core.persistance.User;
import com.aristotle.core.persistance.UserLocation;
import com.aristotle.core.persistance.repo.AssemblyConstituencyRepository;
import com.aristotle.core.persistance.repo.CountryRegionAreaRepository;
import com.aristotle.core.persistance.repo.CountryRegionRepository;
import com.aristotle.core.persistance.repo.CountryRepository;
import com.aristotle.core.persistance.repo.DistrictRepository;
import com.aristotle.core.persistance.repo.EventRepository;
import com.aristotle.core.persistance.repo.LocationRepository;
import com.aristotle.core.persistance.repo.LocationTypeRepository;
import com.aristotle.core.persistance.repo.NewsRepository;
import com.aristotle.core.persistance.repo.ParliamentConstituencyRepository;
import com.aristotle.core.persistance.repo.PermissionRepository;
import com.aristotle.core.persistance.repo.RoleRepository;
import com.aristotle.core.persistance.repo.StateRepository;
import com.aristotle.core.persistance.repo.UserLocationRepository;
import com.aristotle.core.persistance.repo.UserRepository;

@Service
@Transactional
public class LocationUpgradeServiceImpl implements LocationUpgradeService {

    private final String COUNTRY = "Country";
    private final String COUNTRY_REGION = "CountryRegion";
    private final String COUNTRY_REGION_AREA = "CountryRegionArea";
    private final String STATE = "State";
    private final String DISTRICT = "District";
    private final String AC = "AssemblyConstituency";
    private final String PC = "ParliamentConstituency";

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CountryRepository countryRepository;
    
    @Autowired
    private CountryRegionRepository countryRegionRepository;

    @Autowired
    private CountryRegionAreaRepository countryRegionAreaRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private AssemblyConstituencyRepository assemblyConstituencyRepository;

    @Autowired
    private ParliamentConstituencyRepository parliamentConstituencyRepository;

    @Autowired
    private LocationTypeRepository locationTypeRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserLocationRepository userLocationRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private EventRepository eventRepository;

    @Override
    public void copyCountries() {
        LocationType countryLocationType = getOrCreateLocationType(COUNTRY, null);
        List<Country> allCountries = countryRepository.findAll();


        for (Country oneCountry : allCountries) {
            Location oneLocation = new Location();
            oneLocation.setLocationType(countryLocationType);
            oneLocation.setName(oneCountry.getName());
            oneLocation.setNameUp(oneCountry.getName().toUpperCase());
            oneLocation.setIsdCode(oneCountry.getIsdCode());
            oneLocation = locationRepository.save(oneLocation);
            oneCountry.setLocation(oneLocation);
        }

    }

    private LocationType getOrCreateLocationType(String locationTypeName, LocationType parentLocationType) {
        LocationType locationType = locationTypeRepository.getLocationTypeByName(locationTypeName);
        if (locationType != null) {
            return locationType;
        }
        locationType = new LocationType();
        locationType.setName(locationTypeName);
        locationType.setParentLocationType(parentLocationType);
        locationType = locationTypeRepository.save(locationType);
        return locationType;
    }

    @Override
    public void copyCountryRegions() {
        LocationType countryLocationType = getOrCreateLocationType(COUNTRY, null);
        LocationType countryRegionLocationType = getOrCreateLocationType(COUNTRY_REGION, countryLocationType);

        List<CountryRegion> allCountryRegion = countryRegionRepository.findAll();

        for (CountryRegion oneCountryRegion : allCountryRegion) {
            Location oneLocation = new Location();
            oneLocation.setLocationType(countryRegionLocationType);
            oneLocation.setName(oneCountryRegion.getName());
            oneLocation.setNameUp(oneCountryRegion.getName().toUpperCase());
            oneLocation.setParentLocation(oneCountryRegion.getCountry().getLocation());
            oneLocation = locationRepository.save(oneLocation);
            oneCountryRegion.setLocation(oneLocation);
        }

    }

    @Override
    public void copyCountryRegionAreas() {
        LocationType countryLocationType = getOrCreateLocationType(COUNTRY, null);
        LocationType countryRegionLocationType = getOrCreateLocationType(COUNTRY_REGION, countryLocationType);
        LocationType countryRegionAreaLocationType = getOrCreateLocationType(COUNTRY_REGION_AREA, countryRegionLocationType);

        List<CountryRegionArea> allCountryRegionAreas = countryRegionAreaRepository.findAll();

        for (CountryRegionArea oneCountryRegionArea : allCountryRegionAreas) {
            Location oneLocation = new Location();
            oneLocation.setLocationType(countryRegionAreaLocationType);
            oneLocation.setName(oneCountryRegionArea.getName());
            oneLocation.setNameUp(oneCountryRegionArea.getName().toUpperCase());
            oneLocation.setParentLocation(oneCountryRegionArea.getCountryRegion().getLocation());
            oneLocation = locationRepository.save(oneLocation);
            oneCountryRegionArea.setLocation(oneLocation);
        }
    }

    @Override
    public void copyStates() {
        LocationType countryLocationType = getOrCreateLocationType(COUNTRY, null);
        LocationType stateLocationType = getOrCreateLocationType(STATE, countryLocationType);

        Location india = locationRepository.getLocationByNameUpAndLocationTypeId("INDIA", countryLocationType.getId());

        List<State> allStates = stateRepository.findAll();

        for (State oneState : allStates) {
            Location oneLocation = new Location();
            oneLocation.setLocationType(stateLocationType);
            oneLocation.setName(oneState.getName());
            oneLocation.setNameUp(oneState.getName().toUpperCase());
            oneLocation.setParentLocation(india);
            oneLocation = locationRepository.save(oneLocation);
            oneState.setLocation(oneLocation);
        }
    }

    @Override
    public void copyDistricts() {
        LocationType countryLocationType = getOrCreateLocationType(COUNTRY, null);
        LocationType stateLocationType = getOrCreateLocationType(STATE, countryLocationType);
        LocationType districtLocationType = getOrCreateLocationType(DISTRICT, stateLocationType);

        List<District> allDistricts = districtRepository.findAll();

        for (District oneDistrict : allDistricts) {
            Location oneLocation = new Location();
            oneLocation.setLocationType(districtLocationType);
            oneLocation.setName(oneDistrict.getName());
            oneLocation.setNameUp(oneDistrict.getName().toUpperCase());
            oneLocation.setParentLocation(oneDistrict.getState().getLocation());
            oneLocation = locationRepository.save(oneLocation);
            oneDistrict.setLocation(oneLocation);
        }
    }

    @Override
    public void copyAcs() {
        LocationType countryLocationType = getOrCreateLocationType(COUNTRY, null);
        LocationType stateLocationType = getOrCreateLocationType(STATE, countryLocationType);
        LocationType districtLocationType = getOrCreateLocationType(DISTRICT, stateLocationType);
        LocationType acLocationType = getOrCreateLocationType(AC, districtLocationType);

        List<AssemblyConstituency> allAcs = assemblyConstituencyRepository.findAll();

        for (AssemblyConstituency oneAc : allAcs) {
            Location oneLocation = new Location();
            oneLocation.setLocationType(acLocationType);
            oneLocation.setName(oneAc.getName());
            oneLocation.setNameUp(oneAc.getName().toUpperCase());
            oneLocation.setParentLocation(oneAc.getDistrict().getLocation());
            oneLocation = locationRepository.save(oneLocation);
            oneAc.setLocation(oneLocation);
        }

    }

    @Override
    public void copyPcs() {
        LocationType countryLocationType = getOrCreateLocationType(COUNTRY, null);
        LocationType stateLocationType = getOrCreateLocationType(STATE, countryLocationType);
        LocationType pcLocationType = getOrCreateLocationType(PC, stateLocationType);

        List<ParliamentConstituency> allPcs = parliamentConstituencyRepository.findAll();

        for (ParliamentConstituency onePc : allPcs) {
            Location oneLocation = new Location();
            oneLocation.setLocationType(pcLocationType);
            oneLocation.setName(onePc.getName());
            oneLocation.setNameUp(onePc.getName().toUpperCase());
            oneLocation.setParentLocation(onePc.getState().getLocation());
            oneLocation = locationRepository.save(oneLocation);
            onePc.setLocation(oneLocation);
        }
    }

    @Override
    public void copyUserRoles() {
        // TODO Auto-generated method stub

    }

    @Override
    public void copyUserLocations() throws Exception {
        List<User> users = userRepository.findAll();
        for (User oneuser : users) {

            addLocationsTouser(oneuser, oneuser.getAssemblyConstituencyLiving().getLocationId(), "Living");
            addLocationsTouser(oneuser, oneuser.getAssemblyConstituencyVoting().getLocationId(), "Voting");
            addLocationsTouser(oneuser, oneuser.getDistrictLiving().getLocationId(), "Living");
            addLocationsTouser(oneuser, oneuser.getDistrictLiving().getLocationId(), "Voting");
            addLocationsTouser(oneuser, oneuser.getNriCountry().getLocationId(), "Living");
            addLocationsTouser(oneuser, oneuser.getNriCountryRegionArea().getLocationId(), "Living");
            addLocationsTouser(oneuser, oneuser.getNriCountryRegion().getLocationId(), "Living");
            addLocationsTouser(oneuser, oneuser.getParliamentConstituencyLiving().getLocationId(), "Living");
            addLocationsTouser(oneuser, oneuser.getParliamentConstituencyVoting().getLocationId(), "Voting");
            addLocationsTouser(oneuser, oneuser.getStateLiving().getLocationId(), "Living");
            addLocationsTouser(oneuser, oneuser.getStateVoting().getLocationId(), "Voting");
        }
    }

    private void addLocationsTouser(User oneuser, Long locationId, String type) throws AppException {
        if (locationId == null) {
            return;
        }
        UserLocation userLocation = new UserLocation();
        userLocation.setUser(oneuser);
        Location location = locationRepository.findOne(locationId);
        if (location == null) {
            throw new AppException("Invalid Location id [" + locationId + "]");
        }
        userLocation.setLocation(location);
        userLocation.setUserLocationType(type);

        userLocation = userLocationRepository.save(userLocation);
    }

    @Override
    public void copyLocationTypeRoles() throws Exception {
        createRoleWithPermissions("VoiceOfAapFacebookAdminRole", " User of this role will be able to make Facebook post using voice of AAP Application", true, true, false, false, true, true, false,
                AppPermission.ADMIN_VOICE_OF_AAP_FB);
        createRoleWithPermissions("VoiceOfAapTwitterAdminRole", "User of this role will be able to make Twitter post using voice of AAP Application", true, true, false, false, true, true, false,
                AppPermission.ADMIN_VOICE_OF_AAP_TWITTER); // News Related Roles
        createRoleWithPermissions("NewsAdminRole", "User of this role will be able to create/update/Approve/delete news for a location", true, true, true, true, true, true, false,
                AppPermission.CREATE_NEWS, AppPermission.UPDATE_NEWS, AppPermission.DELETE_NEWS, AppPermission.APPROVE_NEWS);

        createRoleWithPermissions("NewsReporterRole", "User of this role will be able to create/update news for a location", true, true, true, true, true, true, false, AppPermission.CREATE_NEWS,
                AppPermission.UPDATE_NEWS);

        createRoleWithPermissions("NewsEditorRole", "User of this role will be able to create/update/approve and publish news for a location", true, true, true, true, true, true, false,
                AppPermission.CREATE_NEWS, AppPermission.UPDATE_NEWS, AppPermission.APPROVE_NEWS);

        createRoleWithPermissions("NewsApproverRole", "User of this role will be able to approve/publish existing news for a location", true, true, true, true, true, true, false,
                AppPermission.APPROVE_NEWS);

        createRoleWithPermissions("GlobalMemberAdminRole", "User of this role will be able to add new member at any location and will be able to update any member", false, false, false, false, true,
                true, false, AppPermission.ADD_MEMBER, AppPermission.UPDATE_GLOBAL_MEMBER, AppPermission.VIEW_MEMBER);

        createRoleWithPermissions("MemberAdminRole", "User of this role will be able to add new member at any location and will be able to update member at his location only", true, true, true, true,
                true, true, false, AppPermission.ADD_MEMBER, AppPermission.UPDATE_MEMBER, AppPermission.VIEW_MEMBER, AppPermission.SEARCH_MEMBER);

        createRoleWithPermissions("MemberSearchRole", "User of this role will be able to search member at his/her location or below it", true, true, true, true,
                true, true, false, AppPermission.SEARCH_MEMBER);

        createRoleWithPermissions("AdminEditUserRoles", "User of this role will be able to add or remove user roles on a location", true, true, true, true, true, true, false,
                AppPermission.EDIT_USER_ROLES);

        createRoleWithPermissions("BlogAdminRole", "User of this role will be able to create/update/Approve/delete blog for a location", true, true, true, true, true, true, false,
                AppPermission.CREATE_BLOG, AppPermission.UPDATE_BLOG, AppPermission.DELETE_BLOG, AppPermission.APPROVE_BLOG);

        createRoleWithPermissions("BlogReporterRole", "User of this role will be able to create/update blog for a location", true, true, true, true, true, true, false, AppPermission.CREATE_BLOG,
                AppPermission.UPDATE_BLOG);

        createRoleWithPermissions("BlogEditorRole", "User of this role will be able to create/update/approve and publish blog for a location", true, true, true, true, true, true, false,
                AppPermission.CREATE_BLOG, AppPermission.UPDATE_BLOG, AppPermission.APPROVE_BLOG);

        createRoleWithPermissions("BlogApproverRole", "User of this role will be able to approve/publish existing blog for a location", true, true, true, true, true, true, false,
                AppPermission.APPROVE_BLOG);

        // Poll
        createRoleWithPermissions("PollAdminRole", "User of this role will be able to create/update/Approve/delete poll for a location", true, true, true, true, true, true, false,
                AppPermission.CREATE_POLL, AppPermission.UPDATE_POLL, AppPermission.DELETE_POLL, AppPermission.APPROVE_POLL);

        createRoleWithPermissions("PollReporterRole", "User of this role will be able to create/update poll for a location", true, true, true, true, true, true, false, AppPermission.CREATE_POLL,
                AppPermission.UPDATE_POLL);

        createRoleWithPermissions("PollEditorRole", "User of this role will be able to create/update/approve and publish poll for a location", true, true, true, true, true, true, false,
                AppPermission.CREATE_POLL, AppPermission.UPDATE_POLL, AppPermission.APPROVE_POLL);

        createRoleWithPermissions("PollApproverRole", "User of this role will be able to approve/publish existing poll for a location", true, true, true, true, true, true, false,
                AppPermission.APPROVE_POLL);

        createRoleWithPermissions("Treasury", "User of this role will be able to do all Treasury operation of a location", true, true, true, true, false, false, false, AppPermission.TREASURY);
        createRoleWithPermissions("OfficeAdmin", "User of this role will be able to do all Office related operation of a location, i.e. editing Office address,contact information etc", true, true,
                true, true, true, true, false, AppPermission.EDIT_OFFICE_ADDRESS);

        createRoleWithPermissions("SmsSender", "User of this role will be able to send SMS to all people in his/her location", true, true, true, true, true, true, false, AppPermission.ADMIN_SMS);
        createRoleWithPermissions("EmailSender", "User of this role will be able to send EMAIL to all people in his/her location", true, true, true, true, true, true, false, AppPermission.ADMIN_EMAIL);
        createRoleWithPermissions("GlobalDonationCampaigner", "User of this role will be able to create global donation campaign", false, false, false, false, false, false, false,
                AppPermission.ADMIN_GLOBAL_CAMPAIGN);
        createRoleWithPermissions("EventManager", "User of this role will be able to create events at various level", true, true, true, true, true, true, false, AppPermission.ADMIN_EVENT);

        createRoleWithPermissions("CandidateCampaigner", "User of this role will be able to create/update Candidate Profile", false, false, false, false, false, false, false,
                AppPermission.ADMIN_CANDIDATE_PC);

        createRoleWithPermissions("Web Developer", "User of this role will be able to edit template for website ", true, true, true, true, true, true, true, AppPermission.WEB_ADMIN_DRAFT);
        createRoleWithPermissions("Web Developer Admin", "User of this role will be able to edit template for website and publish them", true, true, true, true, true, true, true,
                AppPermission.WEB_ADMIN);
        createRoleWithPermissions("Team Admin", "User of this role will be able to edit Various Teams", true, true, true, true, true, true, true, AppPermission.EDIT_TEAM);
        createRoleWithPermissions("Call Campaign Admin", "User of this role will be able to edit Various Call Campaigns", true, true, true, true, true, true, true, AppPermission.CALL_CAMPAIGN_ADMIN);
        logger.info("All Roles and permissions are created");

    }

    private void createRoleWithPermissions(String name, String description, boolean addStateRoles, boolean addDistrictRoles, boolean addAcRoles, boolean addPcRoles, boolean addCountryRole,
            boolean addCountryRegionRole, boolean addCuntryRegionAreaRole, AppPermission... appPermissions) {
        logger.info("Creating Role " + name);

        Role role = roleRepository.getRoleByName(name);
        if (role == null) {
            role = new Role();
            role.setName(name);
            role.setDescription(description);
            role = roleRepository.save(role);
        }
        if (role.getPermissions() == null) {
            role.setPermissions(new HashSet<Permission>());
        }
        Permission onePermission;
        for (AppPermission oneAppPermission : appPermissions) {
            onePermission = permissionRepository.getPermissionByPermission(oneAppPermission);
            if (onePermission == null) {
                onePermission = new Permission();
                onePermission.setPermission(oneAppPermission);
                onePermission = permissionRepository.save(onePermission);
            }
            role.getPermissions().add(onePermission);
        }
        if (addStateRoles) {
            addRoleToLocationType(STATE, role);
        }

        if (addDistrictRoles) {
            addRoleToLocationType(DISTRICT, role);
        }

        if (addAcRoles) {
            addRoleToLocationType(AC, role);
        }

        if (addPcRoles) {
            addRoleToLocationType(PC, role);
        }
        if (addCountryRole) {
            addRoleToLocationType(COUNTRY, role);
        }

        if (addCountryRegionRole) {
            addRoleToLocationType(COUNTRY_REGION, role);
        }

    }

    private void addRoleToLocationType(String locationTypeName, Role role) {
        LocationType locationType = locationTypeRepository.getLocationTypeByName(locationTypeName);
        if (locationType.getRoles() == null) {
            locationType.setRoles(new HashSet<Role>());
        }
        locationType.getRoles().add(role);
    }

    @Override
    public void copyNewsLocations() throws Exception {
        List<News> newsList = newsRepository.findAll();
        for (News oneNews : newsList) {
        }
        
    }

    private void addLocationsToNews(News oneNews, Long locationId, String type) throws AppException {
        if (locationId == null) {
            return;
        }
        Location location = locationRepository.findOne(locationId);
        if (location == null) {
            throw new AppException("Invalid Location id [" + locationId + "]");
        }
        if (oneNews.getLocations() == null) {
            oneNews.setLocations(new HashSet<Location>());
        }
        oneNews.getLocations().add(location);
    }

    @Override
    public void copyEventLocations() throws Exception {
        // TODO Auto-generated method stub

    }

}
