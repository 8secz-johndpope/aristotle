package com.aristotle.web.plugin.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonObject;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FacebookCommentPlugin extends AbstractDataPlugin {

    public FacebookCommentPlugin(String pluginName) {
        super(pluginName);
    }

    public FacebookCommentPlugin() {
    }

    @Override
    public void applyPlugin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv) {
        String requestedUrl = httpServletRequest.getRequestURL().toString();
        String fbData = "<div id=\"fb-root\"></div><script>(function(d, s, id) { var js, fjs = d.getElementsByTagName(s)[0];"
                + " if (d.getElementById(id)) return;"
                + " js = d.createElement(s); js.id = id;"
                + " js.src = \"//connect.facebook.net/en_US/sdk.js#xfbml=1&version=v2.3&appId=119862768108344\";"
                + " fjs.parentNode.insertBefore(js, fjs);"
                + " }(document, 'script', 'facebook-jssdk'));</script>";
        String commentBox = " <div class=\"fb-comments\" data-href=\"" + requestedUrl + "\" data-numposts=\"5\" data-colorscheme=\"light\"></div>";
        JsonObject context = (JsonObject) mv.getModel().get("context");
        context.addProperty(name, fbData + commentBox);

    }

}
