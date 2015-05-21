package com.aristotle.admin.jsf.bean;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.AccessDeniedException;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.aristotle.admin.util.UserSessionBean;
import com.aristotle.core.persistance.User;

public class BaseJsfBean extends BaseController implements Serializable {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public static HttpServletRequest getHttpServletRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }

    public void redirect(String url) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void sendErrorMessageToJsfScreen(String componentId, Exception e) {
        sendErrorMessageToJsfScreen(componentId, null, e);
    }

    protected void sendErrorMessageToJsfScreen(String componentId, String message) {
        sendErrorMessageToJsfScreen(componentId, message, null);
    }

    protected void sendErrorMessageToJsfScreen(Exception e) {
        sendErrorMessageToJsfScreen(null, null, e);
    }

    protected void sendErrorMessageToJsfScreen(String message) {
        sendErrorMessageToJsfScreen(null, message, null);
    }

    protected void sendErrorMessageToJsfScreen(String componentId, String message, Exception e) {
        logger.warn(message);
        if (e != null) {
            e.printStackTrace();
        }
        if (StringUtils.isEmpty(message)) {
            if (e instanceof AccessDeniedException) {
                FacesContext.getCurrentInstance().addMessage(componentId, new FacesMessage(FacesMessage.SEVERITY_ERROR, "You are not allowed for this operation", e.getMessage()));
            } else {
                FacesContext.getCurrentInstance().addMessage(componentId, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage()));
            }
        } else {
            if (e == null) {
                FacesContext.getCurrentInstance().addMessage(componentId, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));

            } else {
                FacesContext.getCurrentInstance().addMessage(componentId, new FacesMessage(FacesMessage.SEVERITY_ERROR, message + " : " + e.getMessage(), message + " : " + e.getMessage()));

            }

        }
    }

    protected void sendInfoMessageToJsfScreen(String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message, message));

    }

    public void refreshPage() {
        try {
            HttpServletRequest httpServletRequest = getHttpServletRequest();
            String url = httpServletRequest.getRequestURI();
            FacesContext.getCurrentInstance().getExternalContext().redirect(buildUrl(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buildAndRedirect(String url) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(buildUrl(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String buildUrl(String url) {
        HttpServletRequest httpServletRequest = getHttpServletRequest();
        return httpServletRequest.getContextPath() + url;
    }

    public UserSessionBean getUserRolePermissionInSesion() {
        HttpServletRequest httpServletRequest = getHttpServletRequest();
        return (UserSessionBean) httpServletRequest.getSession(true).getAttribute(SESSION_USER_PERMISSIONS_PARAM);
    }

    public static String buildLoginUrl(String redirectUrl) {
        HttpServletRequest httpServletRequest = getHttpServletRequest();
        return buildLoginUrl(httpServletRequest, redirectUrl);
    }

    public static String buildLoginUrl(HttpServletRequest httpServletRequest, String redirectUrl) {
        return httpServletRequest.getContextPath() + "/login?" + REDIRECT_URL_PARAM_ID + "=" + httpServletRequest.getContextPath() + redirectUrl;
    }

    protected boolean isLocationNotSelected(MenuBean menuBean) {
        System.out.println("menuBean.getSelectedLocation()= " + menuBean.getSelectedLocation());
        System.out.println("menuBean.isGlobalSelected()= " + menuBean.isGlobalSelected());
        return (menuBean.getSelectedLocation() == null && !menuBean.isGlobalSelected());
    }

    protected User getLoggedInUser() {
        return getLoggedInUser(false, "");
    }

    protected User getLoggedInUser(boolean redirect, String url) {
        HttpServletRequest httpServletRequest = getHttpServletRequest();
        User user = getLoggedInUserFromSesion(httpServletRequest);
        if (user == null) {
            if (redirect) {
                redirect(url);
            }
        }
        return user;
    }

    protected boolean isValidInput() {
        if (FacesContext.getCurrentInstance().getMessageList().size() > 0) {
            return false;
        }
        return true;
    }
    /*
     * 
     * 
     * 
     * 
     * 
     * protected void setFinalRedirectUrlInSesion(String url){ HttpServletRequest httpServletRequest = getHttpServletRequest(); setFinalRedirectUrlInSesion(httpServletRequest, url); }
     * 
     * protected void ssaveLoggedInUserInSession(UserDto user) { HttpServletRequest httpServletRequest = getHttpServletRequest(); super.setLoggedInUserInSesion(httpServletRequest, user); }
     * 
     * public LoginAccountDto getLoggedInAccountsFromSesion(){ HttpServletRequest httpServletRequest = getHttpServletRequest(); return getLoggedInAccountsFromSesion(httpServletRequest); } protected
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     */
}
