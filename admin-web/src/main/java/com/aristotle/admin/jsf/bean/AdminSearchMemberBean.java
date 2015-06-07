package com.aristotle.admin.jsf.bean;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.aristotle.admin.jsf.convertors.LocationConvertor;
import com.aristotle.admin.jsf.convertors.LocationTypeConvertor;
import com.aristotle.core.enums.AppPermission;
import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Interest;
import com.aristotle.core.persistance.Location;
import com.aristotle.core.persistance.LocationType;
import com.aristotle.core.persistance.User;
import com.aristotle.core.persistance.Volunteer;
import com.aristotle.core.service.AppService;
import com.aristotle.core.service.LocationService;
import com.aristotle.core.service.UserService;
import com.aristotle.core.service.dto.UserSearchResult;
import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLBeanName;
import com.ocpsoft.pretty.faces.annotation.URLMapping;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "view")
@URLMapping(id = "adminSearchMemberBean", beanName = "adminSearchMemberBean", pattern = "/admin/search", viewId = "/admin/admin_searchmember.xhtml")
@URLBeanName("adminSearchMemberBean")
public class AdminSearchMemberBean extends BaseMultiPermissionAdminJsfBean {
	
	private static final long serialVersionUID = 1L;
    private static final Long NRI_ID = -1L;
    private static final Long GLOBAL_ID = -2L;
    private boolean showExporter;
    private boolean locationSearch;
    private boolean emailSearch;
    private boolean showLocationSearchPanel;
    private boolean showStateLocationSelectionOption;
    private boolean showDistrictLocationSelectionOption;
    private boolean showAcLocationSelectionOption;
    private boolean showPcLocationSelectionOption;

    private boolean showCountryLocationSelectionOption;
    private boolean showCountryRegionLocationSelectionOption;
    private boolean showCountryRegionAreaLocationSelectionOption;

    private List<LocationType> locationTypes;
    private LocationType selectedLocationType;
    private Location selectedState;
    private Location selectedDistrict;
    private Location selectedAc;
    private Location selectedPc;
    private Location selectedCountry;
    private Location selectedCountryRegion;
    private Location selectedCountryRegionArea;
    private List<Location> states;
    private List<Location> districts;
    private List<Location> acs;
    private List<Location> pcs;
    private List<Location> countries;
    private List<Location> countryRegions;
    private List<Location> countryRegionAreas;

    private List<UserSearchResult> userSearchResults;

    private String emailText;

    @Autowired
    private LocationService locationService;
    @Autowired
    private LocationTypeConvertor locationTypeConvertor;
    @Autowired
    private LocationConvertor stateLocationConvertor;
    @Autowired
    private LocationConvertor districtLocationConvertor;
    @Autowired
    private LocationConvertor acLocationConvertor;
    @Autowired
    private LocationConvertor pcLocationConvertor;
    @Autowired
    private LocationConvertor countryLocationConvertor;
    @Autowired
    private LocationConvertor countryRegionLocationConvertor;
    @Autowired
    private LocationConvertor countryRegionAreaLocationConvertor;
    @Autowired
    private VolunteerBean volunteerBean;
    @Autowired
    private UserService userService;
    @Autowired
    private AppService appService;


	
	public AdminSearchMemberBean(){
        super("/admin/search", AppPermission.ADD_MEMBER, AppPermission.VIEW_MEMBER, AppPermission.UPDATE_GLOBAL_MEMBER, AppPermission.UPDATE_MEMBER);
	}

	@URLAction(onPostback = false)
	public void init() throws Exception {
        System.out.println("stateLocationConvertor=" + stateLocationConvertor);
        System.out.println("districtLocationConvertor=" + districtLocationConvertor);
        volunteerBean.init(null);
		if(!checkUserAccess()){
			return;
		}
        User loggedInAdminUser = getLoggedInUser(true, buildLoginUrl("/admin/search"));
		if (loggedInAdminUser == null) {
			return;
		}
        locationSearch = false;
        showLocationSearchPanel = false;
        emailSearch = true;
		
        if (loggedInAdminUser.isSuperAdmin() || menuBean.isGlobalAdmin()) {
            locationTypes = locationService.getAllLocationTypes();
            addNriAndGlobalLocationType();
        } else {
            locationTypes = locationService.getAllLocationUnderLocationType(menuBean.getSelectedLocation().getLocationTypeId());
        }
        locationTypeConvertor.setLocationTypes(locationTypes);

	}

    public void searchUser() {
        System.out.println(volunteerBean.getSelectedInterestIds());
        try {
            if (emailSearch) {
                searchUserByEmail();
            } else {
                searchVolunteerByLocation();
            }
        } catch (Exception ex) {
            sendErrorMessageToJsfScreen(ex);
        } finally {
            toggleShowExporter();
        }

    }

    private void searchUserByEmail() throws AppException {
        userSearchResults = userService.searchUserByEmail(emailText);
    }

    private void toggleShowExporter() {
        if (userSearchResults == null || userSearchResults.isEmpty()) {
            showExporter = false;
        } else {
            showExporter = true;
        }
    }

    public void onRowToggle(ToggleEvent event) {
        UserSearchResult selectedUser = (UserSearchResult) event.getData();
        if (event.getVisibility().equals(Visibility.VISIBLE)) {
            try {
                Volunteer selectedVolunteer = appService.getVolunteerDataForUser(selectedUser.getId());
                selectedUser.setVolunteerRecord(selectedVolunteer);
                if (selectedVolunteer == null) {
                    System.out.println("Selected Volunteer is null null");
                    selectedUser.setVolunteerInterests(Collections.emptySet());
                } else {
                    System.out.println("Selected Volunteer is not null");
                    Set<Interest> userInterests = selectedVolunteer.getInterests();
                    if (userInterests == null || userInterests.isEmpty()) {
                        System.out.println("No User Interests");
                        selectedUser.setVolunteerInterests(Collections.emptySet());
                    } else {
                        System.out.println("Some User Interests");
                        selectedUser.setVolunteerInterests(userInterests);
                    }

                }
            } catch (AppException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        System.out.println("Row Toggled " + (event.getData()));
    }
    private void searchVolunteerByLocation() throws AppException {
        List<Long> selectedIntrests = volunteerBean.getSelectedInterestIds();
        if (selectedLocationType == null || selectedLocationType.getId().equals(GLOBAL_ID)) {
            // Search Global Users
            userSearchResults = userService.searchGlobalUserForVolunteerIntrest(selectedIntrests);

        } else if (selectedLocationType.getId().equals(NRI_ID)) {
            // Search all User where location is not India
            userSearchResults = userService.searchNriUserForVolunteerIntrest(selectedIntrests);
        } else {
            // Search for given Location as per selected LocationType
            Long locationId = getSelectedLocationId();
            userSearchResults = userService.searchLocationUserForVolunteerIntrest(locationId, selectedIntrests);
        }
    }

    private Long getSelectedLocationId() throws AppException {
        if (selectedLocationType.getId().equals(GLOBAL_ID)) {
            return GLOBAL_ID;
        }
        if (selectedLocationType.getId().equals(NRI_ID)) {
            return NRI_ID;
        }
        Location location = getLocationIdFromType("Country", selectedCountry);
        if (location != null) {
            return location.getId();
        }

        location = getLocationIdFromType("State", selectedState);
        if (location != null) {
            return location.getId();
        }

        location = getLocationIdFromType("District", selectedDistrict);
        if (location != null) {
            return location.getId();
        }

        location = getLocationIdFromType("AssemblyConstituency", selectedAc);
        if (location != null) {
            return location.getId();
        }

        location = getLocationIdFromType("ParliamentConstituency", selectedPc);
        if (location != null) {
            return location.getId();
        }

        location = getLocationIdFromType("CountryRegion", selectedCountryRegion);
        if (location != null) {
            return location.getId();
        }

        location = getLocationIdFromType("CountryRegionArea", selectedCountryRegionArea);
        if (location != null) {
            return location.getId();
        }
        return null;
    }

    private Location getLocationIdFromType(String locationTypeName, Location selectedLocation) throws AppException {
        if (selectedLocationType.getName().equals(locationTypeName)) {
            if (selectedLocation == null) {
                throw new AppException("Please select a Country");
            }
            return selectedLocation;
        }
        return null;
    }

    private void addNriAndGlobalLocationType() {
        LocationType nriLocationType = new LocationType();
        nriLocationType.setId(NRI_ID);
        nriLocationType.setName("Nri");
        locationTypes.add(nriLocationType);

        LocationType globalLocationType = new LocationType();
        globalLocationType.setId(GLOBAL_ID);
        globalLocationType.setName("Global(NRI + India)");
        locationTypes.add(0, globalLocationType);
    }

    public void locationSearchSelectionHandler() {
        System.out.println("Location Search Select : " + locationSearch);
        if (locationSearch) {
            emailSearch = false;
            showLocationSearchPanel = false;
        }
	}

    public void emailSearchSelectionHandler() {
        System.out.println("Email Search Select : " + emailSearch);
        if (emailSearch) {
            locationSearch = false;
        }

    }

    public void handleLocationTypeSelection() {
        try {
            System.out.println("Location Type Select : " + selectedLocationType);
            if (!selectedLocationType.getId().equals(NRI_ID)) {
                // If location Type is Not Global then Show Location Search panel
                showLocationSearchPanel = true;
            } else {
                showLocationSearchPanel = false;
            }
            if (selectedLocationType.getId().equals(GLOBAL_ID) || selectedLocationType.getName().equalsIgnoreCase("Country") ||
                    selectedLocationType.getName().equalsIgnoreCase("CountryRegion")
                    || selectedLocationType.getName().equalsIgnoreCase("CountryRegionArea")) {
                // NRI So load Countries
                loadCountries();
            } else {
                // Load States
                loadStates();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        toggleStateSelectOption();
        toggleDistrictSelectOption();
        toggleAcSelectOption();
        togglePcSelectOption();
        toggleCountrySelectOption();
        toggleCountryRegionSelectOption();
        toggleCountryRegionAreaSelectOption();
    }

    private void loadStates() throws AppException {
        states = locationService.getAllStates();
        stateLocationConvertor.setLocations(states);
    }

    private void loadCountries() throws AppException {
        countries = locationService.getAllCountries();
        countryLocationConvertor.setLocations(countries);
    }

    private void toggleStateSelectOption() {
        if (selectedLocationType.getName().equalsIgnoreCase("State") || selectedLocationType.getName().equalsIgnoreCase("District")
                || selectedLocationType.getName().equalsIgnoreCase("AssemblyConstituency") || selectedLocationType.getName().equalsIgnoreCase("ParliamentConstituency")) {
            showStateLocationSelectionOption = true;
        } else {
            showStateLocationSelectionOption = false;
        }
    }
    private void toggleDistrictSelectOption(){
        if (selectedLocationType.getName().equalsIgnoreCase("District") || selectedLocationType.getName().equalsIgnoreCase("AssemblyConstituency")) {
            showDistrictLocationSelectionOption = true;
        } else {
            showDistrictLocationSelectionOption = false;
        }
    }

    private void toggleAcSelectOption() {
        if (selectedLocationType.getName().equalsIgnoreCase("AssemblyConstituency")) {
            showAcLocationSelectionOption = true;
        } else {
            showAcLocationSelectionOption = false;
        }
    }

    private void togglePcSelectOption() {
        if (selectedLocationType.getName().equalsIgnoreCase("ParliamentConstituency")) {
            showPcLocationSelectionOption = true;
        } else {
            showPcLocationSelectionOption = false;
        }
    }

    private void toggleCountrySelectOption() {
        if (selectedLocationType.getId().equals(NRI_ID) || selectedLocationType.getName().equalsIgnoreCase("Country") || selectedLocationType.getName().equalsIgnoreCase("CountryRegion")
                || selectedLocationType.getName().equalsIgnoreCase("CountryRegionArea")) {
            showCountryLocationSelectionOption = true;
        } else {
            showCountryLocationSelectionOption = false;
        }
    }

    private void toggleCountryRegionSelectOption() {
        if (selectedLocationType.getName().equalsIgnoreCase("CountryRegion") || selectedLocationType.getName().equalsIgnoreCase("CountryRegionArea")) {
            showCountryRegionLocationSelectionOption = true;
        } else {
            showCountryRegionLocationSelectionOption = false;
        }
    }

    private void toggleCountryRegionAreaSelectOption() {
        if (selectedLocationType.getName().equalsIgnoreCase("CountryRegionArea")) {
            showCountryRegionAreaLocationSelectionOption = true;
        } else {
            showCountryRegionAreaLocationSelectionOption = false;
        }
    }

    public void handleStateChange() {
        System.out.println("Location Select : " + selectedState);
        try {
            if (isShowPcLocationSelectionOption()) {
                pcs = locationService.getAllParliamentConstituenciesOfState(selectedState.getId());
                pcLocationConvertor.setLocations(pcs);

            }
            if (isShowDistrictLocationSelectionOption()) {
                districts = locationService.getAllDistrictOfState(selectedState.getId());
                districtLocationConvertor.setLocations(districts);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleDistrictChange() {
        System.out.println("Location Select : " + selectedDistrict);
        try {
            if (isShowAcLocationSelectionOption()) {
                acs = locationService.getAllAssemblyConstituenciesOfDistrict(selectedDistrict.getId());
                acLocationConvertor.setLocations(acs);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleCountryChange() {
        System.out.println("Location Select : " + selectedCountry);
        try {
            if (isShowCountryRegionLocationSelectionOption()) {
                countryRegions = locationService.getAllChildLocations(selectedCountry.getId());
                countryRegionLocationConvertor.setLocations(countryRegions);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleCountryRegionChange() {
        System.out.println("Location Select : " + selectedCountryRegion);
        try {
            if (isShowCountryRegionAreaLocationSelectionOption()) {
                countryRegionAreas = locationService.getAllChildLocations(selectedCountryRegion.getId());
                countryRegionAreaLocationConvertor.setLocations(countryRegionAreas);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleCountryRegionAreaChange() {
        System.out.println("Location Select : " + selectedCountryRegionArea);
    }

    public void handleAcChange() {
        System.out.println("Location Select : " + selectedAc);
    }

    public void handlePcChange() {
        System.out.println("Location Select : " + selectedPc);
    }


    public boolean isLocationSearch() {
        return locationSearch;
    }

    public void setLocationSearch(boolean locationSearch) {
        this.locationSearch = locationSearch;
    }

    public List<LocationType> getLocationTypes() {
        return locationTypes;
    }

    public void setLocationTypes(List<LocationType> locationTypes) {
        this.locationTypes = locationTypes;
    }

    public LocationType getSelectedLocationType() {
        return selectedLocationType;
    }

    public void setSelectedLocationType(LocationType selectedLocationType) {
        this.selectedLocationType = selectedLocationType;
    }

    public LocationService getLocationService() {
        return locationService;
    }

    public void setLocationService(LocationService locationService) {
        this.locationService = locationService;
    }

    public boolean isShowLocationSearchPanel() {
        return showLocationSearchPanel;
    }

    public void setShowLocationSearchPanel(boolean showLocationSearchPanel) {
        this.showLocationSearchPanel = showLocationSearchPanel;
    }

    public boolean isShowStateLocationSelectionOption() {
        return showStateLocationSelectionOption;
    }

    public void setShowStateLocationSelectionOption(boolean showStateLocationSelectionOption) {
        this.showStateLocationSelectionOption = showStateLocationSelectionOption;
    }

    public List<Location> getStates() {
        return states;
    }

    public void setStates(List<Location> states) {
        this.states = states;
    }

    public List<Location> getCountries() {
        return countries;
    }

    public void setCountries(List<Location> countries) {
        this.countries = countries;
    }

    public LocationConvertor getStateLocationConvertor() {
        return stateLocationConvertor;
    }

    public void setStateLocationConvertor(LocationConvertor stateLocationConvertor) {
        this.stateLocationConvertor = stateLocationConvertor;
    }

    public LocationConvertor getDistrictLocationConvertor() {
        return districtLocationConvertor;
    }

    public void setDistrictLocationConvertor(LocationConvertor districtLocationConvertor) {
        this.districtLocationConvertor = districtLocationConvertor;
    }

    public List<Location> getDistricts() {
        return districts;
    }

    public void setDistricts(List<Location> districts) {
        this.districts = districts;
    }

    public Location getSelectedState() {
        return selectedState;
    }

    public void setSelectedState(Location selectedState) {
        this.selectedState = selectedState;
    }

    public boolean isShowDistrictLocationSelectionOption() {
        return showDistrictLocationSelectionOption;
    }

    public void setShowDistrictLocationSelectionOption(boolean showDistrictLocationSelectionOption) {
        this.showDistrictLocationSelectionOption = showDistrictLocationSelectionOption;
    }

    public Location getSelectedDistrict() {
        return selectedDistrict;
    }

    public void setSelectedDistrict(Location selectedDistrict) {
        this.selectedDistrict = selectedDistrict;
    }

    public boolean isShowAcLocationSelectionOption() {
        return showAcLocationSelectionOption;
    }

    public void setShowAcLocationSelectionOption(boolean showAcLocationSelectionOption) {
        this.showAcLocationSelectionOption = showAcLocationSelectionOption;
    }

    public Location getSelectedAc() {
        return selectedAc;
    }

    public void setSelectedAc(Location selectedAc) {
        this.selectedAc = selectedAc;
    }

    public LocationConvertor getAcLocationConvertor() {
        return acLocationConvertor;
    }

    public void setAcLocationConvertor(LocationConvertor acLocationConvertor) {
        this.acLocationConvertor = acLocationConvertor;
    }

    public List<Location> getAcs() {
        return acs;
    }

    public void setAcs(List<Location> acs) {
        this.acs = acs;
    }

    public boolean isShowPcLocationSelectionOption() {
        return showPcLocationSelectionOption;
    }

    public void setShowPcLocationSelectionOption(boolean showPcLocationSelectionOption) {
        this.showPcLocationSelectionOption = showPcLocationSelectionOption;
    }

    public Location getSelectedPc() {
        return selectedPc;
    }

    public void setSelectedPc(Location selectedPc) {
        this.selectedPc = selectedPc;
    }

    public LocationConvertor getPcLocationConvertor() {
        return pcLocationConvertor;
    }

    public void setPcLocationConvertor(LocationConvertor pcLocationConvertor) {
        this.pcLocationConvertor = pcLocationConvertor;
    }

    public List<Location> getPcs() {
        return pcs;
    }

    public void setPcs(List<Location> pcs) {
        this.pcs = pcs;
    }

    public boolean isShowCountryLocationSelectionOption() {
        return showCountryLocationSelectionOption;
    }

    public void setShowCountryLocationSelectionOption(boolean showCountryLocationSelectionOption) {
        this.showCountryLocationSelectionOption = showCountryLocationSelectionOption;
    }

    public boolean isShowCountryRegionLocationSelectionOption() {
        return showCountryRegionLocationSelectionOption;
    }

    public void setShowCountryRegionLocationSelectionOption(boolean showCountryRegionLocationSelectionOption) {
        this.showCountryRegionLocationSelectionOption = showCountryRegionLocationSelectionOption;
    }

    public boolean isShowCountryRegionAreaLocationSelectionOption() {
        return showCountryRegionAreaLocationSelectionOption;
    }

    public void setShowCountryRegionAreaLocationSelectionOption(boolean showCountryRegionAreaLocationSelectionOption) {
        this.showCountryRegionAreaLocationSelectionOption = showCountryRegionAreaLocationSelectionOption;
    }

    public Location getSelectedCountry() {
        return selectedCountry;
    }

    public void setSelectedCountry(Location selectedCountry) {
        this.selectedCountry = selectedCountry;
    }

    public Location getSelectedCountryRegionArea() {
        return selectedCountryRegionArea;
    }

    public void setSelectedCountryRegionArea(Location selectedCountryRegionArea) {
        this.selectedCountryRegionArea = selectedCountryRegionArea;
    }

    public LocationConvertor getCountryLocationConvertor() {
        return countryLocationConvertor;
    }

    public void setCountryLocationConvertor(LocationConvertor countryLocationConvertor) {
        this.countryLocationConvertor = countryLocationConvertor;
    }

    public LocationConvertor getCountryRegionLocationConvertor() {
        return countryRegionLocationConvertor;
    }

    public void setCountryRegionLocationConvertor(LocationConvertor countryRegionLocationConvertor) {
        this.countryRegionLocationConvertor = countryRegionLocationConvertor;
    }

    public LocationConvertor getCountryRegionAreaLocationConvertor() {
        return countryRegionAreaLocationConvertor;
    }

    public void setCountryRegionAreaLocationConvertor(LocationConvertor countryRegionAreaLocationConvertor) {
        this.countryRegionAreaLocationConvertor = countryRegionAreaLocationConvertor;
    }

    public List<Location> getCountryRegions() {
        return countryRegions;
    }

    public void setCountryRegions(List<Location> countryRegions) {
        this.countryRegions = countryRegions;
    }

    public List<Location> getCountryRegionAreas() {
        return countryRegionAreas;
    }

    public void setCountryRegionAreas(List<Location> countryRegionAreas) {
        this.countryRegionAreas = countryRegionAreas;
    }

    public Location getSelectedCountryRegion() {
        return selectedCountryRegion;
    }

    public void setSelectedCountryRegion(Location selectedCountryRegion) {
        this.selectedCountryRegion = selectedCountryRegion;
    }

    public VolunteerBean getVolunteerBean() {
        return volunteerBean;
    }

    public void setVolunteerBean(VolunteerBean volunteerBean) {
        this.volunteerBean = volunteerBean;
    }

    public boolean isEmailSearch() {
        return emailSearch;
    }

    public void setEmailSearch(boolean emailSearch) {
        this.emailSearch = emailSearch;
    }

    public String getEmailText() {
        return emailText;
    }

    public void setEmailText(String emailText) {
        this.emailText = emailText;
    }

    public List<UserSearchResult> getUserSearchResults() {
        return userSearchResults;
    }

    public void setUserSearchResults(List<UserSearchResult> userSearchResults) {
        this.userSearchResults = userSearchResults;
    }

    public boolean isShowExporter() {
        return showExporter;
    }

    public void setShowExporter(boolean showExporter) {
        this.showExporter = showExporter;
    }
}
