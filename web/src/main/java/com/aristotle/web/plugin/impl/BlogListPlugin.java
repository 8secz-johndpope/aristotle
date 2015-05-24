package com.aristotle.web.plugin.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

public class BlogListPlugin extends AbstractDataPlugin {

    public BlogListPlugin(String pluginName) {
        super(pluginName);
    }

    public BlogListPlugin() {
    }

    @Override
    public void applyPlugin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv) {


    }

}
