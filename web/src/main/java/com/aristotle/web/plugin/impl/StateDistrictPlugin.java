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

 * @author Ravi
 *
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class StateDistrictPlugin extends AbstractDataPlugin {

    @Autowired
    private LocationService locationService;

    public StateDistrictPlugin() {
    }

    public StateDistrictPlugin(String pluginName) {
        super(pluginName);
    }
    
    @Override
    public void applyPlugin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv) {
        logger.info("Applying {} plugin", name);
        try {
            JsonObject context = (JsonObject) mv.getModel().get("context");
            List<Location> allStates = locationService.getAllStates();
            List<Location> districts;
            JsonArray stateJasonArray = new JsonArray();
            for(Location oneLocation : allStates){
            	JsonObject oneStateJson = convertLocation(oneLocation);
            	districts = locationService.getAllDistrictOfState(oneLocation.getId());
            	JsonArray districtList = convertLocationList(districts);
            	oneStateJson.add("districts", districtList);
            	stateJasonArray.add(oneStateJson);
            }
            context.add(name, stateJasonArray);
            // context.add(name, jsonArray);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
