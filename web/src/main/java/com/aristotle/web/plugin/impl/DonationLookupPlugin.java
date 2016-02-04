package com.aristotle.web.plugin.impl;

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
import com.google.gson.JsonObject;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DonationLookupPlugin extends AbstractDataPlugin {

    @Autowired
    private DonationService donationService;



    public DonationLookupPlugin() {
    }

    public DonationLookupPlugin(String pluginName) {
        super(pluginName);
    }

    @Override
    public void applyPlugin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv) {
        try {
            JsonObject context = (JsonObject) mv.getModel().get("context");
            String paymentGatewayDonationId = getStringSettingPramater(HttpParameters.PAYMENT_GATEWAY_TRANSACTION_ID_PARAM, null);
            System.out.println("paymentGatewayDonationId = " + paymentGatewayDonationId);
            Donation donation = donationService.getDonationByPgTransactionId(paymentGatewayDonationId);
            JsonObject jsonObject = convertDonation(donation);
            context.add(name, jsonObject);
            // addPaginInformation(pageNumber, pageSize, totalNews, context);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }



}
