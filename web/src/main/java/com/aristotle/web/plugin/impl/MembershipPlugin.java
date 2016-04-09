package com.aristotle.web.plugin.impl;

import java.util.Calendar;
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
import com.aristotle.core.persistance.Membership;
import com.aristotle.core.persistance.MembershipTransaction;
import com.aristotle.core.persistance.User;
import com.aristotle.core.service.DonationService;
import com.aristotle.core.service.UserService;
import com.aristotle.web.parameters.HttpParameters;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MembershipPlugin extends AbstractDataPlugin {

    @Autowired
    private UserService userService;



    public MembershipPlugin() {
    }

    public MembershipPlugin(String pluginName) {
        super(pluginName);
    }

    @Override
    public void applyPlugin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv) {
        try {
            JsonObject context = (JsonObject) mv.getModel().get("context");
            User user = (User) httpServletRequest.getSession().getAttribute("loggedInUser");
            JsonObject userJsonObject = new JsonObject();
            if (user != null) {
            	List<MembershipTransaction> txns = userService.getUserMembershipTransactions(user.getId());
            	Membership membership = userService.getUserMembership(user.getId());
            	if(membership != null){
            		userJsonObject.add("membership", convertMembership(membership));
            	}
                if(txns != null && !txns.isEmpty()){
                	 userJsonObject.add("membershipTransactions", convertMembershipTransactions(txns));
                }
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MONTH, 1);
                boolean feeAllow = false;
                System.out.println("membership = "+membership);
                System.out.println("With iN mOnth expiry = "+calendar.getTime().after(membership.getEndDate()));
                if(membership == null || calendar.getTime().after(membership.getEndDate())){
                	feeAllow = true;
                }
                userJsonObject.addProperty("feeAllow", feeAllow);
            } 
            
            context.add(name, userJsonObject);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
