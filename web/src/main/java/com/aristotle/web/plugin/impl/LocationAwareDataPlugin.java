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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


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
        logger.debug("Applying {} plugin", name);
        // First get location from Domain
        Set<Long> loggedInUserLocations = new HashSet<Long>();
        Long domainLocation = uiTemplateManager.getDomainLocation(httpServletRequest);
        if (domainLocation != null) {
            loggedInUserLocations.add(domainLocation);
        }
        // if user location not found then deriveit from user session/cookies
        if (loggedInUserLocations == null || loggedInUserLocations.isEmpty()) {
            loggedInUserLocations = httpSessionUtil.getLoggedInUserLocations(httpServletRequest);
        }
        applyPluginForLocation(httpServletRequest, httpServletResponse, mv, loggedInUserLocations);
    }

    protected void addPaginInformation(int currentPage, int pageSize, long totalRecords, JsonObject context) {
        JsonObject pageJsonObject = new JsonObject();
        int totalPages = (int) totalRecords / pageSize;
        if (totalRecords % pageSize > 0) {
            totalPages++;
        }
        if (currentPage > 1) {
            pageJsonObject.addProperty("previous", true);
            pageJsonObject.addProperty("previousPage", (currentPage - 1));
        }
        if (currentPage < totalPages) {
            pageJsonObject.addProperty("next", true);
            pageJsonObject.addProperty("nextPage", (currentPage + 1));
        }
        pageJsonObject.addProperty("lastPage", totalPages);
        // 1 2 3 4
        int pageListStart = 1;
        int pageListEnd = totalPages;
        if (currentPage + 2 <= totalPages) {
            if (currentPage > 2) {
                pageListStart = currentPage - 2;
            }
            pageListEnd = pageListStart + 4;
            if (pageListEnd > totalPages) {
                pageListEnd = totalPages;
            }
        } else {
            pageListEnd = totalPages;
            pageListStart = pageListEnd - 4;
            if (pageListStart <= 0) {
                pageListStart = 1;
            }
        }
        JsonArray pageListJsonArray = new JsonArray();
        for (int count = pageListStart; count <= pageListEnd; count++) {
            JsonObject onePage = new JsonObject();
            onePage.addProperty("pageNumber", count);
            if (count == currentPage) {
                onePage.addProperty("selected", true);
            }
            pageListJsonArray.add(onePage);
        }
        pageJsonObject.add("pages", pageListJsonArray);
        context.add("pageInfo", pageJsonObject);
    }

    public abstract void applyPluginForLocation(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv, Set<Long> locations);

}
