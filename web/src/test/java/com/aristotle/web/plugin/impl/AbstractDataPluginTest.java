package com.aristotle.web.plugin.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonObject;

public class AbstractDataPluginTest {

    private AbstractDataPlugin abstractDataPlugin;

    @Before
    public void init() {
        abstractDataPlugin = new AbstractDataPlugin() {

            @Override
            public void applyPlugin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv) {

            }
        };

    }

    @Test
    public void test01() {
        JsonObject jsonObject = new JsonObject();
        JsonObject newJsonObject = new JsonObject();
        jsonObject.add("news", newJsonObject);
        jsonObject.addProperty("count", 1);
        newJsonObject.addProperty("size", 10);

        abstractDataPlugin.setSettings(jsonObject.toString());
        Assert.assertEquals(10, abstractDataPlugin.getIntSettingPramater("news.size", 0));
        Assert.assertEquals(1, abstractDataPlugin.getIntSettingPramater("count", 0));
    }
}
