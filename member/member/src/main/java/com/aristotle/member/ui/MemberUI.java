package com.aristotle.member.ui;

import javax.servlet.annotation.WebServlet;

import org.springframework.beans.factory.annotation.Autowired;

import com.aristotle.core.persistance.User;
import com.aristotle.core.service.UserService;
import com.aristotle.member.service.MemberService;
import com.aristotle.member.ui.util.VaadinSessionUtil;
import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.spring.server.SpringVaadinServlet;
import com.vaadin.ui.UI;

@SpringUI(path = "/")
@Theme("mytheme")
@JavaScript(value={"http://www.google.com/recaptcha/api/js/recaptcha_ajax.js"})
@Widgetset("Member")
public class MemberUI extends UI {

	private static final long serialVersionUID = 1L;
	@Autowired
	private SpringViewProvider viewProvider;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private VaadinSessionUtil vaadinSessionUtil;

	@Override
    protected void init(VaadinRequest vaadinRequest) {
		System.out.println("Creating UI , viewProvider = " + viewProvider);
		//setupLoggedInuserForDevMachine();
        setContent(new MainLayout(viewProvider, vaadinSessionUtil));
    }
	private void setupLoggedInuserForDevMachine(){
		try{
			User loggedinUser = vaadinSessionUtil.getLoggedInUserFromSession();
	        if(loggedinUser == null && "DEV_MACHINE".equalsIgnoreCase(System.getProperty("ENV"))){
	        	loggedinUser = memberService.login(System.getProperty("USER"), System.getProperty("PASSWORD"));
	        	vaadinSessionUtil.setLoggedInUserinSession(loggedinUser);
	        }
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MemberUI.class, productionMode = true)
    public static class MyUIServlet extends SpringVaadinServlet {
    }
}
