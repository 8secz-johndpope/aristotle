package com.aristotle.web.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.omg.CORBA.portable.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.aristotle.core.persistance.PaymentGatewayDonation;
import com.aristotle.core.persistance.User;
import com.aristotle.core.persistance.repo.CustomerRepository;
import com.aristotle.core.service.UserService;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Controller
public class MembershipController {

	@Autowired
	private UserService userService;
	@Value("${instamojoApiKey}")
    private String apiKey;
    @Value("${instamojoApiAuthToken}")
    private String apiAuthToken;
    
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    RestTemplate restTemplate;
    JsonParser jsonParser;

    private DateFormat dateFormat = new ISO8601DateFormat();

    @PostConstruct
    public void init() {
        restTemplate = new RestTemplate();
        jsonParser = new JsonParser();
        restTemplate.setInterceptors(Collections.singletonList(new XUserAgentInterceptor()));

    }
    
    @RequestMapping(value = { "/user/memberpaymentsuccess" })
    public ModelAndView serverSideHandler(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) throws IOException {
        // payment_id=MOJO6131000C45454677&status=success
        String paymentId = httpServletRequest.getParameter("payment_id");
        String status = httpServletRequest.getParameter("status");

        PaymentGatewayDonation paymentGatewayDonation = processPayment(httpServletRequest, paymentId);
        if (paymentGatewayDonation == null) {
            modelAndView.getModel().put("success", false);
        } else {
            modelAndView.getModel().put("success", true);
            modelAndView.getModel().put("paymentGatewayDonation", paymentGatewayDonation);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("donationId", paymentGatewayDonation.getId());
            /*
            try {
                queueService.sendRefreshDonationMessage(jsonObject.toString());
            } catch (ApplicationException e) {
                logger.error("Unabel to send Donation Refresh Message", e);
            }
            */
        }
        modelAndView.setView(new RedirectView("//www.swarajabhiyan.org/user/editprofile"));
        return modelAndView;
    }


    private PaymentGatewayDonation processPayment(HttpServletRequest httpServletRequest, String donationId) {
      String paymentId = null;
      try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-API-KEY", apiKey);
            headers.set("X-AUTH-TOKEN", apiAuthToken);
            HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

            String url = "https://www.instamojo.com/api/1.1/payments/" + donationId + "/";
            System.out.println("Hitting Url : "+ url);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

            logger.info("responseEntity={}", responseEntity.getBody());
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                String responseBody = responseEntity.getBody();
                JsonObject jsonObject = (JsonObject) jsonParser.parse(responseBody);
                boolean success = jsonObject.get("success").getAsBoolean();
                JsonObject paymentJsonObject = jsonObject.get("payment").getAsJsonObject();

                paymentId = paymentJsonObject.get("payment_id").getAsString();
                String status = paymentJsonObject.get("status").getAsString();
                String buyerName = paymentJsonObject.get("buyer_name").getAsString();
                String buyerPhone = paymentJsonObject.get("buyer_phone").getAsString();
                String buyerEmail = paymentJsonObject.get("buyer_email").getAsString();
                String amount = paymentJsonObject.get("amount").getAsString();
                String fees = paymentJsonObject.get("fees").getAsString();
                // created_at": "2016-01-31T00:13:41.211Z"
                Date donationTime = dateFormat.parse(paymentJsonObject.get("created_at").getAsString());
                
                if(success){
                	User user = (User) httpServletRequest.getSession().getAttribute("loggedInUser");
                    Long loggedInUserId = null;
                    if(user != null){
                    	loggedInUserId = user.getId();
                    }
                    
                    userService.registerOnlineMember(loggedInUserId, buyerPhone, buyerName, amount, "Online",paymentId, fees);
                }
                

            } else {
                logger.error("Unable to process Membership Record for PaymentId : {}, donationId : {}", paymentId, donationId);
            }
        } catch (Exception ex) {
            logger.error("Unable to process Membership Record for PaymentId : "+paymentId+", donationId : " + donationId, ex);
        }
        return null;
    }

}