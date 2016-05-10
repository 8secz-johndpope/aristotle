package com.aristotle.admin.jsf.bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.aristotle.core.enums.AppPermission;
import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Team;
import com.aristotle.core.persistance.TeamMember;
import com.aristotle.core.service.TeamService;
import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLBeanName;
import com.ocpsoft.pretty.faces.annotation.URLMapping;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "view")
@URLMapping(id = "teamAdminBean", beanName = "teamAdminBean", pattern = "/admin/teams", viewId = "/admin/admin_teams.xhtml")
@URLBeanName("teamAdminBean")
public class TeamAdminBean extends BaseMultiPermissionAdminJsfBean {

	private static final long serialVersionUID = 1L;

    @Autowired
    private TeamService teamService;

    private Team selectedTeam;
	
	private boolean showList = true;

    private List<Team> teamList;

    private List<TeamMember> teamMembers;

    private String email;
    private String post;

	public TeamAdminBean(){
        super("/admin/teams", AppPermission.EDIT_TEAM);
        selectedTeam = new Team();
	}
	//@URLActions(actions = { @URLAction(mappingId = "userProfileBean") })
	@URLAction(onPostback=false)
	public void init() throws Exception {
        System.out.println("Checking USer Access");
		if(!checkUserAccess()){
			return;
		}
		refreshTeamList();
	}
	private void refreshTeamList(){
        try {
            if (menuBean.isGlobalSelected()) {
                teamList = teamService.getAllGlobalTeams();
            } else {
                Set<Long> locationIds = new HashSet<Long>();
                locationIds.add(menuBean.getSelectedLocation().getId());
                teamList = teamService.getLocationTeams(locationIds);
            }

        } catch (Exception ex) {
            teamList = new ArrayList<Team>();
            sendErrorMessageToJsfScreen(ex);
        }
	}

    public Team getSelectedTeam() {
        return selectedTeam;
	}

    public void setSelectedTeam(Team selectedTeam) {
        this.selectedTeam = selectedTeam;
		showList = false;
        refreshTeamMember();

    }

    private void refreshTeamMember() {
        try {
            teamMembers = teamService.getTeamMembersByTeamId(selectedTeam.getId());
        } catch (AppException e) {
            sendErrorMessageToJsfScreen(e);
        }
    }

    public void setDeleteTeamMember(TeamMember selectedTeamMember) {
        try {
            teamService.deleteTeamMember(selectedTeamMember.getId());
            refreshTeamMember();
        } catch (AppException e) {
            sendErrorMessageToJsfScreen(e);
        }
    }

    public void saveTeam() {
		try{
            if (StringUtils.isEmpty(selectedTeam.getName())) {
                sendErrorMessageToJsfScreen("Please enter Team name");
			}
            if (StringUtils.isEmpty(selectedTeam.getDescription())) {
                sendErrorMessageToJsfScreen("Please enter Team Description");
			}

			if(isValidInput()){
                selectedTeam.setGlobal(menuBean.isGlobalSelected());
                Long locationId = null;
                if (menuBean.getSelectedLocation() != null) {
                    locationId = menuBean.getSelectedLocation().getId();
                }
                selectedTeam = teamService.saveTeam(selectedTeam, locationId);
                sendInfoMessageToJsfScreen("Team saved succesfully");
				refreshTeamList();
				showList = true;
			}
				
		}catch(Exception ex){
			sendErrorMessageToJsfScreen("Unable to save Post",ex);
		}
		
	}

    public void saveTeamMember() {
        try {
            if (StringUtils.isEmpty(email)) {
                sendErrorMessageToJsfScreen("Please enter Registered User Email or Mobile Number(10 digits)");
            }
            if (StringUtils.isEmpty(post)) {
                sendErrorMessageToJsfScreen("Please Choose a Post");
            }

            if (isValidInput()) {
                teamService.saveTeamMember(email, post, selectedTeam.getId());
                sendInfoMessageToJsfScreen("Team Member saved succesfully");
                refreshTeamMember();
            }

        } catch (Exception ex) {
            sendErrorMessageToJsfScreen("Unable to save Post", ex);
        }

    }

    public void createTeam() {
        selectedTeam = new Team();
		showList = false;
        email = null;
        teamMembers = null;
	}

    public boolean isShowMemberPanel() {
        if (selectedTeam == null || selectedTeam.getId() == null || selectedTeam.getId() <= 0) {
            return false;
        }
        return true;
    }
	public void cancel(){
        selectedTeam = new Team();
        showList = true;
	}
	public boolean isShowList() {
		return showList;
	}
	public void setShowList(boolean showList) {
		this.showList = showList;
	}

    public List<Team> getTeamList() {
        return teamList;
    }

    public void setTeamList(List<Team> teamList) {
        this.teamList = teamList;
    }

    public List<TeamMember> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<TeamMember> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

}
