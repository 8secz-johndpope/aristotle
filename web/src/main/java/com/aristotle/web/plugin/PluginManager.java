package com.aristotle.web.plugin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

public interface PluginManager {

    void applyAllPluginsForUrl(HttpServletRequest httpServletRequest, ModelAndView modelAndView);
}
