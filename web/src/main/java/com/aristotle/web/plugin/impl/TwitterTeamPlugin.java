package com.aristotle.web.plugin.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.aristotle.core.persistance.TwitterTeam;
import com.aristotle.core.persistance.User;
import com.aristotle.core.service.TwitterService;
import com.aristotle.web.parameters.HttpParameters;
import com.google.gson.JsonObject;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TwitterTeamPlugin extends AbstractDataPlugin {

    @Autowired
    private TwitterService twitterService;

    private static final String TEAM_URL_PARAMETER = "teamUrl";



    public TwitterTeamPlugin() {
    }

    public TwitterTeamPlugin(String pluginName) {
        super(pluginName);
    }

    @Override
    public void applyPlugin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv) {
        logger.info("Applying {} plugin", name);
        try {
            JsonObject context = (JsonObject) mv.getModel().get("context");
            User user = (User) httpServletRequest.getSession().getAttribute("loggedInUser");
            System.out.println("user = " + user);
            String teamUrl = getTwitterTeamUrl(httpServletRequest);
            System.out.println("teamUrl = " + teamUrl);
            if (teamUrl == null) {
                context.addProperty(name, "{\"error\":\"No TeamUrl Specified\"}");
                return;
            }
            JsonObject jsonObject = new JsonObject();
            TwitterTeam twitterTeam = twitterService.getTwitterTeamByUrl(teamUrl);
            if (twitterTeam == null) {
                System.out.println("Invalid Team" + teamUrl);
                jsonObject.addProperty("message", "Invalid Team");
            } else if (user == null) {
                jsonObject.addProperty("message", "Not logged In");
            } else {
                System.out.println("Checking if user already part of the team" + user.getId() + " , " + twitterTeam.getId());
                boolean userPartOfTheTeam = twitterService.isUserPartOfTwitterTeam(user.getId(), twitterTeam.getId());
                jsonObject.addProperty("userPartOfTheTeam", userPartOfTheTeam);
                jsonObject.addProperty("teamLoginUrl", "/twitter/team/" + teamUrl);
                jsonObject.addProperty("teamUrl", teamUrl);
            }

            context.add(name, jsonObject);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String getTwitterTeamUrl(HttpServletRequest httpServletRequest) {
        Map<String, String> pathParams = (Map<String, String>) httpServletRequest.getAttribute(HttpParameters.PATH_PARAMETER_PARAM);
        String teamUrl = pathParams.get(TEAM_URL_PARAMETER);
        if (teamUrl == null) {
            teamUrl = httpServletRequest.getParameter(TEAM_URL_PARAMETER);
        }
        return teamUrl;
    }


}
