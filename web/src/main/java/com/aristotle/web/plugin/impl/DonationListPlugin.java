package com.aristotle.web.plugin.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.aristotle.core.persistance.Donation;
import com.aristotle.core.service.DonationService;
import com.aristotle.web.parameters.HttpParameters;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DonationListPlugin extends AbstractDataPlugin {

    @Autowired
    private DonationService donationService;



    public DonationListPlugin() {
    }

    public DonationListPlugin(String pluginName) {
        super(pluginName);
    }

    @Override
    public void applyPlugin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv) {
        try {
            JsonObject context = (JsonObject) mv.getModel().get("context");
            int pageNumber = getIntPramater(httpServletRequest, HttpParameters.PAGE_NUMBER_PARAM, HttpParameters.PAGE_NUMBER_DEFAULT_VALUE);
            int pageSize = getIntSettingPramater("donation.size", 50);
            System.out.println("Getting Donations page number = " + pageNumber + ", pageSize=" + pageSize);
            List<Donation> donationList = donationService.getDonations(pageNumber - 1, pageSize);
            JsonArray jsonArray = convertDonationList(donationList);
            context.add(name, jsonArray);
            // addPaginInformation(pageNumber, pageSize, totalNews, context);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }



}
