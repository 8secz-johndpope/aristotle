package com.aristotle.donation.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class HomeController {

    @ExceptionHandler({ Exception.class })
    public String handleException(Exception ex) {
        ex.printStackTrace();
        return ex.getMessage();
    }

    @RequestMapping(value = { "/", "/index.html", "/**" })
    public ModelAndView serverSideHandler(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) throws IOException {
        RedirectView rv = new RedirectView("https://www.instamojo.com/SwarajAbhiyan/donations-for-swaraj-abhiyan/");
        modelAndView.setView(rv);
        return modelAndView;
    }

    @RequestMapping(value = { "/lb" })
    @ResponseBody
    public String forLoadbalancer(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) throws IOException {
        return "All Good";
    }

    
}
