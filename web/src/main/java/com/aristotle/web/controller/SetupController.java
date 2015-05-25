package com.aristotle.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.service.temp.LocationUpgradeService;
import com.aristotle.web.plugin.PluginManager;
import com.aristotle.web.ui.template.UiTemplateManager;

@Controller
public class SetupController {

    @Autowired
    private PluginManager pluginManager;

    @Autowired
    private UiTemplateManager uiTemplateManager;

    @Autowired
    private LocationUpgradeService locationUpgradeService;

    @ExceptionHandler({ Exception.class })
    public String handleException(Exception ex) {
        ex.printStackTrace();
        return ex.getMessage();
    }

    @RequestMapping("/ui/refresh")
    @ResponseBody
    public String refreshTemplates(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) {
        uiTemplateManager.refresh();
        pluginManager.refresh();
        return "Success";
    }

    @RequestMapping("/plugin/update")
    @ResponseBody
    public String updatePlugins(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) throws AppException {
        pluginManager.updateDbWithAllPlugins();
        return "Success";
    }

    @RequestMapping("/permissions/update")
    @ResponseBody
    public String updatePermissions(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) throws Exception {
        locationUpgradeService.copyLocationTypeRoles();
        return "Success";
    }

    @RequestMapping("/userroles/update")
    @ResponseBody
    public String updateuserRoles(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) throws Exception {
        locationUpgradeService.copyUserRoles();
        return "Success";
    }

    @RequestMapping("/userlocations/update")
    @ResponseBody
    public String updateuserLocations(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) throws Exception {
        locationUpgradeService.copyUserLocations();
        return "Success";
    }

}