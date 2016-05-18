package com.aristotle.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.service.HttpUtil;
import com.aristotle.core.service.UserService;
import com.aristotle.core.service.VideoDownloader;
import com.aristotle.core.service.aws.UserSearchService;
import com.aristotle.core.service.temp.LocationUpgradeService;
import com.aristotle.web.plugin.PluginManager;
import com.aristotle.web.ui.template.UiTemplateManager;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;

import aws.services.cloudsearchv2.search.Hit;

@Controller
public class SetupController {

    @Autowired
    private PluginManager pluginManager;

    @Autowired
    private UiTemplateManager uiTemplateManager;

    @Autowired
    private LocationUpgradeService locationUpgradeService;

    @Autowired
    private VideoDownloader videoDownloader;

    @Autowired
    private UserService userService;

    @Autowired
    private UserSearchService userSearchService;
    
    @ExceptionHandler({ Exception.class })
    public String handleException(Exception ex) {
        ex.printStackTrace();
        return ex.getMessage();
    }

    @RequestMapping("/sc/ui/refresh")
    @ResponseBody
    public String refreshTemplates(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) {
        uiTemplateManager.refresh();
        pluginManager.refresh();
        return "Success";
    }

    @RequestMapping("/sc/plugin/update")
    @ResponseBody
    public String updatePlugins(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) throws AppException {
        pluginManager.updateDbWithAllPlugins();
        return "Success";
    }

    @RequestMapping("/sc/location/update")
    @ResponseBody
    public String updateLocations(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) throws AppException {
        locationUpgradeService.copyCountries();
        locationUpgradeService.copyCountryRegions();
        locationUpgradeService.copyCountryRegionAreas();
        locationUpgradeService.copyStates();
        locationUpgradeService.copyPcs();
        locationUpgradeService.copyDistricts();
        locationUpgradeService.copyAcs();
        return "Success";
    }

    @RequestMapping("/sc/permissions/update")
    @ResponseBody
    public String updatePermissions(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) throws Exception {
        locationUpgradeService.copyLocationTypeRoles();
        return "Success";
    }

    @RequestMapping("/sc/video/update")
    @ResponseBody
    public String downloadVideos(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) throws Exception {
        if(httpServletRequest.getParameter("updateall") == null){
            videoDownloader.refreshVideoList(false);
        }else{
            videoDownloader.refreshVideoList(true);
        }

        return "Success";
    }

    @RequestMapping("/sc/userroles/update")
    @ResponseBody
    public String updateuserRoles(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) throws Exception {
        locationUpgradeService.copyUserRoles();
        return "Success";
    }

    @RequestMapping("/sc/userlocations/update")
    @ResponseBody
    public String updateuserLocations(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) throws Exception {
        locationUpgradeService.copyUserLocations();
        return "Success";
    }

    @RequestMapping("/sc/handlebar/test")
    @ResponseBody
    public String upsddateuserLocations(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) throws Exception {
        Handlebars handlebars = new Handlebars();

        Template template = handlebars.compileInline("Hello {{this}}!");

        String result = template.apply("Handlebars.java");
        return result;

    }

    @RequestMapping("/sc/admin/sendemail")
    @ResponseBody
    public String sendUserAccountEmail(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) throws Exception {
        String email = httpServletRequest.getParameter("email");
        if (email == null) {
            return "Emai is required";
        }
        try {
            userService.generateUserLoginAccount(email);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }

        return "done";
    }

    @RequestMapping("/sc/admin/sendverificationemail")
    @ResponseBody
    public String sendUserVerificationEmail(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) throws Exception {
        String email = httpServletRequest.getParameter("email");
        if (email == null) {
            return "Emai is required";
        }
        try {
            userService.sendEmailConfirmtionEmail(email);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }

        return "done";
    }
    
    @RequestMapping("/sc/admin/sendmemberemail")
    @ResponseBody
    public String sendMembershipEmail(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) throws Exception {
        String email = httpServletRequest.getParameter("email");
        if (email == null) {
            return "Emai is required";
        }
        try {
            userService.sendMembershipConfirmtionEmail(email);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }

        return "done";
    }
    
    @RequestMapping("/sc/admin/members")
    @ResponseBody
    public String searchMembers(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) throws Exception {
        String query = httpServletRequest.getParameter("query");
        String result = userSearchService.searchMembers(query);
        return result;
    }
    @RequestMapping("/sc/admin/memberrefresh")
    @ResponseBody
    public String refreshMembers(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) throws Exception {
        String userId = httpServletRequest.getParameter("userId");
        userSearchService.sendUserForIndexing(userId);
        return "done";
    }
    

}
