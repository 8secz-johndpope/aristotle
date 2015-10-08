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
import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.GroupPlannedSms;
import com.aristotle.core.persistance.LocationPlannedSms;
import com.aristotle.core.persistance.MobileGroup;
import com.aristotle.core.persistance.PlannedSms;
import com.aristotle.core.persistance.TeamPlannedSms;
import com.aristotle.core.service.SmsService;
import com.google.gdata.util.common.base.StringUtil;
import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLBeanName;
import com.ocpsoft.pretty.faces.annotation.URLMapping;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "view")
@URLMapping(id = "adminSmsBean", beanName = "adminSmsBean", pattern = "/admin/sms", viewId = "/WEB-INF/jsf/admin_sms.xhtml")
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
    private String selectedTargetType;
    private String selectedGroupName;

    private boolean showGroupSelectionPanel;
    private boolean showTeamSelectionPanel;
    private boolean showLocationSelectionPanel;

    @Autowired
    private SmsService smsService;

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
	}
	private void refreshSmsList(){
        // plannedSmss = smsService.getPlannedSmssForLocation(menuBean.getLocationType(), menuBean.getAdminSelectedLocationId(), pageNumber, pageSize);
        try {
            plannedSmss = smsService.getAllPlannedSms();
        } catch (AppException e) {
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

    public void saveSms() {
		try {
            // selectedPlannedSms.setLocationType(menuBean.getSelectedLocation().getLocationType());
            if (menuBean.getSelectedLocation() != null) {
                selectedPlannedSms.setLocationId(menuBean.getSelectedLocation().getId());
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

            switch (selectedTargetType) {
            case "TeamSms":
                sendErrorMessageToJsfScreen("Not Implemented yet");
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
        MobileGroup selectedMobileGroup = mobileGroupNameToMobileGroups.get(selectedGroupName);
        GroupPlannedSms groupPlannedSms = (GroupPlannedSms) plannedSmss;
        groupPlannedSms.setMobileGroup(selectedMobileGroup);

        if (isValidInput()) {
            selectedPlannedSms = smsService.saveGroupPlannedSms(groupPlannedSms);
            sendInfoMessageToJsfScreen("Sms saved succesfully");
            refreshSmsList();
            showList = true;
        }
    }

	public void newPost() {
        selectedPlannedSms = new PlannedSms();
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

}
