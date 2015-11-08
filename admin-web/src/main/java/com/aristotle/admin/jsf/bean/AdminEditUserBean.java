package com.aristotle.admin.jsf.bean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.aristotle.admin.jsf.convertors.LocationConvertor;
import com.aristotle.core.enums.AppPermission;
import com.aristotle.core.enums.PostLocationType;
import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Location;
import com.aristotle.core.persistance.User;
import com.aristotle.core.service.LocationService;
import com.aristotle.core.service.UserService;
import com.aristotle.core.service.dto.SearchUser;
import com.aristotle.core.service.dto.UserSearchResultForEdting;
import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLBeanName;
import com.ocpsoft.pretty.faces.annotation.URLMapping;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "view")
@URLMapping(id = "adminEditUserBean", beanName = "adminEditUserBean", pattern = "/admin/edituser", viewId = "/admin/admin_edituser.xhtml")
@URLBeanName("adminEditUserBean")
public class AdminEditUserBean extends BaseMultiPermissionAdminJsfBean {

	private static final long serialVersionUID = 1L;

    @Autowired
    private LocationService locationService;
    @Autowired
    private UserService userService;
    @Autowired
    private LocationConvertor locationConvertor;

    private UserSearchResultForEdting selectedUserForEditing;
    private SearchUser searchedUser;

    private List<UserSearchResultForEdting> userSearchResults;

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

	PostLocationType selectedPostLocationType;
	Long selectedPostLocationId;

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
        userSearchResults = new ArrayList<UserSearchResultForEdting>();
		showResult = false;
		showSearchPanel = true;

        User loggedInAdminUser = getLoggedInUser(true, buildLoginUrl("/admin/edituser"));
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
        selectedUserForEditing = new UserSearchResultForEdting();
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


        } else {
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

    public void cancelSave() {
		showSearchPanel = true;
		resetRolePanel();
	}

    public void handleFileUpload(FileUploadEvent event) {
        System.out.println("Uploading File " + event.getFile().getFileName());
        try {
            userService.uploadUserProfilePic(event.getFile().getInputstream(), selectedUserForEditing.getUser(), event.getFile().getFileName());
        } catch (Exception ex) {
            logger.error("Unable to upload File", ex);
            sendErrorMessageToJsfScreen("Failed", event.getFile().getFileName() + " is failed to uploaded.");
        }
    }

    public boolean isMemberUpdateAllowed(UserSearchResultForEdting user) {
        /*
        User loggedInUser = getLoggedInUser();
        System.out.println("loggedInUser.getId()=" + loggedInUser.getId() + ",user.getId()=" + user.getId());
        return !loggedInUser.getId().equals(user.getId());
        */
        return true;
	}

    public void saveUser(ActionEvent event) {
		if (isValidInput()) {
            try {

            } catch (Exception e) {
                sendErrorMessageToJsfScreen(e);
            }

			resetRolePanel();
			sendInfoMessageToJsfScreen("Roles updated succesfully");
		}
	}
	
	public void resetRolePanel(){

		selectedPostLocationType = null;
		selectedPostLocationId = null;
		selectedAcIdForRoles = null;
		selectedDistrictIdForRoles = null;
		selectedPcIdForRoles = null;
		selectedStateIdForRoles = null;
		location = null;
	}

	public void searchMember() {
        try {
            userSearchResults = userService.searchUserForEditing(searchedUser);
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

    public List<UserSearchResultForEdting> getUserSearchResults() {
        return userSearchResults;
    }

    public void setUserSearchResults(List<UserSearchResultForEdting> userSearchResults) {
        this.userSearchResults = userSearchResults;
    }

    public void setSelectedUserForEditing(UserSearchResultForEdting selectedUserForEditing) {
        this.selectedUserForEditing = selectedUserForEditing;
        showSearchPanel = false;
    }

    public UserSearchResultForEdting getSelectedUserForEditing() {
        return selectedUserForEditing;
    }

    public LocationConvertor getLocationConvertor() {
        return locationConvertor;
    }

    public void setLocationConvertor(LocationConvertor locationConvertor) {
        this.locationConvertor = locationConvertor;
    }

}
