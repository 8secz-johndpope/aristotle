package com.aristotle.web.plugin.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import com.aristotle.web.plugin.WebDataPlugin;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WebStaticDataPlugin implements WebDataPlugin {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    protected JsonObject settingJsonObject;
    protected final JsonObject jsonData;

        protected final String name;

    public WebStaticDataPlugin(JsonObject data, String name) {
        this.jsonData = data;
        this.name = name;
    }

    @Override
    public void setSettings(String settings) {
        JsonParser jsonParser = new JsonParser();
        try {
            settingJsonObject = (JsonObject) jsonParser.parse(settings);
        } catch (Exception ex) {
            logger.error("Invalid Setting for Plugin {}", name);
            settingJsonObject = new JsonObject();
        }

    }

    @Override
    public void applyPlugin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv) {
        JsonObject context = (JsonObject) mv.getModel().get("context");
        context.add(name, jsonData);
    }

    public String getName() {
        return name;
    }


}
