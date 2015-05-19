package com.aristotle.web.plugin.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.aristotle.web.plugin.WebDataPlugin;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WebStaticDataPlugin implements WebDataPlugin {

    protected JsonObject settingJsonObject;
    protected final String jsonData;
    protected final String name;

    public WebStaticDataPlugin(String data, String name) {
        this.jsonData = data;
        this.name = name;
    }

    @Override
    public void setSettings(String settings) {
        JsonParser jsonParser = new JsonParser();
        settingJsonObject = (JsonObject) jsonParser.parse(settings);
    }

    @Override
    public void applyPlugin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv) {
        // TODO Auto-generated method stub

    }

}
