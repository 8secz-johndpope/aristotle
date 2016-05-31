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

	private PasswordField password;
	private TextField userName;
	private VerticalLayout content;
	private Button loginButton;
	private Button registerButton;
	private Label errorLabel;
	
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
		userName = new TextField("User Name");
		userName.setIcon(FontAwesome.USER);
		//userName.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		
		password = new PasswordField("Password");
		password.setIcon(FontAwesome.LOCK);
		//password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		
		loginButton = new Button("Login", FontAwesome.SIGN_IN);
		loginButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		registerButton = new Button("Not Registered Yet? Register Now", FontAwesome.REGISTERED);
		registerButton.setStyleName(ValoTheme.BUTTON_LINK);
		
		errorLabel = new Label();
		errorLabel.setStyleName("error");
		errorLabel.setVisible(false);
		
		content = new VerticalLayout(errorLabel, userName, password, loginButton, registerButton);
		content.addStyleName("login-panel");
		
		content.setSizeFull();

		this.addComponent(content);
	}
	
	private void addListeners(){
		loginButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					errorLabel.setVisible(false);
					memberService.login(userName.getValue(), password.getValue());
					Notification.show("Welcome "+userName.getValue()+", login succesfull", Type.HUMANIZED_MESSAGE);
					getUI().getNavigator().navigateTo("home");
				} catch (AppException e) {
					errorLabel.setValue(e.getMessage());
					errorLabel.setVisible(true);
					HomeView.this.userName.setComponentError(new UserError(e.getMessage()));
				}
			}
		});
	}

	@Override
	public String getNaviagationName() {
		return HomeView.NAVIAGATION_NAME;
	}

}
