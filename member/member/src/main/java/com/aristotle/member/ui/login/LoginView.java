package com.aristotle.member.ui.login;

import org.springframework.beans.factory.annotation.Autowired;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.User;
import com.aristotle.member.service.MemberService;
import com.aristotle.member.ui.NavigableView;
import com.aristotle.member.ui.util.NavigatorUtil;
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
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

//@SpringComponent
//@UIScope
@SpringView(name = LoginView.NAVIAGATION_NAME)
public class LoginView extends VerticalLayout implements NavigableView{

	private static final long serialVersionUID = 1L;
	public static final String NAVIAGATION_NAME = "login";

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


	public LoginView() {
		
		buildUiScreen();
		addListeners();
	}

	public void init() {
		this.setWidth("400px");

	}

	@Override
	public void enter(ViewChangeEvent event) {
		init();
	}
	
	private void buildUiScreen(){
		this.addStyleName(ValoTheme.LAYOUT_CARD);
		MarginInfo marginInfo = new MarginInfo(true);
		this.setMargin(marginInfo);
		
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
		ViewHelper.addNaviagationClickListener(this, registerButton, RegisterView.NAVIAGATION_NAME);
		loginButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					errorLabel.setVisible(false);
					User user = memberService.login(userName.getValue(), password.getValue());
	            	vaadinSessionUtil.setLoggedInUserinSession(user);
					Notification.show("Welcome "+userName.getValue()+", login succesfull", Type.HUMANIZED_MESSAGE);
					NavigatorUtil.goToHomePage(LoginView.this);
				} catch (AppException e) {
					errorLabel.setValue(e.getMessage());
					errorLabel.setVisible(true);
					LoginView.this.userName.setComponentError(new UserError(e.getMessage()));
				}
			}
		});
	}

	@Override
	public String getNaviagationName() {
		return LoginView.NAVIAGATION_NAME;
	}

}
