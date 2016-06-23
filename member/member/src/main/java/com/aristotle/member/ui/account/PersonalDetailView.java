package com.aristotle.member.ui.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.jonatan.contexthelp.ContextHelp;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.User;
import com.aristotle.member.service.MemberService;
import com.aristotle.member.ui.NavigableView;
import com.aristotle.member.ui.util.NavigatorUtil;
import com.aristotle.member.ui.util.UiComponentsUtil;
import com.aristotle.member.ui.util.VaadinSessionUtil;
import com.aristotle.member.ui.util.ViewHelper;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.UserError;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

//@SpringComponent
//@UIScope
@SpringView(name = PersonalDetailView.NAVIAGATION_NAME)
public class PersonalDetailView extends VerticalLayout implements NavigableView{

	private static final long serialVersionUID = 1L;
	public static final String NAVIAGATION_NAME = "personal";

	private PasswordField password;
	private TextField userName;
	private VerticalLayout content;
	private Button loginButton;
	private Button registerButton;
	private Label errorLabel;
	
	@Autowired
	private MemberService memberService;
	@Autowired
	private VaadinSessionUtil vaadinSessionUtil;
	@Autowired
	private UiComponentsUtil uiComponentsUtil;

	private ContextHelp contextHelp;
	private volatile boolean initialized = false;


	public PersonalDetailView() {
	}

	public void init() {
		if(!initialized){
			this.setWidth("400px");
			buildUiScreen();
			addListeners();
			initialized = true;
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		init();
	}
	
	private void buildUiScreen(){
		this.addStyleName(ValoTheme.LAYOUT_CARD);
		MarginInfo marginInfo = new MarginInfo(true);
		this.setMargin(marginInfo);
		
		contextHelp = new ContextHelp();
		contextHelp.extend(UI.getCurrent());
		contextHelp.setFollowFocus(true);
		
		userName = uiComponentsUtil.buildTextField(contextHelp, FontAwesome.USER, "Email/Mobile Number", "Enter your email or phone number with which you registered");
		
		password = uiComponentsUtil.buildPasswordField(contextHelp, FontAwesome.LOCK, "Password" , "Enter your password, its case senstive. Means 'Password' and 'PASSWORD' will not match");
		
		loginButton = new Button("Login", FontAwesome.SIGN_IN);
		loginButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		loginButton.setId("login_button");

		registerButton = new Button("Not Registered Yet? Register Now", FontAwesome.REGISTERED);
		registerButton.setStyleName(ValoTheme.BUTTON_LINK);
		registerButton.setId("register_button");

		errorLabel = uiComponentsUtil.buildErrorlabel();
		
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
					User user = memberService.login(userName.getValue(), password.getValue());
	            	vaadinSessionUtil.setLoggedInUserinSession(user);
					Notification.show("Welcome "+userName.getValue()+", login succesfull", Type.HUMANIZED_MESSAGE);
					NavigatorUtil.goToHomePage(PersonalDetailView.this);
				} catch (AppException e) {
					errorLabel.setValue(e.getMessage());
					errorLabel.setVisible(true);
					PersonalDetailView.this.userName.setComponentError(new UserError(e.getMessage()));
				}
			}
		});
	}

	@Override
	public String getNaviagationName() {
		return PersonalDetailView.NAVIAGATION_NAME;
	}

}
