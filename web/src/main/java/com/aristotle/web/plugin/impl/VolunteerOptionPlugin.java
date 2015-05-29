package com.aristotle.web.plugin.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.aristotle.core.persistance.Interest;
import com.aristotle.core.persistance.InterestGroup;
import com.aristotle.core.service.AppService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VolunteerOptionPlugin extends AbstractDataPlugin {

    @Autowired
    private AppService appService;
    private JsonParser jsonParser = new JsonParser();

    public VolunteerOptionPlugin(String pluginName) {
        super(pluginName);
    }

    public VolunteerOptionPlugin() {
    }

    @Override
    public void applyPlugin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv) {

        logger.info("Applying {} plugin", name);
        try {
            JsonObject context = (JsonObject) mv.getModel().get("context");
            List<InterestGroup> interestGroups = appService.getAllInterests();

            JsonArray interestGroupJsonArray = new JsonArray();
            for (InterestGroup oneInterestGroup : interestGroups) {
                JsonObject oneInterestGroupJsonObject = new JsonObject();
                oneInterestGroupJsonObject.addProperty("description", oneInterestGroup.getDescription());
                JsonArray interestJsonArray = new JsonArray();
                for (Interest oneInterest : oneInterestGroup.getInterests()) {
                    JsonObject oneInterestJsonObject = new JsonObject();
                    oneInterestJsonObject.addProperty("id", oneInterest.getId());
                    oneInterestJsonObject.addProperty("description", oneInterest.getDescription());
                    interestJsonArray.add(oneInterestJsonObject);
                }
                interestGroupJsonArray.add(oneInterestGroupJsonObject);
            }
            context.add(name, interestGroupJsonArray);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
