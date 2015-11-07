package com.aristotle.admin.jsf.bean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.aristotle.admin.jsf.bean.model.RoleModel;
import com.aristotle.admin.jsf.convertors.LocationConvertor;
import com.aristotle.core.enums.AppPermission;
import com.aristotle.core.enums.PostLocationType;
import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Location;
import com.aristotle.core.persistance.Role;
import com.aristotle.core.persistance.User;
import com.aristotle.core.service.LocationService;
import com.aristotle.core.service.UserService;
import com.aristotle.core.service.dto.SearchUser;
import com.aristotle.core.service.dto.UserSearchResult;
import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLBeanName;
import com.ocpsoft.pretty.faces.annotation.URLMapping;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "view")
@URLMapping(id = "adminEditUserBean", beanName = "adminEditUserBean", pattern = "/admin/roles", viewId = "/admin/admin_edituser.xhtml")
@URLBeanName("adminEditUserBean")
public class AdminEditUserBean extends BaseMultiPermissionAdminJsfBean {

	private static final long serialVersionUID = 1L;

    @Autowired
    private LocationService locationService;
    @Autowired
    private UserService userService;
    @Autowired
    private LocationConvertor locationConvertor;

    private UserSearchResult selectedUserForEditing;
    private SearchUser searchedUser;

    private List<UserSearchResult> userSearchResults;

    private List<Location> stateList;
    private List<Location> districtList;
    private List<Location> assemblyConstituencyList;
    private List<Location> parliamentConstituencyList;
	private boolean enableDistrictCombo = false;
	private boolean enableAssemblyConstituencyCombo = false;
	private boolean enableParliamentConstituencyCombo = false;

    private List<Location> livingStateList;
    private List<Location> livingDistrictList;
    private List<Location> livingAssemblyConstituencyList;
    private List<Location> livingParliamentConstituencyList;
	private boolean enableLivingDistrictCombo = false;
	private boolean enableLivingAssemblyConstituencyCombo = false;
	private boolean enableLivingParliamentConstituencyCombo = false;

	private boolean sameAsLiving;

	private boolean showResult;
	private boolean showSearchPanel;
    private List<Location> countries;
	private String location;

	private Long selectedStateIdForRoles;
	private Long selectedDistrictIdForRoles;
	private Long selectedAcIdForRoles;
	private Long selectedPcIdForRoles;
	private Long selectedCountryIdForRoles;
	private Long selectedCountryRegionIdForRoles;
	private Long selectedCountryRegionAreaIdForRoles;

    private List<SelectItem> memberLocationList;

	private boolean showRolesPanel;
	private boolean disableSaveMemberRoleButton = true;

    private RoleModel userLocationRoles;
    private List<Role> editingUserLocationRoles;

	PostLocationType selectedPostLocationType;
	Long selectedPostLocationId;
    private Location selectedLocationForRole;

	public AdminEditUserBean() {
        super("/admin/edituser", AppPermission.UPDATE_MEMBER, AppPermission.ADD_MEMBER, AppPermission.UPDATE_GLOBAL_MEMBER);
	}

	@URLAction(onPostback = false)
	public void init() throws Exception {
		if (!checkUserAccess()) {
			return;
		}

		if (countries == null || countries.isEmpty()) {
            countries = locationService.getAllCountries();
		}
        memberLocationList = new ArrayList<SelectItem>();

        userSearchResults = new ArrayList<UserSearchResult>();
		showResult = false;
		showSearchPanel = true;

        User loggedInAdminUser = getLoggedInUser(true, buildLoginUrl("/admin/roles"));
		if (stateList == null || stateList.isEmpty()) {
            livingStateList = stateList = locationService.getAllStates();
		}
		if (loggedInAdminUser == null) {
			return;
		}
        searchedUser = new SearchUser();
        /*
		searchedUser.setStateLivingId(loggedInAdminUser.getStateLivingId());
		searchedUser.setDistrictLivingId(loggedInAdminUser.getDistrictLivingId());
		searchedUser.setAssemblyConstituencyLivingId(loggedInAdminUser.getAssemblyConstituencyLivingId());
		searchedUser.setParliamentConstituencyLivingId(loggedInAdminUser.getParliamentConstituencyLivingId());

		searchedUser.setStateVotingId(loggedInAdminUser.getStateVotingId());
		searchedUser.setDistrictVotingId(loggedInAdminUser.getDistrictVotingId());
		searchedUser.setAssemblyConstituencyVotingId(loggedInAdminUser.getAssemblyConstituencyVotingId());
		searchedUser.setParliamentConstituencyVotingId(loggedInAdminUser.getParliamentConstituencyVotingId());
		*/
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 1981);
        // searchedUser.setDateOfBirth(cal.getTime());

		// Copy Logged In user to selectedUserForEditing
        selectedUserForEditing = new UserSearchResult();
        // searchedUser.setDateOfBirth(cal.getTime());

		if (searchedUser.getStateVotingId() != null) {
			enableDistrictCombo = true;
			enableParliamentConstituencyCombo = true;
            parliamentConstituencyList = locationService.getAllParliamentConstituenciesOfState(searchedUser.getStateVotingId());
            districtList = locationService.getAllDistrictOfState(searchedUser.getStateVotingId());
			if (searchedUser.getDistrictVotingId() != null) {
				enableAssemblyConstituencyCombo = true;
                assemblyConstituencyList = locationService.getAllAssemblyConstituenciesOfDistrict(searchedUser.getDistrictVotingId());
			}
		}
		if (searchedUser.getStateLivingId() != null) {

			enableLivingDistrictCombo = true;
			enableLivingParliamentConstituencyCombo = true;
            livingParliamentConstituencyList = locationService.getAllParliamentConstituenciesOfState(searchedUser.getStateLivingId());
            livingDistrictList = locationService.getAllDistrictOfState(searchedUser.getStateLivingId());
			if (searchedUser.getDistrictLivingId() != null) {
				enableLivingAssemblyConstituencyCombo = true;
                livingAssemblyConstituencyList = locationService.getAllAssemblyConstituenciesOfDistrict(searchedUser.getDistrictLivingId());
			}
		}

        List<Location> allLocations = new ArrayList<Location>();
        if (menuBean.isGlobalSelected()) {
            // Also add Global Group
            Location location = new Location();// This Location Do not exists in Database
            location.setId(-1L);
            location.setName("Global");

            SelectItemGroup globalGroup = new SelectItemGroup("Global");
            List<Location> globalOptions = new ArrayList<Location>(1);
            globalOptions.add(location);
            allLocations.add(location);
            SelectItem[] globalSelectItems = convertToSelectItems(globalOptions);
            globalGroup.setSelectItems(globalSelectItems);

            SelectItemGroup stateGroup = new SelectItemGroup("State");
            List<Location> states = locationService.getAllStates();
            allLocations.addAll(states);
            SelectItem[] stateSelectItems = convertToSelectItems(states);
            stateGroup.setSelectItems(stateSelectItems);

            List<Location> countries = locationService.getAllCountries();
            allLocations.addAll(countries);
            SelectItem[] countrySelectItems = convertToSelectItems(countries);
            SelectItemGroup countriesGroup = new SelectItemGroup("Countries");
            countriesGroup.setSelectItems(countrySelectItems);


            memberLocationList.clear();
            memberLocationList.add(globalGroup);
            memberLocationList.add(stateGroup);
            memberLocationList.add(countriesGroup);
        } else {
            List<Location> childLocations = locationService.getAllChildLocations(menuBean.getSelectedLocation().getId());
            Map<String, List<Location>> locationMap = new HashMap<String, List<Location>>();
            for (Location oneLocation : childLocations) {
                List<Location> locations = locationMap.get(oneLocation.getLocationType().getName());
                if (locations == null) {
                    locations = new ArrayList<Location>();
                    locationMap.put(oneLocation.getLocationType().getName(), locations);
                }
                allLocations.add(oneLocation);
                locations.add(oneLocation);
            }

            memberLocationList.clear();

            for (Entry<String, List<Location>> oneEntry : locationMap.entrySet()) {
                SelectItemGroup oneGroup = new SelectItemGroup(oneEntry.getKey());
                SelectItem[] stateSelectItems = convertToSelectItems(oneEntry.getValue());
                oneGroup.setSelectItems(stateSelectItems);
                memberLocationList.add(oneGroup);
            }

        }
        locationConvertor.setLocations(allLocations);
	}

    private SelectItem[] convertToSelectItems(Collection<Location> locations) {
        SelectItem[] stateSelectItems = new SelectItem[locations.size()];
        int count = 0;
        for (Location oneLocation : locations) {
            stateSelectItems[count++] = new SelectItem(oneLocation, oneLocation.getName());
        }
        return stateSelectItems;
    }
	public void cancelSaveMemberRole() {
		showSearchPanel = true;
		resetRolePanel();
	}

    public boolean isMemberUpdateAllowed(UserSearchResult user) {
        User loggedInUser = getLoggedInUser();
        System.out.println("loggedInUser.getId()=" + loggedInUser.getId() + ",user.getId()=" + user.getId());
        return !loggedInUser.getId().equals(user.getId());
	}

    public void saveUserRoles(ActionEvent event) {
		if (isValidInput()) {
            try {
                System.out.println("editingUserLocationRoles=" + editingUserLocationRoles);
                adminService.addRolesToUserAtLocation(editingUserLocationRoles, selectedUserForEditing.getId(), selectedLocationForRole);
                showSearchPanel = true;
                selectedLocationForRole = null;
            } catch (AppException e) {
                sendErrorMessageToJsfScreen(e);
            }

			resetRolePanel();
			sendInfoMessageToJsfScreen("Roles updated succesfully");
		}
	}
	
	public void resetRolePanel(){
		showRolesPanel = false;

		userLocationRoles = null;
		editingUserLocationRoles = null;

		selectedPostLocationType = null;
		selectedPostLocationId = null;
		selectedAcIdForRoles = null;
		selectedDistrictIdForRoles = null;
		selectedPcIdForRoles = null;
		selectedStateIdForRoles = null;
		location = null;
		disableSaveMemberRoleButton = true;
	}

	public void searchMember() {
        try {
            userSearchResults = userService.searchUsers(searchedUser);
            showResult = true;
        } catch (AppException e) {
            sendErrorMessageToJsfScreen(e);
        }

	}

	public void handleStateChange(AjaxBehaviorEvent event) {
		try {
			if (searchedUser.getStateVotingId() == 0 || searchedUser.getStateVotingId() == null) {
				enableDistrictCombo = false;
				enableParliamentConstituencyCombo = false;
				districtList = new ArrayList<>();
				parliamentConstituencyList = new ArrayList<>();
			} else {
                districtList = locationService.getAllDistrictOfState(searchedUser.getStateVotingId());
                parliamentConstituencyList = locationService.getAllParliamentConstituenciesOfState(searchedUser.getStateVotingId());
				enableDistrictCombo = true;
				enableParliamentConstituencyCombo = true;
				enableAssemblyConstituencyCombo = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handleUserVotingStateChange(AjaxBehaviorEvent event) {
		try {
			if (searchedUser.getStateVotingId() == 0 || searchedUser.getStateVotingId() == null) {
				enableDistrictCombo = false;
				enableParliamentConstituencyCombo = false;
				districtList = new ArrayList<>();
				parliamentConstituencyList = new ArrayList<>();
			} else {
                districtList = locationService.getAllDistrictOfState(searchedUser.getStateVotingId());
                parliamentConstituencyList = locationService.getAllParliamentConstituenciesOfState(searchedUser.getStateVotingId());
				enableDistrictCombo = true;
				enableParliamentConstituencyCombo = true;
				enableAssemblyConstituencyCombo = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handleLocationClick() {
		if (location.indexOf("Global-") == 0) {
			// User choose his/her location
            User loggedInUser = getLoggedInUser();
            loadLocationRoles(null, null, loggedInUser.isSuperAdmin());
		} else {
			selectedPostLocationType = null;
			selectedPostLocationId = null;
			showRolesPanel = false;
			selectedAcIdForRoles =  null;
			selectedDistrictIdForRoles = null;
			selectedPcIdForRoles = null;
			selectedStateIdForRoles = null;
			disableSaveMemberRoleButton = true;
			// loadLocationRoles(menuBean.getLocationType(), 0L, false);
		}
	}

	private void loadLocationRoles(PostLocationType selectedLocationType, Long selectedLocationId, boolean getAllRolesOfLocation) {
        try {
            selectedPostLocationType = selectedLocationType;
            selectedPostLocationId = selectedLocationId;
            if ((selectedPostLocationType != null && selectedPostLocationType != PostLocationType.Global) && (selectedLocationId == null || selectedLocationId <= 0)) {
                showRolesPanel = false;
                disableSaveMemberRoleButton = true;
                return;
            }
            showRolesPanel = true;
            disableSaveMemberRoleButton = false;
            // get Roles of LoggedInUserId in this location
            User loggedInuser = getLoggedInUser();
            if (getAllRolesOfLocation) {
                userLocationRoles = new RoleModel(adminService.getLocationRoles(menuBean.getSelectedLocation()));
            } else {
                userLocationRoles = new RoleModel(adminService.getUserRoles(loggedInuser.getId(), menuBean.getSelectedLocation()));
            }

            // get Roles of editing User
            editingUserLocationRoles = adminService.getUserRoles(selectedUserForEditing.getId(), menuBean.getSelectedLocation());
            // and merge them to present to screen
        } catch (Exception ex) {
            sendErrorMessageToJsfScreen(ex);
        }

	}

	public void handleLivingStateChange(AjaxBehaviorEvent event) {
		try {
			if (searchedUser.getStateLivingId() == 0 || searchedUser.getStateLivingId() == null) {
				enableLivingDistrictCombo = false;
				enableLivingParliamentConstituencyCombo = false;
				livingDistrictList = new ArrayList<>();
			} else {
                livingParliamentConstituencyList = locationService.getAllParliamentConstituenciesOfState(searchedUser.getStateLivingId());
                livingDistrictList = locationService.getAllDistrictOfState(searchedUser.getStateLivingId());
				enableLivingParliamentConstituencyCombo = true;
				enableLivingDistrictCombo = true;
				enableLivingAssemblyConstituencyCombo = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	public void handleUserLivingStateChange(AjaxBehaviorEvent event) {
		try {
			if (selectedUserForEditing.getStateLivingId() == 0 || selectedUserForEditing.getStateLivingId() == null) {
				enableLivingDistrictCombo = false;
				enableLivingParliamentConstituencyCombo = false;
				livingDistrictList = new ArrayList<>();
			} else {
                livingParliamentConstituencyList = locationService.getAllParliamentConstituenciesOfState(selectedUserForEditing.getStateLivingId());
                livingDistrictList = locationService.getAllDistrictOfState(selectedUserForEditing.getStateLivingId());
				enableLivingParliamentConstituencyCombo = true;
				enableLivingDistrictCombo = true;
				enableLivingAssemblyConstituencyCombo = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
*/
	
	
	public void handleDistrictChange(AjaxBehaviorEvent event) {
		try {
			if (searchedUser.getDistrictVotingId() == 0 || searchedUser.getDistrictVotingId() == null) {
				enableAssemblyConstituencyCombo = false;
				assemblyConstituencyList = new ArrayList<>();
			} else {
				enableAssemblyConstituencyCombo = true;
                assemblyConstituencyList = locationService.getAllAssemblyConstituenciesOfDistrict(searchedUser.getDistrictVotingId());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handleLivingDistrictChange(AjaxBehaviorEvent event) {
		try {
			if (searchedUser.getDistrictLivingId() == 0 || searchedUser.getDistrictLivingId() == null) {
				enableLivingAssemblyConstituencyCombo = false;
				livingAssemblyConstituencyList = new ArrayList<>();
			} else {
				enableLivingAssemblyConstituencyCombo = true;
                livingAssemblyConstituencyList = locationService.getAllAssemblyConstituenciesOfDistrict(searchedUser.getDistrictLivingId());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	public void handleUserVotingDistrictChange(AjaxBehaviorEvent event) {
		try {
			if (selectedUserForEditing.getDistrictVotingId() == 0 || selectedUserForEditing.getDistrictVotingId() == null) {
				enableAssemblyConstituencyCombo = false;
				assemblyConstituencyList = new ArrayList<>();
			} else {
				enableAssemblyConstituencyCombo = true;
                assemblyConstituencyList = locationService.getAllAssemblyConstituenciesOfDistrict(selectedUserForEditing.getDistrictVotingId());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    */
	/*
	public void handleUserLivingDistrictChange(AjaxBehaviorEvent event) {
		try {
			if (selectedUserForEditing.getDistrictLivingId() == 0 || selectedUserForEditing.getDistrictLivingId() == null) {
				enableLivingAssemblyConstituencyCombo = false;
				livingAssemblyConstituencyList = new ArrayList<>();
			} else {
				enableLivingAssemblyConstituencyCombo = true;
                livingAssemblyConstituencyList = locationService.getAllAssemblyConstituenciesOfDistrict(selectedUserForEditing.getDistrictLivingId());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    */
	public boolean isDisableStateComobForRoles() {
		boolean returnValue = !"State".equals(location);
		return returnValue;
	}

	public boolean isRenderStateComboForRoles() {
        boolean returnValue = true;// (PostLocationType.Global == menuBean.getLocationType());
		return returnValue;
	}

	public boolean isDisableDistrictComobForRoles() {
		boolean returnValue = !"District".equals(location);
		return returnValue;
	}

	public boolean isRenderDistrictComboForRoles() {
        boolean returnValue = true;// (PostLocationType.STATE == menuBean.getLocationType());
		return returnValue;
	}
	
	public boolean isDisableCountryComobForRoles() {
		boolean returnValue = !"Country".equals(location);
		return returnValue;
	}

	public boolean isRenderCountryComboForRoles() {
        boolean returnValue = true;// (PostLocationType.Global == menuBean.getLocationType());
		return returnValue;
	}

	public boolean isDisableCountryRegionComobForRoles() {
		boolean returnValue = !"CountryRegion".equals(location);
		return returnValue;
	}

	public boolean isRenderCountryRegionComboForRoles() {
        boolean returnValue = true;// (PostLocationType.COUNTRY == menuBean.getLocationType());
		return returnValue;
	}

	public boolean isDisableAcComobForRoles() {
		boolean returnValue = !"Ac".equals(location);
		return returnValue;
	}

	public boolean isRenderAcComboForRoles() {
        boolean returnValue = true;// (PostLocationType.DISTRICT == menuBean.getLocationType());
		return returnValue;
	}

	public boolean isDisablePcComobForRoles() {
		boolean returnValue = !"Pc".equals(location);
		return returnValue;
	}

	public boolean isRenderPcComboForRoles() {
        boolean returnValue = true;// (PostLocationType.STATE == menuBean.getLocationType());
		return returnValue;
	}

	public boolean isShowMemberPanel() {
		return true;//StringUtil.isEmpty(selectedUserForEditing.getMembershipNumber());
	}

    public void handleRoleLocationChange(AjaxBehaviorEvent event) {
        try {
            Long locationId = null;
            if (selectedLocationForRole != null && selectedLocationForRole.getId() > 0) {
                locationId = selectedLocationForRole.getId();
            }
            loadLocationRoles(null, locationId, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public void handleRoleStateChange(AjaxBehaviorEvent event) {
		try {
			loadLocationRoles(PostLocationType.STATE, selectedStateIdForRoles, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void handleRoleCountryChange(AjaxBehaviorEvent event) {
		try {
			loadLocationRoles(PostLocationType.COUNTRY, selectedCountryIdForRoles, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void handleRoleCountryRegionChange(AjaxBehaviorEvent event) {
		try {
			loadLocationRoles(PostLocationType.REGION, selectedCountryRegionIdForRoles, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handleRoleDistrictChange(AjaxBehaviorEvent event) {
		try {
			loadLocationRoles(PostLocationType.DISTRICT, selectedDistrictIdForRoles, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handleRoleAcChange(AjaxBehaviorEvent event) {
		try {
			loadLocationRoles(PostLocationType.AC, selectedAcIdForRoles, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handleRolePcChange(AjaxBehaviorEvent event) {
		try {
			loadLocationRoles(PostLocationType.PC, selectedPcIdForRoles, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onClickNri() {
	}

	public void onClickMember() {
	}

	public void onClickSameAsLiving() {
	}

	public boolean isEnableDistrictCombo() {
		return enableDistrictCombo;
	}

	public void setEnableDistrictCombo(boolean enableDistrictCombo) {
		this.enableDistrictCombo = enableDistrictCombo;
	}

	public boolean isEnableLivingDistrictCombo() {
		return enableLivingDistrictCombo;
	}

	public void setEnableLivingDistrictCombo(boolean enableLivingDistrictCombo) {
		this.enableLivingDistrictCombo = enableLivingDistrictCombo;
	}

	public boolean isEnableLivingAssemblyConstituencyCombo() {
		return enableLivingAssemblyConstituencyCombo;
	}

	public void setEnableLivingAssemblyConstituencyCombo(boolean enableLivingAssemblyConstituencyCombo) {
		this.enableLivingAssemblyConstituencyCombo = enableLivingAssemblyConstituencyCombo;
	}

	public boolean isSameAsLiving() {
		return sameAsLiving;
	}

	public void setSameAsLiving(boolean sameAsLiving) {
		this.sameAsLiving = sameAsLiving;
	}

	public boolean isEnableLivingParliamentConstituencyCombo() {
		return enableLivingParliamentConstituencyCombo;
	}

	public void setEnableLivingParliamentConstituencyCombo(boolean enableLivingParliamentConstituencyCombo) {
		this.enableLivingParliamentConstituencyCombo = enableLivingParliamentConstituencyCombo;
	}

	public boolean isEnableParliamentConstituencyCombo() {
		return enableParliamentConstituencyCombo;
	}

	public void setEnableParliamentConstituencyCombo(boolean enableParliamentConstituencyCombo) {
		this.enableParliamentConstituencyCombo = enableParliamentConstituencyCombo;
	}

    public SearchUser getSearchedUser() {
		return searchedUser;
	}

    public void setSearchedUser(SearchUser searchedUser) {
		this.searchedUser = searchedUser;
	}

	public boolean isShowResult() {
		return showResult;
	}

	public void setShowResult(boolean showResult) {
		this.showResult = showResult;
	}

	public boolean isShowSearchPanel() {
		return showSearchPanel;
	}

	public void setShowSearchPanel(boolean showSearchPanel) {
		this.showSearchPanel = showSearchPanel;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Long getSelectedStateIdForRoles() {
		return selectedStateIdForRoles;
	}

	public void setSelectedStateIdForRoles(Long selectedStateIdForRoles) {
		this.selectedStateIdForRoles = selectedStateIdForRoles;
	}

	public Long getSelectedDistrictIdForRoles() {
		return selectedDistrictIdForRoles;
	}

	public void setSelectedDistrictIdForRoles(Long selectedDistrictIdForRoles) {
		this.selectedDistrictIdForRoles = selectedDistrictIdForRoles;
	}

	public Long getSelectedAcIdForRoles() {
		return selectedAcIdForRoles;
	}

	public void setSelectedAcIdForRoles(Long selectedAcIdForRoles) {
		this.selectedAcIdForRoles = selectedAcIdForRoles;
	}

	public Long getSelectedPcIdForRoles() {
		return selectedPcIdForRoles;
	}

	public void setSelectedPcIdForRoles(Long selectedPcIdForRoles) {
		this.selectedPcIdForRoles = selectedPcIdForRoles;
	}

	public boolean isShowRolesPanel() {
		return showRolesPanel;
	}

	public void setShowRolesPanel(boolean showRolesPanel) {
		this.showRolesPanel = showRolesPanel;
	}

	public RoleModel getUserLocationRoles() {
		return userLocationRoles;
	}

	public void setUserLocationRoles(RoleModel userLocationRoles) {
		this.userLocationRoles = userLocationRoles;
	}

	public boolean isDisableSaveMemberRoleButton() {
		return disableSaveMemberRoleButton;
	}

	public void setDisableSaveMemberRoleButton(boolean disableSaveMemberRoleButton) {
		this.disableSaveMemberRoleButton = disableSaveMemberRoleButton;
	}

	public Long getSelectedCountryIdForRoles() {
		return selectedCountryIdForRoles;
	}

	public void setSelectedCountryIdForRoles(Long selectedCountryIdForRoles) {
		this.selectedCountryIdForRoles = selectedCountryIdForRoles;
	}

	public Long getSelectedCountryRegionIdForRoles() {
		return selectedCountryRegionIdForRoles;
	}

	public void setSelectedCountryRegionIdForRoles(Long selectedCountryRegionIdForRoles) {
		this.selectedCountryRegionIdForRoles = selectedCountryRegionIdForRoles;
	}

	public Long getSelectedCountryRegionAreaIdForRoles() {
		return selectedCountryRegionAreaIdForRoles;
	}

	public void setSelectedCountryRegionAreaIdForRoles(Long selectedCountryRegionAreaIdForRoles) {
		this.selectedCountryRegionAreaIdForRoles = selectedCountryRegionAreaIdForRoles;
	}

    public List<Location> getStateList() {
        return stateList;
    }

    public void setStateList(List<Location> stateList) {
        this.stateList = stateList;
    }

    public List<Location> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(List<Location> districtList) {
        this.districtList = districtList;
    }

    public List<Location> getAssemblyConstituencyList() {
        return assemblyConstituencyList;
    }

    public void setAssemblyConstituencyList(List<Location> assemblyConstituencyList) {
        this.assemblyConstituencyList = assemblyConstituencyList;
    }

    public List<Location> getParliamentConstituencyList() {
        return parliamentConstituencyList;
    }

    public void setParliamentConstituencyList(List<Location> parliamentConstituencyList) {
        this.parliamentConstituencyList = parliamentConstituencyList;
    }

    public boolean isEnableAssemblyConstituencyCombo() {
        return enableAssemblyConstituencyCombo;
    }

    public void setEnableAssemblyConstituencyCombo(boolean enableAssemblyConstituencyCombo) {
        this.enableAssemblyConstituencyCombo = enableAssemblyConstituencyCombo;
    }

    public List<Location> getLivingStateList() {
        return livingStateList;
    }

    public void setLivingStateList(List<Location> livingStateList) {
        this.livingStateList = livingStateList;
    }

    public List<Location> getLivingDistrictList() {
        return livingDistrictList;
    }

    public void setLivingDistrictList(List<Location> livingDistrictList) {
        this.livingDistrictList = livingDistrictList;
    }

    public List<Location> getLivingAssemblyConstituencyList() {
        return livingAssemblyConstituencyList;
    }

    public void setLivingAssemblyConstituencyList(List<Location> livingAssemblyConstituencyList) {
        this.livingAssemblyConstituencyList = livingAssemblyConstituencyList;
    }

    public List<Location> getLivingParliamentConstituencyList() {
        return livingParliamentConstituencyList;
    }

    public void setLivingParliamentConstituencyList(List<Location> livingParliamentConstituencyList) {
        this.livingParliamentConstituencyList = livingParliamentConstituencyList;
    }

    public List<Location> getCountries() {
        return countries;
    }

    public void setCountries(List<Location> countries) {
        this.countries = countries;
    }

    public List<SelectItem> getMemberLocationList() {
        return memberLocationList;
    }

    public void setMemberLocationList(List<SelectItem> memberLocationList) {
        this.memberLocationList = memberLocationList;
    }

    public List<Role> getEditingUserLocationRoles() {
        return editingUserLocationRoles;
    }

    public void setEditingUserLocationRoles(List<Role> editingUserLocationRoles) {
        this.editingUserLocationRoles = editingUserLocationRoles;
    }

    public List<UserSearchResult> getUserSearchResults() {
        return userSearchResults;
    }

    public void setUserSearchResults(List<UserSearchResult> userSearchResults) {
        this.userSearchResults = userSearchResults;
    }

    public void setSelectedUserForEditing(UserSearchResult selectedUserForEditing) {
        this.selectedUserForEditing = selectedUserForEditing;
        showSearchPanel = false;
    }

    public UserSearchResult getSelectedUserForEditing() {
        return selectedUserForEditing;
    }

    public Location getSelectedLocationForRole() {
        return selectedLocationForRole;
    }

    public void setSelectedLocationForRole(Location selectedLocationForRole) {
        this.selectedLocationForRole = selectedLocationForRole;
    }

    public LocationConvertor getLocationConvertor() {
        return locationConvertor;
    }

    public void setLocationConvertor(LocationConvertor locationConvertor) {
        this.locationConvertor = locationConvertor;
    }

}
