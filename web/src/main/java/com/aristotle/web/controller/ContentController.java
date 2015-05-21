package com.aristotle.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.aristotle.web.plugin.PluginManager;
import com.aristotle.web.ui.template.UiTemplateManager;
import com.google.gson.JsonObject;

@Controller
public class ContentController {

    @Autowired
    private PluginManager pluginManager;

    @Autowired
    private UiTemplateManager uiTemplateManager;

    @ExceptionHandler({ Exception.class })
    public String handleException(Exception ex) {
        ex.printStackTrace();
        return ex.getMessage();
    }

    @RequestMapping("/ui/refresh")
    @ResponseBody
    public String refreshTemplates(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) {
        uiTemplateManager.refresh();
        return "Success";
    }
    @RequestMapping("/content/**")
    public ModelAndView defaultMethod(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) {

        JsonObject context = new JsonObject();
        modelAndView.getModel().put("context", context);
        pluginManager.applyAllPluginsForUrl(httpServletRequest, httpServletResponse, modelAndView, false);

        String template = uiTemplateManager.getTemplate(httpServletRequest);
        modelAndView.getModel().put("template", template);

        modelAndView.setViewName("handlebar");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/api/content/**")
    public String defaultApiMethod(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) {
        System.out.println("defaultMethod called");
        JsonObject context = new JsonObject();
        modelAndView.getModel().put("context", context);
        pluginManager.applyAllPluginsForUrl(httpServletRequest, httpServletResponse, modelAndView, true);
        return context.toString();
    }
}
