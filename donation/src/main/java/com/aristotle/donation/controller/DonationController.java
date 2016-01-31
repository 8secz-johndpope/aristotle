package com.aristotle.donation.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.aristotle.core.service.DonationService;

@Controller
public class DonationController {

    @Autowired
    private DonationService donationService;

    @ExceptionHandler({ Exception.class })
    public String handleException(Exception ex) {
        ex.printStackTrace();
        return ex.getMessage();
    }

    @RequestMapping(value = { "/donationcomplete" })
    public ModelAndView serverSideHandler(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) throws IOException {
        //payment_id=MOJO6131000C45454677&status=success
        String paymentId = httpServletRequest.getParameter("payment_id");
        String status = httpServletRequest.getParameter("status");

        RedirectView rv = new RedirectView("https://www.instamojo.com/SwarajAbhiyan/donations-for-swaraj-abhiyan/");
        modelAndView.setView(rv);
        return modelAndView;
    }


    @RequestMapping(value = { "/private/complete" }, method = RequestMethod.POST, consumes = "application/*")
    @ResponseBody
    public String instaMojoPostUpdates(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView, @RequestBody String data) throws IOException {
        // payment_id=MOJO6131000C45454677&status=success
        System.out.println("Request Body : " + data);
        return "Success";
    }
    
}
