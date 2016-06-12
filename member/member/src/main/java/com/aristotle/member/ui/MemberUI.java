package com.aristotle.member.ui;

import javax.servlet.annotation.WebServlet;

import org.springframework.beans.factory.annotation.Autowired;

import com.aristotle.core.service.UserService;
import com.aristotle.member.ui.util.VaadinSessionUtil;
import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;

@SpringUI(path = "/")
@Theme("mytheme")
@JavaScript("http://www.google.com/recaptcha/api/js/recaptcha_ajax.js")
@Widgetset("Member")
public class MemberUI extends UI {

	private static final long serialVersionUID = 1L;
	@Autowired
	private SpringViewProvider viewProvider;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private VaadinSessionUtil vaadinSessionUtil;

	@Override
    protected void init(VaadinRequest vaadinRequest) {
        setContent(new MainLayout(viewProvider, vaadinSessionUtil));
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MemberUI.class, productionMode = true)
    public static class MyUIServlet extends VaadinServlet {
    }
}
