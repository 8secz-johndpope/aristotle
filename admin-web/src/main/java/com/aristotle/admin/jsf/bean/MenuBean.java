package com.aristotle.admin.jsf.bean;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.aristotle.admin.service.AdminService;
import com.aristotle.core.enums.AppPermission;
import com.aristotle.core.persistance.Location;
import com.aristotle.core.persistance.User;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
// @URLBeanName("menuBean")
public class MenuBean extends BaseJsfBean {

    private Location selectedLocation;
    private List<Location> adminLocations;
    private Set<AppPermission> allPermissions;
    private Map<Long, Set<AppPermission>> locationPermissions;
    private User user;
    private boolean globalSelected = false;

    @Autowired
    private AdminService adminService;

    public void refreshLoginRoles() {
        try {
            adminLocations = adminService.getUserAdminLocations(user.getId());

            allPermissions = adminService.getGlobalPermissionsOfUser(user.getId());
            locationPermissions = adminService.getLocationPermissionsOfUser(user.getId());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean isAllowed(AppPermission... appPermissions) {
        // If no location Selected then return false as we dont want to display any menu item
        if (isLocationNotSelected(this)) {
            return false;
        }
        if (user.isSuperAdmin()) {
            return true;
        }
        Set<AppPermission> permissions = null;
        if (selectedLocation == null) {
            permissions = allPermissions;
        } else {
            permissions = locationPermissions.get(selectedLocation.getId());
        }

        if (permissions == null) {
            return false;
        }
        for (AppPermission oneAPermission : appPermissions) {
            if (permissions.contains(oneAPermission)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAdmin() {
        return (user.isSuperAdmin() || !allPermissions.isEmpty() || !locationPermissions.isEmpty());
    }
    public boolean isVoiceOfAapFbAllowed() {
        return isAllowed(AppPermission.ADMIN_VOICE_OF_AAP_FB);
    }

    public boolean isVoiceOfAapTwitterAllowed() {
        return isAllowed(AppPermission.ADMIN_VOICE_OF_AAP_TWITTER);
    }

    public boolean isCampaignAllowed() {
        return isVoiceOfAapFbAllowed() || isVoiceOfAapTwitterAllowed() || isEmailAllowed() || isSmsAllowed() || isGlobalDonationCampaignAllowed() || isCandidateAllowed();
    }

    public boolean isEmailAllowed() {
        return isAllowed(AppPermission.ADMIN_EMAIL);
    }

    public boolean isSmsAllowed() {
        return isAllowed(AppPermission.ADMIN_SMS);
    }

    public boolean isGlobalDonationCampaignAllowed() {
        return isAllowed(AppPermission.ADMIN_GLOBAL_CAMPAIGN);
    }

    public boolean isCandidateAllowed() {
        return isAllowed(AppPermission.ADMIN_CANDIDATE_PC);
    }

    public boolean isContentAllowed() {
        return isManageNewsAllowed() || isManageBlogAllowed() || isManagePollAllowed() || isManageEventAllowed();
    }

    public boolean isManageNewsAllowed() {
        return isAllowed(AppPermission.CREATE_NEWS, AppPermission.UPDATE_NEWS, AppPermission.DELETE_NEWS, AppPermission.APPROVE_NEWS);
    }

    public boolean isEditNewsAllowed() {
        return isAllowed(AppPermission.CREATE_NEWS, AppPermission.UPDATE_NEWS, AppPermission.DELETE_NEWS, AppPermission.APPROVE_NEWS);
    }

    public boolean isPublishNewsAllowed() {
        return isAllowed(AppPermission.APPROVE_NEWS);
    }

    public boolean isEditBlogAllowed() {
        return isAllowed(AppPermission.CREATE_BLOG, AppPermission.UPDATE_BLOG, AppPermission.DELETE_BLOG, AppPermission.APPROVE_BLOG);
    }

    public boolean isPublishBlogAllowed() {
        return isAllowed(AppPermission.APPROVE_BLOG);
    }

    public boolean isManageBlogAllowed() {
        return isAllowed(AppPermission.CREATE_BLOG, AppPermission.UPDATE_BLOG, AppPermission.DELETE_BLOG, AppPermission.APPROVE_BLOG);
    }

    public boolean isManagePollAllowed() {
        return isAllowed(AppPermission.CREATE_POLL, AppPermission.UPDATE_POLL, AppPermission.DELETE_POLL, AppPermission.APPROVE_POLL);
    }

    public boolean isManageEventAllowed() {
        return isAllowed(AppPermission.ADMIN_EVENT);
    }

    public boolean isGlobalAdmin() {
        return !allPermissions.isEmpty();
    }

    public boolean isDeveloperAllowed() {
        return isWebDeveloperRoleAllowed();
    }

    public boolean isWebDeveloperRoleAllowed() {
        return isAllowed(AppPermission.WEB_ADMIN_DRAFT);
    }

    public boolean isWebDeveloperAdminRoleAllowed() {
        return isAllowed(AppPermission.WEB_ADMIN);
    }

    public boolean isAdminAllowed() {
        return isManageUserRoleAllowed() || isEditOfficeDetailAllowed() || isSearchVolunteerAllowed() || isEditTeamAllowed() || isEditUserAllowed();
    }

    public boolean isManageUserRoleAllowed() {
        return isAllowed(AppPermission.EDIT_USER_ROLES);
    }

    public boolean isEditOfficeDetailAllowed() {
        return isAllowed(AppPermission.EDIT_OFFICE_ADDRESS);
    }

    public boolean isSearchVolunteerAllowed() {
        return isAllowed(AppPermission.SEARCH_MEMBER);
    }

    public boolean isEditTeamAllowed() {
        return isAllowed(AppPermission.EDIT_TEAM);
    }

    public boolean isEditUserAllowed() {
        return isAllowed(AppPermission.ADD_MEMBER, AppPermission.UPDATE_MEMBER, AppPermission.UPDATE_GLOBAL_MEMBER);
    }

    public boolean isCallCampaignAllowed() {
        return isAllowed(AppPermission.CALL_CAMPAIGN_ADMIN);
    }

    public void goToVoiceOfAapAdminPageFb() {
        if (isVoiceOfAapFbAllowed()) {
            buildAndRedirect("/admin/voiceofaapfb");
        } else {
            buildAndRedirect("/admin/notallowed");
        }
    }

    public void goToVoiceOfAapAdminPageTwitter() {
        if (isVoiceOfAapTwitterAllowed()) {
            buildAndRedirect("/admin/voiceofaaptwitter");
        } else {
            buildAndRedirect("/admin/notallowed");
        }
    }

    public void goToEmailAdminPageTwitter() {
        if (isEmailAllowed()) {
            buildAndRedirect("/admin/email");
        } else {
            buildAndRedirect("/admin/notallowed");
        }
    }

    public void goToSmsAdminPageTwitter() {
        if (isSmsAllowed()) {
            buildAndRedirect("/admin/sms");
        } else {
            buildAndRedirect("/admin/notallowed");
        }
    }

    public void goToMobileGroupPage() {
        if (isSmsAllowed()) {
            buildAndRedirect("/admin/mobilegroup");
        } else {
            buildAndRedirect("/admin/notallowed");
        }
    }

    public void goToGlobalDonationcampaignAdminPage() {
        if (isGlobalDonationCampaignAllowed()) {
            buildAndRedirect("/admin/globalcampaign");
        } else {
            buildAndRedirect("/admin/notallowed");
        }
    }

    public void goToCandidateAdminPage() {
        if (isCandidateAllowed()) {
            buildAndRedirect("/admin/candidate");
        } else {
            buildAndRedirect("/admin/notallowed");
        }
    }
    public void goToManageNewsPage() { 
        if (isManageNewsAllowed()) { 
            buildAndRedirect("/admin/news"); 
            } else { 
                buildAndRedirect("/admin/notallowed"); 
        } 
    }

    public void goToManageBlogPage() {
        if (isManageBlogAllowed()) {
            buildAndRedirect("/admin/blog");
        } else {
            buildAndRedirect("/admin/notallowed");
        }
    }

    public void goToManagePollPage() {
        if (isManagePollAllowed()) {
            buildAndRedirect("/admin/poll");
        } else {
            buildAndRedirect("/admin/notallowed");
        }
    }

    public void goToManageEventPage() {
        if (isManageEventAllowed()) {
            buildAndRedirect("/admin/event");
        } else {
            buildAndRedirect("/admin/notallowed");
        }
    }

    public void selectLocation(ActionEvent event) {
        selectedLocation = ((Location) event.getComponent().getAttributes().get("selectedLocation"));
        globalSelected = false;
        System.out.println("globalSelected=" + globalSelected);
        System.out.println("selectedLocation=" + selectedLocation);
        buildAndRedirect("/admin/home");
    }

    public void selectGlobal(ActionEvent event) {
        globalSelected = true;
        selectedLocation = null;
        System.out.println("globalSelected=" + globalSelected);
        System.out.println("selectedLocation=" + selectedLocation);
        buildAndRedirect("/admin/home");
    }

    public void goToHtmlTemplatePage() {
        if (isWebDeveloperRoleAllowed()) {
            buildAndRedirect("/admin/templates");
        } else {
            buildAndRedirect("/admin/notallowed");
        }
    }
    
    public void goToStaticDataPluginPage() {
        if (isWebDeveloperAdminRoleAllowed()) {
            buildAndRedirect("/admin/sdplugin");
        } else {
            buildAndRedirect("/admin/notallowed");
        }
    }

    public void goToHtmlPartPage() {
        if (isWebDeveloperRoleAllowed()) {
            buildAndRedirect("/admin/htmlpart");
        } else {
            buildAndRedirect("/admin/notallowed");
        }
    }

    public void goToUrlMappingPage() {
        if (isWebDeveloperAdminRoleAllowed()) {
            buildAndRedirect("/admin/urls");
        } else {
            buildAndRedirect("/admin/notallowed");
        }
    }


    public void goToEditOfficeDetailPage() {
        if (isEditOfficeDetailAllowed()) {
            buildAndRedirect("/admin/office");
        } else {
            buildAndRedirect("/admin/notallowed");
        }
    }

    public void goToSearchVolunteerPage() {
        if (isSearchVolunteerAllowed()) {
            buildAndRedirect("/admin/search");
        } else {
            buildAndRedirect("/admin/notallowed");
        }
    }

    public void goToTeamsPage() {
        if (isEditTeamAllowed()) {
            buildAndRedirect("/admin/teams");
        } else {
            buildAndRedirect("/admin/notallowed");
        }
    }

    public void goToManagerUserPage() {
        if (isEditUserAllowed()) {
            buildAndRedirect("/admin/edituser");
        } else {
            buildAndRedirect("/admin/notallowed");
        }
    }

    public void goToCallCampaignPage() {
        if (isCallCampaignAllowed()) {
            buildAndRedirect("/admin/call");
        } else {
            buildAndRedirect("/admin/notallowed");
        }
    }


    public void goToManageUserRolePage() {
        if (isManageUserRoleAllowed()) {
            buildAndRedirect("/admin/roles");
        } else {
            buildAndRedirect("/admin/notallowed");
        }
    }

    /*
     * private static final long serialVersionUID = 1L;
     * 
     * private Long adminSelectedLocationId = -1L; private Location selectedLocation;
     * 
     * private Long selectedLocationId;
     * 
     * @Autowired protected AdminService adminService;
     * 
     * @Autowired private MenuBean menuBean;
     * 
     * 
     * public static HttpServletRequest getHttpServletRequest() { return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest(); }
     * 
     * @Override public UserSessionBean getUserRolePermissionInSesion() { HttpServletRequest httpServletRequest = getHttpServletRequest(); return getUserRolePermissionInSesion(httpServletRequest); }
     * 
     * @Override protected User getLoggedInUser() { return getLoggedInUser(false, ""); }
     * 
     * @Override protected User getLoggedInUser(boolean redirect, String url) { HttpServletRequest httpServletRequest = getHttpServletRequest(); User user =
     * getLoggedInUserFromSesion(httpServletRequest); if (user == null) { if (redirect) { redirect(url); } } return user; }
     * 
     * 
     * public boolean isAdmin() { UserRolePermissionDto userRolePermissionDto = getUserRolePermissionInSesion(); if (userRolePermissionDto == null) { return false; } return
     * userRolePermissionDto.isAdmin();
     * 
     * }
     * 
     * public boolean isSuperUser() { UserRolePermissionDto userRolePermissionDto = getLoggedInUser(); if (userRolePermissionDto == null) { return false; } return userRolePermissionDto.isSuperUser();
     * }
     * 
     * public boolean isStateAdmin() { UserRolePermissionDto userRolePermissionDto = getUserRolePermissionInSesion(); if (userRolePermissionDto == null) { return false; } return
     * userRolePermissionDto.isStateAdmin(); }
     * 
     * public boolean isDistrictAdmin() { UserRolePermissionDto userRolePermissionDto = getUserRolePermissionInSesion(); if (userRolePermissionDto == null) { return false; } return
     * userRolePermissionDto.isDistrictAdmin(); }
     * 
     * public boolean isAcAdmin() { UserRolePermissionDto userRolePermissionDto = getUserRolePermissionInSesion(); if (userRolePermissionDto == null) { return false; } return
     * userRolePermissionDto.isAcAdmin(); }
     * 
     * public boolean isPcAdmin() { UserRolePermissionDto userRolePermissionDto = getUserRolePermissionInSesion(); if (userRolePermissionDto == null) { return false; } return
     * userRolePermissionDto.isPcAdmin(); }
     * 
     * public String getContext() { HttpServletRequest httpServletRequest = getHttpServletRequest(); return httpServletRequest.getContextPath(); }
     * 
     * public boolean isLoggedIn() { UserDto user = getLoggedInUser(); if (user == null) { return false; } return true; }
     * 
     * public boolean isAllAdmin() { UserRolePermissionDto userRolePermissionDto = getUserRolePermissionInSesion(); if (userRolePermissionDto == null) { return false; } return
     * userRolePermissionDto.isAllAdmin(); }
     * 
     * public List<StateDto> getAdminStates() { UserRolePermissionDto userRolePermissionDto = getUserRolePermissionInSesion(); return userRolePermissionDto.getAdminStates(); }
     * 
     * public List<DistrictDto> getAdminDistricts() { UserRolePermissionDto userRolePermissionDto = getUserRolePermissionInSesion(); return userRolePermissionDto.getAdminDistricts(); }
     * 
     * public List<AssemblyConstituencyDto> getAdminAcs() { UserRolePermissionDto userRolePermissionDto = getUserRolePermissionInSesion(); return userRolePermissionDto.getAdminAcs(); }
     * 
     * public List<ParliamentConstituencyDto> getAdminPcs() { UserRolePermissionDto userRolePermissionDto = getUserRolePermissionInSesion(); return userRolePermissionDto.getAdminPcs(); }
     * 
     * public List<CountryDto> getAdminCountries() { UserRolePermissionDto userRolePermissionDto = getUserRolePermissionInSesion(); return userRolePermissionDto.getAdminCountries(); }
     * 
     * public List<CountryRegionDto> getAdminCountryRegions() { UserRolePermissionDto userRolePermissionDto = getUserRolePermissionInSesion(); return userRolePermissionDto.getAdminCountryRegions(); }
     * 
     * 
     * 
     * public void selectCurrent(ActionEvent event) { if (locationType == null || locationType == PostLocationType.Global || adminSelectedLocationId <= 0) {
     * sendErrorMessageToJsfScreen("Please select a Location"); } else { switch (locationType) { case STATE: selectState(adminSelectedLocationId); break; case DISTRICT:
     * selectDistrict(adminSelectedLocationId); break; case AC: selectAc(adminSelectedLocationId); break; case PC: selectPc(adminSelectedLocationId); break; } buildAndRedirect("/admin/location"); }
     * 
     * }
     * 
     * 
     * 
     * 
     * 
     * public void selectDistrict(ActionEvent event) { locationType = PostLocationType.DISTRICT; selectDistrict((Long) event.getComponent().getAttributes().get("districtId"));
     * buildAndRedirect("/admin/location"); }
     * 
     * public void selectDistrict(Long districtId) { locationType = PostLocationType.DISTRICT; adminSelectedLocationId = districtId; selectedAdminDistrict =
     * aapService.getDistrictById(adminSelectedLocationId); selectedAdminState = aapService.getStateById(selectedAdminDistrict.getStateId()); }
     * 
     * public void selectAc(ActionEvent event) { locationType = PostLocationType.AC; selectAc((Long) event.getComponent().getAttributes().get("acId")); buildAndRedirect("/admin/location"); } public
     * void selectAc(Long acId) { locationType = PostLocationType.AC; adminSelectedLocationId = acId; selectedAdminAc = aapService.getAssemblyConstituencyById(adminSelectedLocationId);
     * selectedAdminDistrict = aapService.getDistrictById(selectedAdminAc.getDistrictId()); selectedAdminState = aapService.getStateById(selectedAdminDistrict.getStateId()); }
     * 
     * public void selectPc(ActionEvent event) { locationType = PostLocationType.PC; selectPc((Long) event.getComponent().getAttributes().get("pcId")); buildAndRedirect("/admin/location"); } public
     * void selectPc(Long pcId) { locationType = PostLocationType.PC; adminSelectedLocationId = pcId; selectedAdminPc = aapService.getParliamentConstituencyById(adminSelectedLocationId);
     * selectedAdminState = aapService.getStateById(selectedAdminPc.getStateId()); }
     * 
     * public LoginAccountDto getLoginAccounts() { return getLoggedInAccountsFromSesion(); }
     * 
     * public UserDto getUser() { UserDto user = getLoggedInUser(); if(user == null){ user = new UserDto();
     * user.setProfilePic("https://cdn2.iconfinder.com/data/icons/ios-7-icons/50/user_male2-256.png"); user.setName("Guest"); } return user; }
     * 
     * 
     * 
     * 
     * 
     * public void goToManageMemberPage() { if (isManageMemberAllowed()) { buildAndRedirect("/admin/register"); } else { buildAndRedirect("/admin/notallowed"); } }
     * 
     * public void goToSearchMemberPage() { if (isManageMemberAllowed()) { buildAndRedirect("/admin/search"); } else { buildAndRedirect("/admin/notallowed"); } }
     * 
     * public void goToTreasuryPage() { if (isTreasuryAllowed()) { buildAndRedirect("/admin/treasury"); } else { buildAndRedirect("/admin/notallowed"); } }
     * 
     * public void goToTreasuryAccountDetailPage() { if (isTreasuryAllowed()) { buildAndRedirect("/admin/treasurydetail"); } else { buildAndRedirect("/admin/notallowed"); } }
     * 
     * 
     * 
     * 
     * 
     * public boolean isCampaignAllowed() { return isVoiceOfAapFbAllowed() || isVoiceOfAapTwitterAllowed() || isEmailAllowed() || isSmsAllowed() || isGlobalDonationCampaignAllowed() ||
     * isCandidateAllowed(); }
     * 
     * public boolean isVoiceOfAapFbAllowed() { UserRolePermissionDto userRolePermissionDto = getUserRolePermissionInSesion(); return ClientPermissionUtil.isVoiceOfAapFbAllowed(userRolePermissionDto,
     * adminSelectedLocationId, locationType); }
     * 
     * public boolean isVoiceOfAapTwitterAllowed() { UserRolePermissionDto userRolePermissionDto = getUserRolePermissionInSesion(); return
     * ClientPermissionUtil.isVoiceOfAapTwitterAllowed(userRolePermissionDto, adminSelectedLocationId, locationType); }
     * 
     * 
     * 
     * 
     * 
     * 
     * public boolean isMemberAllowed() { return isManageMemberAllowed(); }
     * 
     * public boolean isManageMemberAllowed() { UserRolePermissionDto userRolePermissionDto = getUserRolePermissionInSesion(); return ClientPermissionUtil.isManageMemberAllowed(userRolePermissionDto,
     * adminSelectedLocationId, locationType); }
     * 
     * public boolean isTreasuryAllowed() { UserRolePermissionDto userRolePermissionDto = getUserRolePermissionInSesion(); return ClientPermissionUtil.isTreasuryAllowed(userRolePermissionDto,
     * adminSelectedLocationId, locationType); }
     * 
     * 
     * 
     * 
     * 
     * public PostLocationType getLocationType() { return locationType; }
     * 
     * public String getCurrentLocationName() { if (locationType == null) { return "None"; } switch (locationType) { case Global: return "Global"; case STATE: return selectedAdminState.getName(); case
     * DISTRICT: return selectedAdminDistrict.getName(); case AC: return selectedAdminAc.getName(); case PC: return selectedAdminPc.getName(); } return "No Location Selected"; }
     * 
     * public Long getAdminSelectedLocationId() { return adminSelectedLocationId; }
     * 
     * public void setAdminSelectedLocationId(Long adminSelectedLocationId) { this.adminSelectedLocationId = adminSelectedLocationId; }
     * 
     * public StateDto getSelectedAdminState() { return selectedAdminState; }
     * 
     * public void setSelectedAdminState(StateDto selectedAdminState) { this.selectedAdminState = selectedAdminState; }
     * 
     * public DistrictDto getSelectedAdminDistrict() { return selectedAdminDistrict; }
     * 
     * public void setSelectedAdminDistrict(DistrictDto selectedAdminDistrict) { this.selectedAdminDistrict = selectedAdminDistrict; }
     * 
     * public AssemblyConstituencyDto getSelectedAdminAc() { return selectedAdminAc; }
     * 
     * public void setSelectedAdminAc(AssemblyConstituencyDto selectedAdminAc) { this.selectedAdminAc = selectedAdminAc; }
     * 
     * public ParliamentConstituencyDto getSelectedAdminPc() { return selectedAdminPc; }
     * 
     * public void setSelectedAdminPc(ParliamentConstituencyDto selectedAdminPc) { this.selectedAdminPc = selectedAdminPc; }
     * 
     * public void setLocationType(PostLocationType locationType) { this.locationType = locationType; }
     * 
     * public CountryDto getSelectedAdminCountry() { return selectedAdminCountry; }
     * 
     * public void setSelectedAdminCountry(CountryDto selectedAdminCountry) { this.selectedAdminCountry = selectedAdminCountry; }
     * 
     * public CountryRegionDto getSelectedAdminCountryRegion() { return selectedAdminCountryRegion; }
     * 
     * public void setSelectedAdminCountryRegion(CountryRegionDto selectedAdminCountryRegion) { this.selectedAdminCountryRegion = selectedAdminCountryRegion; }
     * 
     * public List<CountryDto> getNriCountries() { return aapService.getNriCountries(); }
     * 
     * public List<StateDto> getStates() { return aapService.getAllStates(); }
     * 
     * public List<DistrictDto> getDistrictOfState() { if (selectedStateId == null) { return new ArrayList<>(); } return aapService.getAllDistrictOfState(selectedStateId); }
     * 
     * public List<AssemblyConstituencyDto> getAssemblyConstituencyOfDistrict() { if (selectedDistrictId == null) { return new ArrayList<>(); } return
     * aapService.getAllAssemblyConstituenciesOfDistrict(selectedDistrictId); }
     * 
     * public List<ParliamentConstituencyDto> getParliamentConstituencyOfState() { if (selectedStateId == null) { return new ArrayList<>(); } return
     * aapService.getAllParliamentConstituenciesOfState(selectedStateId); }
     * 
     * public List<CountryRegionDto> getCountryRegionOfCountry() { if (selectedCountryId == null) { return new ArrayList<>(); } return aapService.getAllCountryRegionsOfCountry(selectedCountryId); }
     * 
     * public List<CountryRegionAreaDto> getCountryRegionAreaOfCountryRegion() { if (selectedCountryRegiontId == null) { return new ArrayList<>(); } return
     * aapService.getAllCountryRegionAreasOfCountryRegion(selectedCountryRegiontId); }
     * 
     * public void handleStateChange(AjaxBehaviorEvent event) { if (selectedStateId == 0 || selectedStateId == null) { locationType = PostLocationType.NA; adminSelectedLocationId = 0L; } else {
     * locationType = PostLocationType.STATE; adminSelectedLocationId = selectedStateId; } }
     * 
     * public void handleDistrictChange(AjaxBehaviorEvent event) { if (selectedDistrictId == 0 || selectedDistrictId == null) { locationType = PostLocationType.NA; adminSelectedLocationId = 0L; } else
     * { locationType = PostLocationType.DISTRICT; adminSelectedLocationId = selectedDistrictId; } }
     * 
     * public void handleAcChange(AjaxBehaviorEvent event) { if (selectedAcId == 0 || selectedAcId == null) { locationType = PostLocationType.NA; adminSelectedLocationId = 0L; } else { locationType =
     * PostLocationType.AC; adminSelectedLocationId = selectedAcId; } }
     * 
     * public void handlePcChange(AjaxBehaviorEvent event) { if (selectedPcId == 0 || selectedPcId == null) { locationType = PostLocationType.NA; adminSelectedLocationId = 0L; } else { locationType =
     * PostLocationType.PC; adminSelectedLocationId = selectedPcId; } }
     * 
     * public void handleCountryChange(AjaxBehaviorEvent event) { if (selectedCountryId == 0 || selectedCountryId == null) { locationType = PostLocationType.NA; adminSelectedLocationId = 0L; } else {
     * locationType = PostLocationType.COUNTRY; adminSelectedLocationId = selectedCountryId; } }
     * 
     * public void handleCountryRegionChange(AjaxBehaviorEvent event) { if (selectedCountryRegiontId == 0 || selectedCountryRegiontId == null) { locationType = PostLocationType.NA;
     * adminSelectedLocationId = 0L; } else { locationType = PostLocationType.REGION; adminSelectedLocationId = selectedCountryRegiontId; } }
     * 
     * public Long getSelectedDistrictId() { return selectedDistrictId; }
     * 
     * public void setSelectedDistrictId(Long selectedDistrictId) { this.selectedDistrictId = selectedDistrictId; }
     * 
     * public Long getSelectedAcId() { return selectedAcId; }
     * 
     * public void setSelectedAcId(Long selectedAcId) { this.selectedAcId = selectedAcId; }
     * 
     * public Long getSelectedPcId() { return selectedPcId; }
     * 
     * public void setSelectedPcId(Long selectedPcId) { this.selectedPcId = selectedPcId; }
     * 
     * public Long getSelectedCountrytId() { return selectedCountryId; }
     * 
     * public void setSelectedCountrytId(Long selectedCountrytId) { this.selectedCountryId = selectedCountrytId; }
     * 
     * public Long getSelectedCountryRegiontId() { return selectedCountryRegiontId; }
     * 
     * public void setSelectedCountryRegiontId(Long selectedCountryRegiontId) { this.selectedCountryRegiontId = selectedCountryRegiontId; }
     * 
     * public Long getSelectedStateId() { return selectedStateId; }
     * 
     * public void setSelectedStateId(Long selectedStateId) { this.selectedStateId = selectedStateId; }
     * 
     * public Long getSelectedCountryId() { return selectedCountryId; }
     * 
     * public void setSelectedCountryId(Long selectedCountryId) { this.selectedCountryId = selectedCountryId; } public AapService getAapService() { return aapService; } public void
     * setAapService(AapService aapService) { this.aapService = aapService; }
     */

    public Location getSelectedLocation() {
        return selectedLocation;
    }

    public void setSelectedLocation(Location selectedLocation) {
        this.selectedLocation = selectedLocation;
    }

    public List<Location> getAdminLocations() {
        return adminLocations;
    }

    public void setAdminLocations(List<Location> adminLocations) {
        this.adminLocations = adminLocations;
        if (adminLocations.size() == 1) {
            selectedLocation = adminLocations.get(0);
        }
    }

    public Set<AppPermission> getAllPermissions() {
        return allPermissions;
    }

    public void setAllPermissions(Set<AppPermission> allPermissions) {
        this.allPermissions = allPermissions;
    }

    public Map<Long, Set<AppPermission>> getLocationPermissions() {
        return locationPermissions;
    }

    public void setLocationPermissions(Map<Long, Set<AppPermission>> locationPermissions) {
        this.locationPermissions = locationPermissions;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isGlobalSelected() {
        return globalSelected;
    }

    public void setGlobalSelected(boolean globalSelected) {
        this.globalSelected = globalSelected;
    }
}
