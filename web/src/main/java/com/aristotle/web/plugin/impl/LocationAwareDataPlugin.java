package com.aristotle.web.plugin.impl;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.aristotle.web.controller.HttpSessionUtil;
import com.aristotle.web.ui.template.UiTemplateManager;


@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public abstract class LocationAwareDataPlugin extends AbstractDataPlugin {

    @Autowired
    private HttpSessionUtil httpSessionUtil;
    @Autowired
    private UiTemplateManager uiTemplateManager;

    public LocationAwareDataPlugin() {
        super();
    }

    public LocationAwareDataPlugin(String pluginName) {
        super(pluginName);
    }

    @Override
    public final void applyPlugin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv) {
        logger.info("Applying {} plugin", name);
        // First get location from loggeed In User
        Set<Long> loggedInUserLocations = httpSessionUtil.getLoggedInUserLocations(httpServletRequest);
        // if user location not found then deriveit from domain
        if (loggedInUserLocations == null || loggedInUserLocations.isEmpty()) {
            Long domainLocation = uiTemplateManager.getDomainLocation(httpServletRequest);
            if(domainLocation != null){
                loggedInUserLocations = new HashSet<Long>(1, 1);
                loggedInUserLocations.add(domainLocation);
            }
        }
        applyPluginForLocation(httpServletRequest, httpServletResponse, mv, loggedInUserLocations);
    }

    public abstract void applyPluginForLocation(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv, Set<Long> locations);

}
