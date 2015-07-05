package com.aristotle.web.plugin.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.aristotle.core.persistance.User;
import com.aristotle.core.service.NewsService;
import com.google.gson.JsonObject;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LoggedInUserPlugin extends AbstractDataPlugin {

    @Autowired
    private NewsService newsService;



    public LoggedInUserPlugin() {
    }

    public LoggedInUserPlugin(String pluginName) {
        super(pluginName);
    }

    @Override
    public void applyPlugin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv) {
        logger.info("Applying {} plugin", name);
        try {
            JsonObject context = (JsonObject) mv.getModel().get("context");
            User user = (User) httpServletRequest.getSession().getAttribute("loggedInUser");
            JsonObject userJsonObject;
            if (user == null) {
                userJsonObject = new JsonObject();
                userJsonObject.addProperty("loggedIn", false);
            } else {
                userJsonObject = convertUser(user);
                userJsonObject.addProperty("loggedIn", true);
            }
            context.add(name, userJsonObject);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
