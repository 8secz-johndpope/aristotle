package com.aristotle.web.plugin.impl;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.aristotle.core.persistance.Team;
import com.aristotle.core.persistance.TeamMember;
import com.aristotle.core.service.TeamService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TeamPlugin extends AbstractDataPlugin {

    @Autowired
    private TeamService teamService;

    private static final String TEAM_URL_PARAMETER = "teamUrl";



    public TeamPlugin() {
    }

    public TeamPlugin(String pluginName) {
        super(pluginName);
    }

    @Override
    public void applyPlugin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv) {
        logger.info("Applying {} plugin", name);
        try {
            JsonObject context = (JsonObject) mv.getModel().get("context");
            String teamUrl = getStringParameterFromPathOrParams(httpServletRequest, TEAM_URL_PARAMETER);
            logger.info("teamUrl = " + teamUrl);
            if (teamUrl == null) {
                context.addProperty(name, "{\"error\":\"No TeamUrl Specified\"}");
                return;
            }
            JsonObject jsonObject = new JsonObject();
            Team team = teamService.getTeamByUrl(teamUrl);
            if (team == null) {
                System.out.println("Invalid Team" + teamUrl);
                jsonObject.addProperty("message", "Invalid Team");
            } else {
                List<TeamMember> teamMembers = teamService.getTeamMembersByTeamId(team.getId());
                jsonObject.add("team", convertTeam(team));
                jsonObject.add("members", convertTeamMembers(teamMembers));
            }
            context.add(name, jsonObject);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private JsonArray convertTeamMembers(Collection<TeamMember> teamMembers) {
        JsonArray teamMemberArray = new JsonArray();
        if (teamMembers == null) {
            return teamMemberArray;
        }
        for (TeamMember oneTeamMember : teamMembers) {
            JsonObject oneJsonObject = new JsonObject();
            oneJsonObject.addProperty("post", oneTeamMember.getPost());
            oneJsonObject.addProperty("responsbility", oneTeamMember.getResponsbility());
            oneJsonObject.add("user", convertUser(oneTeamMember.getUser()));
            teamMemberArray.add(oneJsonObject);
        }
        return teamMemberArray;

    }



}
