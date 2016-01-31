package com.aristotle.donation.controller;

import java.io.IOException;
import java.util.Collections;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.omg.CORBA.portable.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.aristotle.core.persistance.PaymentGatewayDonation;
import com.aristotle.core.service.DonationService;
import com.aristotle.core.service.aws.QueueService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Controller
public class DonationController {

    @Autowired
    private DonationService donationService;

    @Value("${instamojoApiKey}")
    private String apiKey;
    @Value("${instamojoApiAuthToken}")
    private String apiAuthToken;

    @Autowired
    private QueueService queueService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    RestTemplate restTemplate;
    JsonParser jsonParser;

    @PostConstruct
    public void init() {
        restTemplate = new RestTemplate();
        jsonParser = new JsonParser();
        restTemplate.setInterceptors(Collections.singletonList(new XUserAgentInterceptor()));

    }

    @ExceptionHandler({ Exception.class })
    public String handleException(Exception ex) {
        ex.printStackTrace();
        return ex.getMessage();
    }

    @RequestMapping(value = { "/donationcomplete" })
    @ResponseBody
    public String serverSideHandler(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) throws IOException {
        // payment_id=MOJO6131000C45454677&status=success
        String paymentId = httpServletRequest.getParameter("payment_id");
        String status = httpServletRequest.getParameter("status");
        PaymentGatewayDonation paymentGatewayDonation = processDonation(paymentId);
        if (paymentGatewayDonation == null) {
            return "Unable to read donation status, we will check it again and update our records";
        } else {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("donationId", paymentGatewayDonation.getId());
            try {
                queueService.sendRefreshDonationMessage(jsonObject.toString());
            } catch (ApplicationException e) {
                logger.error("Unabel to send Donation Refresh Message", e);
            }
            return "Donation was Succesfull, Swaraj Abhiyan Donation id : " + paymentGatewayDonation.getId() + ", InstaMojo Donation id : " + paymentGatewayDonation.getMerchantReferenceNumber();
        }

    }


    private PaymentGatewayDonation processDonation(String donationId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-API-KEY", apiKey);
            headers.set("X-AUTH-TOKEN", apiAuthToken);
            HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

            String url = "https://www.instamojo.com/api/1.1/payments/" + donationId + "/";
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

            logger.info("responseEntity={}", responseEntity.getBody());
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                String responseBody = responseEntity.getBody();
                JsonObject jsonObject = (JsonObject) jsonParser.parse(responseBody);
                boolean success = jsonObject.get("success").getAsBoolean();
                JsonObject paymentJsonObject = jsonObject.get("payment").getAsJsonObject();

                String paymentId = paymentJsonObject.get("payment_id").getAsString();
                String status = paymentJsonObject.get("status").getAsString();
                String buyerName = paymentJsonObject.get("buyer_name").getAsString();
                String buyerPhone = paymentJsonObject.get("buyer_phone").getAsString();
                String buyerEmail = paymentJsonObject.get("buyer_email").getAsString();
                String amount = paymentJsonObject.get("amount").getAsString();
                String fees = paymentJsonObject.get("fees").getAsString();

                return donationService.saveOnlineDonationFromInstamojo(success, paymentId, status, buyerName, buyerPhone, buyerEmail, amount, fees);
            } else {
                logger.error("Unable to process Donation : {}", donationId);
            }
        } catch (Exception ex) {
            logger.error("Unabel to process donation : " + donationId, ex);
        }
        return null;
    }

    @RequestMapping(value = { "/private/complete" }, method = RequestMethod.POST, consumes = "application/*")
    @ResponseBody
    public String instaMojoPostUpdates(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView, @RequestBody String data) throws IOException {
        try {
            logger.info("Request Body : " + data);
            String buyerEmail = httpServletRequest.getParameter("buyer");
            String buyerName = httpServletRequest.getParameter("buyer_name");
            String customFields = httpServletRequest.getParameter("custom_fields");
            String unitPrice = httpServletRequest.getParameter("unit_price");
            String buyerPhone = httpServletRequest.getParameter("buyer_phone");
            String variants = httpServletRequest.getParameter("variants");
            String offerTitle = httpServletRequest.getParameter("offer_title");
            String currency = httpServletRequest.getParameter("currency");
            String offerSlug = httpServletRequest.getParameter("offer_slug");
            String paymentId = httpServletRequest.getParameter("payment_id");
            String mac = httpServletRequest.getParameter("mac");
            String amount = httpServletRequest.getParameter("amount");
            String fees = httpServletRequest.getParameter("fees");
            String status = httpServletRequest.getParameter("status");
            String quantity = httpServletRequest.getParameter("quantity");

            // donationService.saveOnlineDonationFromInstamojo("Credit".equalsIgnoreCase(status), paymentId, status, buyerName, buyerPhone, buyerEmail, amount, fees);

        } catch (Exception ex) {
            logger.error("Failed", ex);
        }
        return "Success";
    }

}
