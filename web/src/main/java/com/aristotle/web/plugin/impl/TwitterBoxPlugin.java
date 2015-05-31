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
public class TwitterBoxPlugin extends AbstractDataPlugin {

    public TwitterBoxPlugin(String pluginName) {
        super(pluginName);
    }

    public TwitterBoxPlugin() {
    }

    @Override
    public void applyPlugin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv) {
        String twitterData = "<a class=\"twitter-timeline\" href=\"https://twitter.com/swaraj_abhiyan\" data-widget-id=\"591375821736120321\">Tweets by @swaraj_abhiyan</a>" + "<script>"
                + "!function(d, s, id) {" + "var js, fjs = d.getElementsByTagName(s)[0], p = /^http:/.test(d.location) ? 'http': 'https';" + "if (!d.getElementById(id)) {"
                + "js = d.createElement(s);" + "js.id = id;" + "js.src = p" + "+ \"://platform.twitter.com/widgets.js\";" + "fjs.parentNode.insertBefore(js, fjs);" + "}"
                + "}(document, \"script\", \"twitter-wjs\");" + "</script>";
        JsonObject context = (JsonObject) mv.getModel().get("context");
        context.addProperty(name, twitterData);

    }

}
