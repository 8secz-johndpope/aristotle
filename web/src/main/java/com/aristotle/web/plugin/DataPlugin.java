package com.aristotle.web.plugin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

public interface DataPlugin {

    void applyPlugin(HttpServletRequest httpServletRequest, ModelAndView mv);
}
