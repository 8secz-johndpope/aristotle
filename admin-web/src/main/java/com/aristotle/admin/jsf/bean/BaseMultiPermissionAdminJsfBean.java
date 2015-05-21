package com.aristotle.admin.jsf.bean;

import org.springframework.beans.factory.annotation.Autowired;

import com.aristotle.admin.service.AwsFileManager;
import com.aristotle.core.enums.AppPermission;
import com.aristotle.core.persistance.User;
import com.aristotle.core.service.DataPluginService;


public class BaseMultiPermissionAdminJsfBean extends BaseJsfBean{

	
    @Autowired
	protected MenuBean menuBean;
    
    @Autowired
    protected DataPluginService dataPluginService;

    @Autowired
    protected AwsFileManager awsFileManager;

	AppPermission[] appPermissions;
	String url;
	public BaseMultiPermissionAdminJsfBean(String url, AppPermission...appPermissions){
		this.appPermissions = appPermissions;
		this.url = url;
	}
	protected boolean checkUserAccess(){
        User loggedInUser = getLoggedInUser(true, buildLoginUrl(url));
        System.out.println("loggedInUser = " + loggedInUser);
		if(loggedInUser == null){
            System.out.println("No Logged in User Found");
			return false;
		}
		System.out.println("menuBean="+menuBean);
		if(isLocationNotSelected(menuBean)){
			
			buildAndRedirect("/admin/home");
			return false;
		}
		if(appPermissions == null || appPermissions.length == 0){
			return true;
		}
        if (menuBean.isAllowed(appPermissions)) {
            return true;
		}
		buildAndRedirect("/admin/notallowed");
		return false;
	}

	public MenuBean getMenuBean() {
		return menuBean;
	}
	public void setMenuBean(MenuBean menuBean) {
		this.menuBean = menuBean;
	}
}
