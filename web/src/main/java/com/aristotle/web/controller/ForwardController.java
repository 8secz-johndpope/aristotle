package com.aristotle.web.controller;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.service.UserService;
import com.aristotle.core.service.VideoDownloader;
import com.aristotle.core.service.temp.LocationUpgradeService;
import com.aristotle.web.plugin.PluginManager;
import com.aristotle.web.ui.template.UiTemplateManager;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ForwardController {

    @ExceptionHandler({ Exception.class })
    public String handleException(Exception ex) {
        ex.printStackTrace();
        return ex.getMessage();
    }

    @RequestMapping(value = {"/register"})
    public ModelAndView refreshTemplates(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) {
        RedirectView rv = new RedirectView("/index.html?register=1");
        rv.setExposeModelAttributes(false);
        modelAndView.setView(rv);
        return modelAndView;
    }

}
