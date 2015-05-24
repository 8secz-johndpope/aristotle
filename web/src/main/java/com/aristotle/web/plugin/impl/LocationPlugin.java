package com.aristotle.web.plugin.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Location;
import com.aristotle.core.persistance.LocationType;
import com.aristotle.core.service.LocationService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Urls this class can handle
 * /api/states
 * /api/districts/{stateId}
 * /api/acs/{districtId}
 * /api/pcs/{stateId}
 * /api/countries
 * /api/cr/{countryId}
 * /api/cra/{countryRegionId}
 * @author Ravi
 *
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LocationPlugin extends AbstractDataPlugin {

    @Autowired
    private LocationService locationService;

    int totalNews = 2;

    public LocationPlugin() {
    }

    public LocationPlugin(String pluginName) {
        super(pluginName);
    }
    
    @Override
    public void applyPlugin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv) {
        logger.info("Applying {} plugin", name);
        try {
            JsonObject context = (JsonObject) mv.getModel().get("context");
            String url = httpServletRequest.getRequestURI();

            LocationType locationType = getLocationTypeFromUrl(url);
            Long parentLocationId = getParentLocationId(httpServletRequest);
            addLocationsOfType(locationType, parentLocationId, context);
            // context.add(name, jsonArray);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private LocationType getLocationTypeFromUrl(String url) {
        String locationTypeString = null;
        if (url.contains("/states")) {
            locationTypeString = "State";
        }
        if (url.contains("/districts")) {
            locationTypeString = "District";
        }
        if (url.contains("/acs")) {
            locationTypeString = "AssemblyConstituency";
        }
        if (url.contains("/pcs")) {
            locationTypeString = "ParliamentConstituency";
        }
        if (url.contains("/countries")) {
            locationTypeString = "Country";
        }
        if (url.contains("/cr/")) {
            locationTypeString = "CountryRegion";
        }
        if (url.contains("/cra/")) {
            locationTypeString = "CountryRegionArea";
        }
        return getLocationtype(locationTypeString);
    }

    private LocationType getLocationtype(String locationTypeString) {
        if (StringUtils.isEmpty(locationTypeString)) {
            return null;
        }
        try {
            List<LocationType> allLocationTypes = locationService.getAllLocationTypes();
            for (LocationType oneLocationType : allLocationTypes) {
                if (oneLocationType.getName().equals(locationTypeString)) {
                    return oneLocationType;
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void addLocationsOfType(LocationType locationType, Long parentLocationId, JsonObject context) {
        if (locationType == null) {
            return;
        }
        try {
            List<Location> locations = locationService.getAllLocationsOfType(locationType, parentLocationId);
            JsonArray locationsJsonArray = convertLocationList(locations);
            context.add(name, locationsJsonArray);

        } catch (AppException e) {
            e.printStackTrace();
        }
    }

    private Long getParentLocationId(HttpServletRequest httpServletRequest) {
        Long parentLocationId = getLongParameterFromPathOrParams(httpServletRequest, "stateId");
        if (parentLocationId != null) {
            return parentLocationId;
        }
        parentLocationId = getLongParameterFromPathOrParams(httpServletRequest, "districtId");
        if (parentLocationId != null) {
            return parentLocationId;
        }
        parentLocationId = getLongParameterFromPathOrParams(httpServletRequest, "countryId");
        if (parentLocationId != null) {
            return parentLocationId;
        }
        parentLocationId = getLongParameterFromPathOrParams(httpServletRequest, "countryRegionId");
        return parentLocationId;
    }


}
