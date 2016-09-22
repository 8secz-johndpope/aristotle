package com.aristotle.web.plugin.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.aristotle.core.exception.AppException;
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
            logger.info("Getting Donations page number = {}, pageSize={} " ,pageNumber , pageSize);
            List<Donation> donationList = donationService.getDonations(pageNumber - 1, pageSize);
            JsonArray jsonArray = convertDonationList(donationList);

            JsonObject jsonObject = new JsonObject();
            jsonObject.add("list", jsonArray);
            addPagingInformation(jsonObject, pageNumber, pageSize);

            context.add(name, jsonObject);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void addPagingInformation(JsonObject jsonObject, int pageNumber, int pageSize) throws AppException {
        Long totalRecords = donationService.getTotalDonation();
        Long totalPages = totalRecords / pageSize;
        if (totalRecords % pageSize > 0) {
            totalPages++;
        }
        jsonObject.addProperty("totalPages", totalPages);
        if (pageNumber > 1) {
            jsonObject.addProperty("previousAvailable", true);
            jsonObject.addProperty("previousPage", (pageNumber - 1));
        } else {
            jsonObject.addProperty("previousAvailable", false);
        }
        if (pageNumber < totalPages) {
            jsonObject.addProperty("nextAvailable", true);
            jsonObject.addProperty("nextPage", (pageNumber + 1));
        } else {
            jsonObject.addProperty("nextAvailable", false);
        }
    }



}
