package com.aristotle.member.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.aristotle.core.exception.AppException;
import com.aristotle.member.service.MemberService;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.UserError;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringView(name = HomeView.NAVIAGATION_NAME)
public class HomeView extends LoggedInView{

	private static final long serialVersionUID = 1L;
	public static final String NAVIAGATION_NAME = "home";

	private VerticalLayout content;
	private Label errorLabel;
	private Label successLabel;
	
	@Autowired
	private MemberService memberService;


	public HomeView() {
		
		buildUiScreen();
		addListeners();
	}

	public void init() {
		
	}

	@Override
	public void enter(ViewChangeEvent event) {
		super.enter(event);
		init();
	}
	
	private void buildUiScreen(){
		errorLabel = new Label();
		errorLabel.setStyleName("error");
		errorLabel.setVisible(false);
		
		content = new VerticalLayout(errorLabel);//
		content.addStyleName("login-panel");
		
		content.setSizeFull();

		this.addComponent(content);
	}
	
	private void addListeners(){
		
	}

	@Override
	public String getNaviagationName() {
		return HomeView.NAVIAGATION_NAME;
	}

}
