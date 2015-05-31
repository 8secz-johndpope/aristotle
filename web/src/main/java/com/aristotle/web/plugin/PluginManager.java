package com.aristotle.web.plugin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.aristotle.core.exception.AppException;

public interface PluginManager {

    void applyAllPluginsForUrl(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView, boolean addData);

    void refresh();

    void updateDbWithAllPlugins() throws AppException;
}
