package com.aristotle.admin.jsf.bean;

import java.util.Enumeration;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.aristotle.admin.service.AdminService;
import com.aristotle.admin.util.SessionUtil;
import com.aristotle.core.persistance.User;
import com.aristotle.core.service.temp.LocationUpgradeService;
import com.ocpsoft.pretty.faces.annotation.URLBeanName;
import com.ocpsoft.pretty.faces.annotation.URLMapping;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
@URLMapping(id = "adminLoginBean", beanName = "adminLoginBean", pattern = "/admin/login", viewId = "/admin/login.xhtml")
@URLBeanName("adminLoginBean")
public class LoginBean extends BaseJsfBean {

    private static final long serialVersionUID = 1L;

    @Autowired
    private AdminService adminService;

    @Autowired
    private LocationUpgradeService locationUpgradeService;

    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private MenuBean menuBean;


    private String userName;
    private String password;

    @Autowired
    private ApplicationContext applicationCtx;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void init() {
    }

    public void login() {

        System.out.println("Login with " + userName);
        logger.info("Login with " + userName);
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            User user = adminService.login(userName, password);
            if (user != null) {
                sessionUtil.setLoggedInUserinSession(httpServletRequest, user);
                menuBean.setUser(user);
            }
            menuBean.refreshLoginRoles();
            // Move to Redirect page
            String redirectUrl = httpServletRequest.getParameter("redirect_url");
            if (StringUtils.isEmpty(redirectUrl)) {
                redirectUrl = "/admin/home";
            }
            redirect(redirectUrl);
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorMessageToJsfScreen("Error", e.getMessage());
        }

    }

    public void logout() {
        HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        httpServletRequest.getSession().invalidate();
        String redirectUrl = "/admin/login.xhtml";
        redirect(redirectUrl);
    }

    public void onSelectDepartment() {
        HttpServletRequest request = getHttpServletRequest();
        Enumeration<String> paramNames = request.getSession().getAttributeNames();
        while (paramNames.hasMoreElements()) {
            logger.info("param : " + paramNames.nextElement());
        }
        request.getSession().removeAttribute("scopedTarget.complaintsBean");
        refreshPage();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

