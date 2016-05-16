package com.aristotle.admin.jsf.bean;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.faces.event.AjaxBehaviorEvent;

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
@URLMapping(id = "adminMemberUploadBean", beanName = "adminMemberUploadBean", pattern = "/admin/member-upload", viewId = "/admin/admin_member_upload.xhtml")
@URLBeanName("adminMemberUploadBean")
public class AdminMemberUploadBean extends BaseMultiPermissionAdminJsfBean {

	private static final long serialVersionUID = 1L;
    private List<UserUploadDto> userBeingUploaded;
    private List<LocationType> locationTypes;

    private Location selectedState;
    private Location selectedDistrict;
    private List<Location> states;
    private List<Location> districts;

    @Autowired
    private UserService userService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private LocationConvertor stateLocationConvertor;
    @Autowired
    private LocationConvertor districtLocationConvertor;
	public AdminMemberUploadBean() {
        super("/admin/member-upload", AppPermission.ADMIN_EVENT);
	}

	// @URLActions(actions = { @URLAction(mappingId = "userProfileBean") })
	@URLAction(onPostback = false)
	public void init() throws Exception {

		if (!checkUserAccess()) {
			return;
		}

        User loggedInAdminUser = getLoggedInUser(true, buildLoginUrl("/admin/member-upload"));
        if (loggedInAdminUser == null) {
            return;
        }
		System.out.println("Init");
        if (menuBean.getSelectedLocation() == null && (loggedInAdminUser.isSuperAdmin() || menuBean.isGlobalAdmin())) {
            locationTypes = locationService.getAllLocationTypes();
        } else {
            locationTypes = locationService.getAllLocationUnderLocationType(menuBean.getSelectedLocation().getLocationTypeId());
        }
        states = locationService.getAllStates();
        stateLocationConvertor.setLocations(states);
	}

    public void uploadData() {
        try {
            validateLocationSelection();
            if (!isValidInput()) {
                return;
            }
            //userService.saveUsers(userBeingUploaded, userNamePassword, selectedState, selectedDistrict, selectedPc, selectedAc);
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

    private void validateLocationSelection() {
        if (selectedState == null) {
            sendErrorMessageToJsfScreen("Please select a state");
        }
        if (selectedDistrict == null) {
            sendErrorMessageToJsfScreen("Please select a district");
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

    public void handleStateChange(final AjaxBehaviorEvent event) {
        System.out.println("Location Select : " + selectedState+", "+event);
        try {
            selectedDistrict = null;
            districts = locationService.getAllDistrictOfState(selectedState.getId());
            districtLocationConvertor.setLocations(districts);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleDistrictChange() {
        System.out.println("Location Select : " + selectedDistrict);
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

    public LocationService getLocationService() {
        return locationService;
    }

    public void setLocationService(LocationService locationService) {
        this.locationService = locationService;
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


}
