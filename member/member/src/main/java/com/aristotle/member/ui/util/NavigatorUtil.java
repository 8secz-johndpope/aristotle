package com.aristotle.member.ui.util;

import com.aristotle.member.ui.HomeView;
import com.aristotle.member.ui.login.LoginView;
import com.aristotle.member.ui.login.RegisterView;
import com.vaadin.server.AbstractClientConnector;

public class NavigatorUtil {

	public static void goToHomePage(AbstractClientConnector clientConnector){
		clientConnector.getUI().getNavigator().navigateTo(HomeView.NAVIAGATION_NAME);
	}
	public static void goToLoginPage(AbstractClientConnector clientConnector){
		clientConnector.getUI().getNavigator().navigateTo(LoginView.NAVIAGATION_NAME);
	}
	public static void goToRegisterPage(AbstractClientConnector clientConnector){
		clientConnector.getUI().getNavigator().navigateTo(RegisterView.NAVIAGATION_NAME);
	}
}
