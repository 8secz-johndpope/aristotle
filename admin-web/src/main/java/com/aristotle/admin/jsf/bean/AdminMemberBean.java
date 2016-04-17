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

import com.aristotle.admin.jsf.bean.dto.OfflineMember;
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
import com.mysql.fabric.xmlrpc.base.Member;
import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLBeanName;
import com.ocpsoft.pretty.faces.annotation.URLMapping;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "view")
@URLMapping(id = "adminMemberBean", beanName = "adminMemberBean", pattern = "/admin/member", viewId = "/admin/admin_member.xhtml")
@URLBeanName("adminMemberBean")
public class AdminMemberBean extends BaseMultiPermissionAdminJsfBean {

	private static final long serialVersionUID = 1L;
	private static final Long NRI_ID = -1L;
	private static final Long GLOBAL_ID = -2L;

	private List<Location> states;
	private List<Location> districts;
	private List<Location> acs;
	private List<Location> pcs;
	
	private OfflineMember selectedMember;

	private List<UserSearchResult> userSearchResults;

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
	private UserService userService;
	
	public AdminMemberBean() {
		super("/admin/member", AppPermission.ADD_MEMBER, AppPermission.VIEW_MEMBER, AppPermission.UPDATE_GLOBAL_MEMBER,
				AppPermission.UPDATE_MEMBER, AppPermission.SEARCH_MEMBER);
	}

	@URLAction(onPostback = false)
	public void init() throws Exception {
		if (!checkUserAccess()) {
			return;
		}
		User loggedInAdminUser = getLoggedInUser(true, buildLoginUrl("/admin/member"));
		if (loggedInAdminUser == null) {
			return;
		}
		selectedMember = new OfflineMember();
		states = locationService.getAllStates();
		addFirstItem(states, "--Select State--");
		stateLocationConvertor.setLocations(states);
	}
	private void addFirstItem(List<Location> locations, String name){
		Location location = new Location();
		location.setName(name);
		location.setId(0L);
		locations.add(0, location);
	}

	public void saveMember() {
        System.out.println("Saving Member "+selectedMember);
        try {
        	sendInfoMessageToJsfScreen("Member Saves Succesfully");
        } catch (Exception ex) {
            sendErrorMessageToJsfScreen(ex);
        } finally {
        }

    }

	public void handleStateChange() {
		System.out.println("Location Select : " + selectedMember.getSelectedState());
		try {
			pcs = locationService.getAllParliamentConstituenciesOfState(selectedMember.getSelectedState().getId());
			pcLocationConvertor.setLocations(pcs);
			addFirstItem(pcs, "--Select Parliament Constituecy--");
			districts = locationService.getAllDistrictOfState(selectedMember.getSelectedState().getId());
			districtLocationConvertor.setLocations(districts);
			addFirstItem(districts, "--Select District--");
			acs = null;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void handleDistrictChange() {
		System.out.println("Location Select : " + selectedMember.getSelectedDistrict());
		try {
			acs = locationService.getAllAssemblyConstituenciesOfDistrict(selectedMember.getSelectedDistrict().getId());
			acLocationConvertor.setLocations(acs);
			addFirstItem(acs, "--Select Assemblye Constituency--");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public void handleAcChange() {
        System.out.println("Location Select : " + selectedMember.getSelectedAc());
    }

    public void handlePcChange() {
        System.out.println("Location Select : " + selectedMember.getSelectedPc());
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

	public OfflineMember getSelectedMember() {
		return selectedMember;
	}

	public void setSelectedMember(OfflineMember selectedMember) {
		this.selectedMember = selectedMember;
	}

	public List<UserSearchResult> getUserSearchResults() {
		return userSearchResults;
	}

	public void setUserSearchResults(List<UserSearchResult> userSearchResults) {
		this.userSearchResults = userSearchResults;
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

	public LocationConvertor getAcLocationConvertor() {
		return acLocationConvertor;
	}

	public void setAcLocationConvertor(LocationConvertor acLocationConvertor) {
		this.acLocationConvertor = acLocationConvertor;
	}

	public LocationConvertor getPcLocationConvertor() {
		return pcLocationConvertor;
	}

	public void setPcLocationConvertor(LocationConvertor pcLocationConvertor) {
		this.pcLocationConvertor = pcLocationConvertor;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
