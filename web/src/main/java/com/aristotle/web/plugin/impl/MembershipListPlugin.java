package com.aristotle.web.plugin.impl;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Donation;
import com.aristotle.core.persistance.Membership;
import com.aristotle.core.persistance.MembershipTransaction;
import com.aristotle.core.persistance.User;
import com.aristotle.core.service.DonationService;
import com.aristotle.core.service.UserService;
import com.aristotle.core.service.aws.UserSearchService;
import com.aristotle.web.parameters.HttpParameters;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MembershipListPlugin extends AbstractDataPlugin {

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserSearchService userSearchService;



    public MembershipListPlugin() {
    }

    public MembershipListPlugin(String pluginName) {
        super(pluginName);
    }

    @Override
    public void applyPlugin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv) {
        try {
        	logger.info("applying Plugin : "+ name);
        	String query = getStringParameterFromPathOrParams(httpServletRequest, "query");
            logger.info("Query : "+query);
            userSearchService.searchMembers(query);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
