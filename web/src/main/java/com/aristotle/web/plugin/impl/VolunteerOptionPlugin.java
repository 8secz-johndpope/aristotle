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

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VolunteerOptionPlugin extends AbstractDataPlugin {

    @Autowired
    private AppService appService;


    public VolunteerOptionPlugin(String pluginName) {
        super(pluginName);
    }

    public VolunteerOptionPlugin() {
    }

    @Override
    public void applyPlugin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv) {

        logger.info("Applying {} plugin", name);
        int rowSize = getIntSettingPramater("interest.rowsize", 3);
        try {
            JsonObject context = (JsonObject) mv.getModel().get("context");
            logger.info("Finding All Interests");
            List<InterestGroup> interestGroups = appService.getAllInterests();
            logger.info("Intrests Loaded");
            JsonArray interestGroupJsonArray = new JsonArray();
            for (InterestGroup oneInterestGroup : interestGroups) {
                JsonObject oneInterestGroupJsonObject = new JsonObject();
                oneInterestGroupJsonObject.addProperty("description", oneInterestGroup.getDescription());
                JsonArray interestJsonArray = new JsonArray();
                JsonArray interestRowJsonArray = new JsonArray();
                int count = 0;
                for (Interest oneInterest : oneInterestGroup.getInterests()) {
                    JsonObject oneInterestJsonObject = new JsonObject();
                    oneInterestJsonObject.addProperty("id", oneInterest.getId());
                    oneInterestJsonObject.addProperty("description", oneInterest.getDescription());

                    interestRowJsonArray.add(oneInterestJsonObject);
                    count++;
                    if (count % rowSize == 0) {
                        interestJsonArray.add(interestRowJsonArray);
                        interestRowJsonArray = new JsonArray();
                    }
                }
                oneInterestGroupJsonObject.add("interests", interestJsonArray);
                interestGroupJsonArray.add(oneInterestGroupJsonObject);
            }
            context.add(name, interestGroupJsonArray);
            logger.info("Plugin {} applied", name);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
