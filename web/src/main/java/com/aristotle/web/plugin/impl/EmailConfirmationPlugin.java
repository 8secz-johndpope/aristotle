package com.aristotle.web.plugin.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.aristotle.core.service.UserService;
import com.google.gson.JsonObject;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EmailConfirmationPlugin extends AbstractDataPlugin {

    @Autowired
    private UserService userService;

    public EmailConfirmationPlugin(String pluginName) {
        super(pluginName);
    }

    public EmailConfirmationPlugin() {
    }

    @Override
    public void applyPlugin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv) {
        logger.info("Applying {} plugin", name);
        JsonObject emailConfirmationjsonObject = new JsonObject();
        JsonObject context = (JsonObject) mv.getModel().get("context");
        context.add(name, emailConfirmationjsonObject);
        try {

            String emailId = httpServletRequest.getParameter("email");
            String token = httpServletRequest.getParameter("token");
            logger.info("Confirm : {}, token: {}" , emailId ,  token);
            userService.confirmEmail(emailId, token);

            emailConfirmationjsonObject.addProperty("message", "Email confirmed succesfully");
        } catch (Exception ex) {
            emailConfirmationjsonObject.addProperty("message", ex.getMessage());
            emailConfirmationjsonObject.addProperty("error", true);
            ex.printStackTrace();
        }
    }

}
