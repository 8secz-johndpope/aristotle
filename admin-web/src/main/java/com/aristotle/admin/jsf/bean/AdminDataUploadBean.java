package com.aristotle.admin.jsf.bean;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.aristotle.admin.jsf.convertors.LocationConvertor;
import com.aristotle.admin.jsf.convertors.LocationTypeConvertor;
import com.aristotle.core.enums.AppPermission;
import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Location;
import com.aristotle.core.persistance.LocationType;
import com.aristotle.core.persistance.User;
import com.aristotle.core.service.LocationService;
import com.aristotle.core.service.UserService;
import com.aristotle.core.service.dto.UserUploadDto;
import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLBeanName;
import com.ocpsoft.pretty.faces.annotation.URLMapping;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "view")
@URLMapping(id = "adminDataUploadBean", beanName = "adminDataUploadBean", pattern = "/admin/user-upload", viewId = "/admin/admin_user_upload.xhtml")
@URLBeanName("adminDataUploadBean")
public class AdminDataUploadBean extends BaseMultiPermissionAdminJsfBean {

	private static final long serialVersionUID = 1L;
    private List<UserUploadDto> userBeingUploaded;
    private List<LocationType> locationTypes;
    private LocationType selectedLocationType;
    private boolean showStateLocationSelectionOption;
    private boolean showDistrictLocationSelectionOption;
    private boolean showAcLocationSelectionOption;
    private boolean showPcLocationSelectionOption;

    private boolean showCountryLocationSelectionOption;
    private boolean showCountryRegionLocationSelectionOption;
    private boolean showCountryRegionAreaLocationSelectionOption;

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
    private boolean userNamePassword;

    @Autowired
    private UserService userService;
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

	public AdminDataUploadBean() {
        super("/admin/user-upload", AppPermission.ADMIN_EVENT);
	}

	// @URLActions(actions = { @URLAction(mappingId = "userProfileBean") })
	@URLAction(onPostback = false)
	public void init() throws Exception {

		if (!checkUserAccess()) {
			return;
		}

        User loggedInAdminUser = getLoggedInUser(true, buildLoginUrl("/admin/search"));
        if (loggedInAdminUser == null) {
            return;
        }
        loadStates();
        loadCountries();
		System.out.println("Init");
        if (loggedInAdminUser.isSuperAdmin() || menuBean.isGlobalAdmin()) {
            locationTypes = locationService.getAllLocationTypes();
        } else {
            locationTypes = locationService.getAllLocationUnderLocationType(menuBean.getSelectedLocation().getLocationTypeId());
        }
        locationTypeConvertor.setLocationTypes(locationTypes);
	}

    private void loadStates() throws AppException {
        states = locationService.getAllStates();
        stateLocationConvertor.setLocations(states);
    }

    private void loadCountries() throws AppException {
        countries = locationService.getAllCountries();
        countryLocationConvertor.setLocations(countries);
    }

    public void uploadData() {
        try {
            userService.saveUsers(userBeingUploaded);
            int totalSuccess = 0;
            int totalFailed = 0;
            for (UserUploadDto oneUserUploadDto : userBeingUploaded) {
                if (oneUserUploadDto.isUserCreated()) {
                    totalSuccess++;
                } else {
                    totalFailed++;
                }
            }
            sendInfoMessageToJsfScreen("Total User created " + totalSuccess + ", total failed user " + totalFailed);
        } catch (Exception ex) {
            sendErrorMessageToJsfScreen(ex);
        }

	}

    public void cancel() {
        userBeingUploaded = null;
    }

    public boolean isShowUploadButton() {
        if (userBeingUploaded == null || userBeingUploaded.isEmpty()) {
            return false;
        }
        return true;
    }


    public void handleFileUpload(FileUploadEvent event) {
        System.out.println("Uploading File " + event.getFile().getFileName());

        try {
            Reader in = new InputStreamReader(event.getFile().getInputstream());
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader("name", "email", "phone").withDelimiter(',').parse(in);
            userBeingUploaded = new ArrayList<UserUploadDto>();
            boolean header = true;
            for (CSVRecord record : records) {
                if (header) {
                    header = false;
                } else {
                    String email = record.get("email");
                    String phone = record.get("phone");
                    String name = record.get("name");
                    if (StringUtils.isEmpty(phone)) {
                        phone = record.get("mobile");
                    }

                    UserUploadDto userUploadDto = new UserUploadDto();
                    userUploadDto.setEmail(email);
                    userUploadDto.setPhone(phone);
                    userUploadDto.setName(name);
                    userBeingUploaded.add(userUploadDto);
                }

            }
            userService.checkUserStatus(userBeingUploaded);
        } catch (Exception ex) {
            logger.error("Unable to upload File", ex);
            sendErrorMessageToJsfScreen("Failed", event.getFile().getFileName() + " is failed to uploaded.");
        }
    }

    public void handleLocationTypeSelection() {
        toggleStateSelectOption();
        toggleDistrictSelectOption();
        toggleAcSelectOption();
        togglePcSelectOption();
        toggleCountrySelectOption();
        toggleCountryRegionSelectOption();
        toggleCountryRegionAreaSelectOption();
    }

    private void toggleStateSelectOption() {
        if (selectedLocationType.getName().equalsIgnoreCase("State") || selectedLocationType.getName().equalsIgnoreCase("District")
                || selectedLocationType.getName().equalsIgnoreCase("AssemblyConstituency") || selectedLocationType.getName().equalsIgnoreCase("ParliamentConstituency")) {
            showStateLocationSelectionOption = true;
        } else {
            showStateLocationSelectionOption = false;
        }
    }

    private void toggleDistrictSelectOption() {
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
        if (selectedLocationType.getName().equalsIgnoreCase("Country") || selectedLocationType.getName().equalsIgnoreCase("CountryRegion")
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

    public List<UserUploadDto> getUserBeingUploaded() {
        return userBeingUploaded;
    }

    public void setUserBeingUploaded(List<UserUploadDto> userBeingUploaded) {
        this.userBeingUploaded = userBeingUploaded;
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

    public boolean isShowStateLocationSelectionOption() {
        return showStateLocationSelectionOption;
    }

    public void setShowStateLocationSelectionOption(boolean showStateLocationSelectionOption) {
        this.showStateLocationSelectionOption = showStateLocationSelectionOption;
    }

    public boolean isShowDistrictLocationSelectionOption() {
        return showDistrictLocationSelectionOption;
    }

    public void setShowDistrictLocationSelectionOption(boolean showDistrictLocationSelectionOption) {
        this.showDistrictLocationSelectionOption = showDistrictLocationSelectionOption;
    }

    public boolean isShowAcLocationSelectionOption() {
        return showAcLocationSelectionOption;
    }

    public void setShowAcLocationSelectionOption(boolean showAcLocationSelectionOption) {
        this.showAcLocationSelectionOption = showAcLocationSelectionOption;
    }

    public boolean isShowPcLocationSelectionOption() {
        return showPcLocationSelectionOption;
    }

    public void setShowPcLocationSelectionOption(boolean showPcLocationSelectionOption) {
        this.showPcLocationSelectionOption = showPcLocationSelectionOption;
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

    public Location getSelectedState() {
        return selectedState;
    }

    public void setSelectedState(Location selectedState) {
        this.selectedState = selectedState;
    }

    public Location getSelectedDistrict() {
        return selectedDistrict;
    }

    public void setSelectedDistrict(Location selectedDistrict) {
        this.selectedDistrict = selectedDistrict;
    }

    public Location getSelectedAc() {
        return selectedAc;
    }

    public void setSelectedAc(Location selectedAc) {
        this.selectedAc = selectedAc;
    }

    public Location getSelectedPc() {
        return selectedPc;
    }

    public void setSelectedPc(Location selectedPc) {
        this.selectedPc = selectedPc;
    }

    public Location getSelectedCountry() {
        return selectedCountry;
    }

    public void setSelectedCountry(Location selectedCountry) {
        this.selectedCountry = selectedCountry;
    }

    public Location getSelectedCountryRegion() {
        return selectedCountryRegion;
    }

    public void setSelectedCountryRegion(Location selectedCountryRegion) {
        this.selectedCountryRegion = selectedCountryRegion;
    }

    public Location getSelectedCountryRegionArea() {
        return selectedCountryRegionArea;
    }

    public void setSelectedCountryRegionArea(Location selectedCountryRegionArea) {
        this.selectedCountryRegionArea = selectedCountryRegionArea;
    }

    public LocationService getLocationService() {
        return locationService;
    }

    public void setLocationService(LocationService locationService) {
        this.locationService = locationService;
    }

    public LocationTypeConvertor getLocationTypeConvertor() {
        return locationTypeConvertor;
    }

    public void setLocationTypeConvertor(LocationTypeConvertor locationTypeConvertor) {
        this.locationTypeConvertor = locationTypeConvertor;
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

    public LocationConvertor getPcLocationConvertor() {
        return pcLocationConvertor;
    }

    public void setPcLocationConvertor(LocationConvertor pcLocationConvertor) {
        this.pcLocationConvertor = pcLocationConvertor;
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

    public List<Location> getStates() {
        return states;
    }

    public void setStates(List<Location> states) {
        this.states = states;
    }

    public List<Location> getDistricts() {
        return districts;
    }

    public void setDistricts(List<Location> districts) {
        this.districts = districts;
    }

    public List<Location> getAcs() {
        return acs;
    }

    public void setAcs(List<Location> acs) {
        this.acs = acs;
    }

    public List<Location> getPcs() {
        return pcs;
    }

    public void setPcs(List<Location> pcs) {
        this.pcs = pcs;
    }

    public List<Location> getCountries() {
        return countries;
    }

    public void setCountries(List<Location> countries) {
        this.countries = countries;
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

    public LocationConvertor getCountryLocationConvertor() {
        return countryLocationConvertor;
    }

    public void setCountryLocationConvertor(LocationConvertor countryLocationConvertor) {
        this.countryLocationConvertor = countryLocationConvertor;
    }

    public boolean isUserNamePassword() {
        return userNamePassword;
    }

    public void setUserNamePassword(boolean userNamePassword) {
        this.userNamePassword = userNamePassword;
    }

}
