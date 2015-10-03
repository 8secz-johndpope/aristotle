package com.aristotle.web.plugin.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.aristotle.core.persistance.Team;
import com.aristotle.core.service.TeamService;
import com.google.gson.JsonObject;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TeamListPlugin extends AbstractDataPlugin {

    @Autowired
    private TeamService teamService;


    public TeamListPlugin() {
    }

    public TeamListPlugin(String pluginName) {
        super(pluginName);
    }

    @Override
    public void applyPlugin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv) {
        logger.info("Applying {} plugin", name);
        try {
            JsonObject context = (JsonObject) mv.getModel().get("context");
            JsonObject jsonObject = new JsonObject();
            List<Team> teams = teamService.getAllGlobalTeams();
            jsonObject.add("teams", convertTeams(teams));
            context.add(name, jsonObject);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



}
