package com.aristotle.admin.jsf.bean;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.aristotle.core.enums.AppPermission;
import com.aristotle.core.enums.PlannedPostStatus;
import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.GroupPlannedSms;
import com.aristotle.core.persistance.LocationPlannedSms;
import com.aristotle.core.persistance.MobileGroup;
import com.aristotle.core.persistance.PlannedSms;
import com.aristotle.core.persistance.SmsTemplate;
import com.aristotle.core.persistance.Team;
import com.aristotle.core.persistance.TeamPlannedSms;
import com.aristotle.core.service.SmsService;
import com.aristotle.core.service.TeamService;
import com.google.gdata.util.common.base.StringUtil;
import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLBeanName;
import com.ocpsoft.pretty.faces.annotation.URLMapping;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "view")
@URLMapping(id = "adminSmsBean", beanName = "adminSmsBean", pattern = "/admin/sms", viewId = "/admin/admin_sms.xhtml")
@URLBeanName("adminSmsBean")
public class AdminSmsBean extends BaseMultiPermissionAdminJsfBean {

	private static final long serialVersionUID = 1L;

    private PlannedSms selectedPlannedSms;

	private int pageNumber = 1;
	private int pageSize = 20;
	private boolean showList = true;

    private List<PlannedSms> plannedSmss;
    private Map<String, MobileGroup> mobileGroupNameToMobileGroups;
    private Map<String, String> mobileGroups;
    private String selectedGroupName;
    private String selectedTargetType;

    private Map<String, Team> teamNameToTeam;
    private Map<String, String> teams;
    private String selectedTeamName;

    private Map<String, SmsTemplate> smsTemplateNameToSmsTemplate;
    private Map<String, String> smsTemplates;
    private String selectedSmsTemplate;
    private boolean editSmsAllowed;

    private boolean showGroupSelectionPanel;
    private boolean showTeamSelectionPanel;
    private boolean showLocationSelectionPanel;

    @Autowired
    private SmsService smsService;
    @Autowired
    private TeamService teamService;

	public AdminSmsBean() {
        super("/admin/sms", AppPermission.ADMIN_SMS);
	}

	// @URLActions(actions = { @URLAction(mappingId = "userProfileBean") })
	@URLAction(onPostback = false)
	public void init() throws Exception {
		if (!checkUserAccess()) {
			return;
		}
		refreshSmsList();
        refreshGroupList();
        refreshTeamList();
        refreshSmsTemplateList();
	}
	private void refreshSmsList(){
        // plannedSmss = smsService.getPlannedSmssForLocation(menuBean.getLocationType(), menuBean.getAdminSelectedLocationId(), pageNumber, pageSize);
        try {
            plannedSmss = smsService.getAllPlannedSms();
        } catch (AppException e) {
            sendErrorMessageToJsfScreen(e);
        }
	}

    private void refreshSmsTemplateList() {
        // plannedSmss = smsService.getPlannedSmssForLocation(menuBean.getLocationType(), menuBean.getAdminSelectedLocationId(), pageNumber, pageSize);
        try {
            List<SmsTemplate> dbSmsTemplates = smsService.getAllSmsTemplates();
            smsTemplates = new LinkedHashMap<>();
            smsTemplateNameToSmsTemplate = new HashMap<>();
            for (SmsTemplate oneSmsTemplate : dbSmsTemplates) {
                smsTemplates.put(oneSmsTemplate.getName(), oneSmsTemplate.getSystemName());
                smsTemplateNameToSmsTemplate.put(oneSmsTemplate.getSystemName(), oneSmsTemplate);
            }
            // Add Custom template
            SmsTemplate customTemplate = new SmsTemplate();
            customTemplate.setMessage("");
            customTemplate.setName("Custom");
            customTemplate.setStatus("Approved");
            customTemplate.setSystemName("Custom");
            customTemplate.setId(-1L);
            smsTemplates.put(customTemplate.getName(), customTemplate.getSystemName());
            smsTemplateNameToSmsTemplate.put(customTemplate.getSystemName(), customTemplate);

        } catch (Exception e) {
            sendErrorMessageToJsfScreen(e);
        }
    }

    private void refreshGroupList() {
        // plannedSmss = smsService.getPlannedSmssForLocation(menuBean.getLocationType(), menuBean.getAdminSelectedLocationId(), pageNumber, pageSize);
        try {
            List<MobileGroup> dbMobileGroups = smsService.getAllMobileGroups();
            mobileGroups = new LinkedHashMap<String, String>();
            mobileGroupNameToMobileGroups = new HashMap<String, MobileGroup>();
            for (MobileGroup oneMobileGroup : dbMobileGroups) {
                mobileGroups.put(oneMobileGroup.getName(), oneMobileGroup.getName());
                mobileGroupNameToMobileGroups.put(oneMobileGroup.getName(), oneMobileGroup);
            }
        } catch (AppException e) {
            sendErrorMessageToJsfScreen(e);
        }
    }

    private void refreshTeamList() {
        // plannedSmss = smsService.getPlannedSmssForLocation(menuBean.getLocationType(), menuBean.getAdminSelectedLocationId(), pageNumber, pageSize);
        try {
            List<Team> dbTeams = teamService.getAllGlobalTeams();
            teams = new LinkedHashMap<String, String>();
            teamNameToTeam = new HashMap<String, Team>();
            for (Team oneTeam : dbTeams) {
                teams.put(oneTeam.getName(), oneTeam.getName());
                teamNameToTeam.put(oneTeam.getName(), oneTeam);
            }
        } catch (AppException e) {
            sendErrorMessageToJsfScreen(e);
        }
    }

    public void saveSms() {
		try {
            // selectedPlannedSms.setLocationType(menuBean.getSelectedLocation().getLocationType());
            if (menuBean.getSelectedLocation() != null) {
                selectedPlannedSms.setLocationId(menuBean.getSelectedLocation().getId());
            }

            if (StringUtils.isEmpty(selectedSmsTemplate)) {
                sendErrorMessageToJsfScreen("Please select SMS template");
            }
			if (StringUtil.isEmpty(selectedPlannedSms.getMessage())) {
				sendErrorMessageToJsfScreen("Please enter a Message");
			}
			if (selectedPlannedSms.getMessage().length() > 140) {
                sendErrorMessageToJsfScreen("Please enter a message equal or less then 140 Characters");
			}
            if (StringUtils.isEmpty(selectedTargetType)) {
                sendErrorMessageToJsfScreen("Please select target SMS type");
            }
			
			if (selectedPlannedSms.getPostingTime() == null) {
				sendErrorMessageToJsfScreen("Please enter when you want to sms it by choosing correct time in future");
			} else {
				Calendar today = Calendar.getInstance();
				if (today.getTime().after(selectedPlannedSms.getPostingTime())) {
					sendErrorMessageToJsfScreen("Please enter posting time in future");
				}
			}
            selectedPlannedSms.setSmsTemplate(smsTemplateNameToSmsTemplate.get(selectedSmsTemplate));

            switch (selectedTargetType) {
            case "TeamSms":
                saveTeamSms();
                break;
            case "GroupSms":
                saveGroupSms();
                break;
            case "LocationSms":
                sendErrorMessageToJsfScreen("Not Implemented yet");
                break;
            default:
                selectedPlannedSms = copyPlannedSms(new PlannedSms());
                break;
            }

		} catch (Exception ex) {
            sendErrorMessageToJsfScreen("Unable to save SMS", ex);
		}

	}

    private void saveGroupSms() throws AppException {
        if (StringUtils.isEmpty(selectedGroupName) || mobileGroupNameToMobileGroups.get(selectedGroupName) == null) {
            sendErrorMessageToJsfScreen("Please Select a Group");
        }

        if (isValidInput()) {
            MobileGroup selectedMobileGroup = mobileGroupNameToMobileGroups.get(selectedGroupName);
            GroupPlannedSms groupPlannedSms = (GroupPlannedSms) selectedPlannedSms;
            groupPlannedSms.setMobileGroup(selectedMobileGroup);
            selectedPlannedSms = smsService.saveGroupPlannedSms(groupPlannedSms);
            sendInfoMessageToJsfScreen("Group Sms saved succesfully");
            refreshSmsList();
            showList = true;
        }
    }

    private void saveTeamSms() throws AppException {
        if (StringUtils.isEmpty(selectedTeamName) || teamNameToTeam.get(selectedTeamName) == null) {
            sendErrorMessageToJsfScreen("Please Select a Team");
        }

        if (isValidInput()) {
            Team selectedTeam = teamNameToTeam.get(selectedTeamName);
            TeamPlannedSms teamPlannedSms = (TeamPlannedSms) selectedPlannedSms;
            teamPlannedSms.setTeam(selectedTeam);

            selectedPlannedSms = smsService.saveTeamPlannedSms(teamPlannedSms);
            sendInfoMessageToJsfScreen("Team Sms saved succesfully");
            refreshSmsList();
            showList = true;
        }
    }

	public void newPost() {
        selectedPlannedSms = new PlannedSms();
        selectedPlannedSms.setStatus(PlannedPostStatus.PENDING);
		showList = false;
	}

	public void clear() {
		newPost();
	}

	public void cancel() {
		newPost();
		showList = true;
	}

    public void handleSmsTargetSelection() {
        switch (selectedTargetType) {
        case "TeamSms":
            showGroupSelectionPanel = false;
            showTeamSelectionPanel = true;
            showLocationSelectionPanel = false;
            selectedPlannedSms = copyPlannedSms(new TeamPlannedSms());
            break;
        case "GroupSms":
            showGroupSelectionPanel = true;
            showTeamSelectionPanel = false;
            showLocationSelectionPanel = false;
            selectedPlannedSms = copyPlannedSms(new GroupPlannedSms());
            break;
        case "LocationSms":
            showGroupSelectionPanel = false;
            showTeamSelectionPanel = false;
            showLocationSelectionPanel = true;
            selectedPlannedSms = copyPlannedSms(new LocationPlannedSms());
            break;
        default:
            selectedPlannedSms = copyPlannedSms(new PlannedSms());
            break;
        }
    }

    public void handleSmsTemplateSelection() {
        switch (selectedSmsTemplate) {
        case "Custom":
            editSmsAllowed = true;
            break;
        default:
            editSmsAllowed = false;
            break;
        }
        SmsTemplate smsTemplate = smsTemplateNameToSmsTemplate.get(selectedSmsTemplate);
        if (smsTemplate == null) {
            selectedPlannedSms.setMessage("");
        } else {
            selectedPlannedSms.setMessage(smsTemplate.getMessage());
        }
    }

    private PlannedSms copyPlannedSms(PlannedSms targettedPlannedSms){
        BeanUtils.copyProperties(selectedPlannedSms, targettedPlannedSms);
        return targettedPlannedSms;
    }
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public boolean isShowList() {
		return showList;
	}

	public void setShowList(boolean showList) {
		this.showList = showList;
	}

    public PlannedSms getSelectedPlannedSms() {
		return selectedPlannedSms;
	}

    public void setSelectedPlannedSms(PlannedSms selectedPlannedSms) {
		this.selectedPlannedSms = selectedPlannedSms;
		showList = false;
	}

    public List<PlannedSms> getPlannedSmss() {
		return plannedSmss;
	}

    public void setPlannedSmss(List<PlannedSms> plannedSmss) {
		this.plannedSmss = plannedSmss;
	}

    public String getSelectedTargetType() {
        return selectedTargetType;
    }

    public void setSelectedTargetType(String selectedTargetType) {
        this.selectedTargetType = selectedTargetType;
    }

    public boolean isShowGroupSelectionPanel() {
        return showGroupSelectionPanel;
    }

    public void setShowGroupSelectionPanel(boolean showGroupSelectionPanel) {
        this.showGroupSelectionPanel = showGroupSelectionPanel;
    }

    public boolean isShowTeamSelectionPanel() {
        return showTeamSelectionPanel;
    }

    public void setShowTeamSelectionPanel(boolean showTeamSelectionPanel) {
        this.showTeamSelectionPanel = showTeamSelectionPanel;
    }

    public boolean isShowLocationSelectionPanel() {
        return showLocationSelectionPanel;
    }

    public void setShowLocationSelectionPanel(boolean showLocationSelectionPanel) {
        this.showLocationSelectionPanel = showLocationSelectionPanel;
    }

    public Map<String, MobileGroup> getMobileGroupNameToMobileGroups() {
        return mobileGroupNameToMobileGroups;
    }

    public void setMobileGroupNameToMobileGroups(Map<String, MobileGroup> mobileGroupNameToMobileGroups) {
        this.mobileGroupNameToMobileGroups = mobileGroupNameToMobileGroups;
    }

    public Map<String, String> getMobileGroups() {
        return mobileGroups;
    }

    public void setMobileGroups(Map<String, String> mobileGroups) {
        this.mobileGroups = mobileGroups;
    }

    public String getSelectedGroupName() {
        return selectedGroupName;
    }

    public void setSelectedGroupName(String selectedGroupName) {
        this.selectedGroupName = selectedGroupName;
    }

    public Map<String, Team> getTeamNameToTeam() {
        return teamNameToTeam;
    }

    public void setTeamNameToTeam(Map<String, Team> teamNameToTeam) {
        this.teamNameToTeam = teamNameToTeam;
    }

    public Map<String, String> getTeams() {
        return teams;
    }

    public void setTeams(Map<String, String> teams) {
        this.teams = teams;
    }

    public String getSelectedTeamName() {
        return selectedTeamName;
    }

    public void setSelectedTeamName(String selectedTeamName) {
        this.selectedTeamName = selectedTeamName;
    }

    public Map<String, String> getSmsTemplates() {
        return smsTemplates;
    }

    public void setSmsTemplates(Map<String, String> smsTemplates) {
        this.smsTemplates = smsTemplates;
    }

    public String getSelectedSmsTemplate() {
        return selectedSmsTemplate;
    }

    public void setSelectedSmsTemplate(String selectedSmsTemplate) {
        this.selectedSmsTemplate = selectedSmsTemplate;
    }

    public boolean isEditSmsAllowed() {
        return editSmsAllowed;
    }

    public void setEditSmsAllowed(boolean editSmsAllowed) {
        this.editSmsAllowed = editSmsAllowed;
    }

}
