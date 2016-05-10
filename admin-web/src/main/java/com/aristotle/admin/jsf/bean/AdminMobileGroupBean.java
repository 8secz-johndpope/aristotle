package com.aristotle.admin.jsf.bean;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.aristotle.core.enums.AppPermission;
import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.MobileGroup;
import com.aristotle.core.persistance.MobileGroupMobile;
import com.aristotle.core.service.SmsService;
import com.google.gdata.util.common.base.StringUtil;
import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLBeanName;
import com.ocpsoft.pretty.faces.annotation.URLMapping;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "view")
@URLMapping(id = "adminMobileGroupBean", beanName = "adminMobileGroupBean", pattern = "/admin/mobilegroup", viewId = "/admin/admin_mobile_group.xhtml")
@URLBeanName("adminMobileGroupBean")
public class AdminMobileGroupBean extends BaseMultiPermissionAdminJsfBean {

	private static final long serialVersionUID = 1L;

    private MobileGroup selectedMobileGroup;

	private boolean showList = true;

    private List<MobileGroup> mobileGroups;
    private String emailMobile;
    private List<MobileGroupMobile> mobileGroupMembers;

    @Autowired
    private SmsService smsService;

	public AdminMobileGroupBean() {
        super("/admin/mobilegroup", AppPermission.ADMIN_SMS);
	}

	// @URLActions(actions = { @URLAction(mappingId = "userProfileBean") })
	@URLAction(onPostback = false)
	public void init() throws Exception {
		if (!checkUserAccess()) {
			return;
		}
        refreshTeamList();
	}
    private void refreshTeamList() {
        // plannedSmss = smsService.getPlannedSmssForLocation(menuBean.getLocationType(), menuBean.getAdminSelectedLocationId(), pageNumber, pageSize);
        try {
            mobileGroups = smsService.getAllMobileGroups();
        } catch (AppException e) {
            sendErrorMessageToJsfScreen(e);
        }
    }

    public void saveMobileGroup() {
		try {
            if (StringUtil.isEmpty(selectedMobileGroup.getName())) {
                sendErrorMessageToJsfScreen("Please enter a Group name");
			}
            if (isValidInput()) {
                selectedMobileGroup = smsService.saveMobileGroup(selectedMobileGroup);
                sendInfoMessageToJsfScreen("Mobile Group [" + selectedMobileGroup.getName() + "] saved succesfully");
            }

		} catch (Exception ex) {
            sendErrorMessageToJsfScreen("Unable to save SMS", ex);
		}

	}

    public void saveMobileGroupMember() {
        try {
            if (StringUtil.isEmpty(emailMobile)) {
                sendErrorMessageToJsfScreen("Please enter a Valid email or 10 digit mobile number");
            }
            if (isValidInput()) {
                smsService.addMemberToMobileGroup(selectedMobileGroup, emailMobile);
                sendInfoMessageToJsfScreen("Member added to Mobile Group [" + selectedMobileGroup.getName() + "] succesfully");
            }

        } catch (Exception ex) {
            sendErrorMessageToJsfScreen("Unable to Add Member", ex);
        }
    }



    public void createMobileGroup() {
        selectedMobileGroup = new MobileGroup();
		showList = false;
	}

	public void clear() {
        createMobileGroup();
	}

	public void cancel() {
        createMobileGroup();
		showList = true;
	}

    public boolean isShowMemberPanel() {
        if (selectedMobileGroup == null || selectedMobileGroup.getId() == null || selectedMobileGroup.getId() <= 0) {
            return false;
        }
        return true;
    }
	public boolean isShowList() {
		return showList;
	}

	public void setShowList(boolean showList) {
		this.showList = showList;
	}

    public MobileGroup getSelectedMobileGroup() {
        return selectedMobileGroup;
    }

    public void setSelectedMobileGroup(MobileGroup selectedMobileGroup) {
        this.selectedMobileGroup = selectedMobileGroup;
        showList = false;
        refreshMobileGroupMembers();
    }

    private void refreshMobileGroupMembers() {
        try {
            mobileGroupMembers = smsService.getMembersOfMobileGroup(selectedMobileGroup.getId());
        } catch (Exception e) {
            sendErrorMessageToJsfScreen(e);
        }
    }

    public void setDeleteMobileGroupMember(MobileGroupMobile mobileGroupMobile) {
        try {
            smsService.removeMemberFromMobileGroup(mobileGroupMobile);
        } catch (Exception e) {
            sendErrorMessageToJsfScreen(e);
        }
    }

    public List<MobileGroup> getMobileGroups() {
        return mobileGroups;
    }

    public void setMobileGroups(List<MobileGroup> mobileGroups) {
        this.mobileGroups = mobileGroups;
    }

    public String getEmailMobile() {
        return emailMobile;
    }

    public void setEmailMobile(String emailMobile) {
        this.emailMobile = emailMobile;
    }

    public List<MobileGroupMobile> getMobileGroupMembers() {
        return mobileGroupMembers;
    }

    public void setMobileGroupMembers(List<MobileGroupMobile> mobileGroupMembers) {
        this.mobileGroupMembers = mobileGroupMembers;
    }

}
