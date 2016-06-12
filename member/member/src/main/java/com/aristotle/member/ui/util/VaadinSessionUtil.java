package com.aristotle.member.ui.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.aristotle.core.persistance.User;
import com.vaadin.server.VaadinService;
import com.vaadin.server.WrappedHttpSession;

@Component
public class VaadinSessionUtil {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    protected final String LOGGED_IN_USER_SESSION_PARAM = "LIUSP";
    
	public void setLoggedInUserinSession(User user) {
        logger.info("Setting User in Session : " + user);
        WrappedHttpSession wrappedSession = (WrappedHttpSession)VaadinService.getCurrentRequest().getWrappedSession(true);
        wrappedSession.setAttribute(LOGGED_IN_USER_SESSION_PARAM, user);
    }
    
    public User getLoggedInUserFromSession() {
        logger.info("Getting User from Session");
        WrappedHttpSession wrappedSession = (WrappedHttpSession)VaadinService.getCurrentRequest().getWrappedSession(true);
        return (User) wrappedSession.getAttribute(LOGGED_IN_USER_SESSION_PARAM);
    }
}
