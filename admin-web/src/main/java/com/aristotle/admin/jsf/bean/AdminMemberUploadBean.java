package com.aristotle.admin.jsf.bean;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
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
import com.aristotle.core.persistance.State;
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
        states = locationService.getAllStates();
        Location state = new Location();
        state.setId(0L);
        state.setName("Select State");
        states.add(0, state);
        stateLocationConvertor.setLocations(states);
        selectedState = states.get(0);
        handleStateChange();
	}

    public void uploadData() {
        try {
            validateLocationSelection();
            if (!isValidInput()) {
                return;
            }
            userService.saveMembers(userBeingUploaded, false, selectedState, selectedDistrict, null, null);
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
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader("Name", "Mobile", "Email", "State", "District","Reference", "TxnId").withDelimiter(',').parse(in);
            userBeingUploaded = new ArrayList<UserUploadDto>();
            boolean header = true;
            for (CSVRecord record : records) {
                if (header) {
                    header = false;
                } else {
                    String email = record.get("Email");
                    String mobile = record.get("Mobile");
                    String referenceMobile = record.get("Reference");
                    String name = record.get("Name");
                    String txnId = record.get("TxnId");
                   

                    UserUploadDto userUploadDto = new UserUploadDto();
                    userUploadDto.setEmail(email);
                    userUploadDto.setPhone(mobile);
                    userUploadDto.setReferencePhone(referenceMobile);
                    userUploadDto.setTxnId(txnId);
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

    public void handleStateChange() {
        System.out.println("Location Select : " + selectedState);
        try {
        	if(selectedState.getId() == 0L){
        		districts = Collections.emptyList();
        		return;
        	}
            districts = locationService.getAllDistrictOfState(selectedState.getId());
            Location district = new Location();
            district.setId(0L);
            district.setName("Select District");
            districts.add(0, district);
            districtLocationConvertor.setLocations(districts);
            selectedDistrict = districts.get(0);
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
