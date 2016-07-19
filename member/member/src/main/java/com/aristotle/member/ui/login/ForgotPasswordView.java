package com.aristotle.member.ui.login;

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
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.UserError;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Image;
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

@SpringView(name = ForgotPasswordView.NAVIAGATION_NAME)
public class ForgotPasswordView extends VerticalLayout implements NavigableView{

	private static final long serialVersionUID = 1L;
	public static final String NAVIAGATION_NAME = "forgot";

	private TextField email;
	private VerticalLayout content;
	private Button passwordRecoveryButton;
	private Button registerButton;
	private Button loginButton;
	private Label errorLabel;
	private Image image;
	
	@Autowired
	private MemberService memberService;
	@Autowired
	private VaadinSessionUtil vaadinSessionUtil;
	@Autowired
	private UiComponentsUtil uiComponentsUtil;

	private ContextHelp contextHelp;
	private volatile boolean initialized = false;


	public ForgotPasswordView() {
	}

	public void init() {
		if(!initialized){
			this.setWidth("450px");
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
		this.addStyleName("panel-with-3d");
		MarginInfo marginInfo = new MarginInfo(true);
		this.setMargin(marginInfo);
		
		contextHelp = new ContextHelp();
		contextHelp.extend(UI.getCurrent());
		contextHelp.setFollowFocus(true);
		
		email = uiComponentsUtil.buildTextField(contextHelp, FontAwesome.USER, "Email/Mobile Number", "Enter your email or phone number with which you registered");
		email.setWidth("350px");
		
		passwordRecoveryButton = uiComponentsUtil.buildButton(ValoTheme.BUTTON_FRIENDLY, FontAwesome.SEND, "Send Password Receovery Details");

		registerButton = uiComponentsUtil.buildButton(ValoTheme.BUTTON_LINK, FontAwesome.REGISTERED, "Not Registered Yet? Register Now");
		
		loginButton = uiComponentsUtil.buildButton(ValoTheme.BUTTON_LINK, FontAwesome.SIGN_IN, "Already Registered? Login Now");

		errorLabel = uiComponentsUtil.buildErrorlabel();
		
		image = new Image();
		image.setWidth("300px");
		image.setSource(new ExternalResource("https://pbs.twimg.com/profile_images/599546377253814272/S1-kHFM8.png"));

		
		content = new VerticalLayout(image, errorLabel, email, passwordRecoveryButton, registerButton, loginButton);
		content.addStyleName("login-panel");
		
		content.setSizeFull();

		this.addComponent(content);
	}
	
	private void addListeners(){
		ViewHelper.addNaviagationClickListener(this, registerButton, RegisterView.NAVIAGATION_NAME);
		ViewHelper.addNaviagationClickListener(this, loginButton, LoginView.NAVIAGATION_NAME);
		passwordRecoveryButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					errorLabel.setVisible(false);
					//User user = memberService.login(email.getValue(), password.getValue());
	            	//vaadinSessionUtil.setLoggedInUserinSession(user);
					Notification.show("Welcome "+email.getValue()+", login succesfull", Type.HUMANIZED_MESSAGE);
					NavigatorUtil.goToHomePage(ForgotPasswordView.this);
				} catch (Exception e) {
					errorLabel.setValue(e.getMessage());
					errorLabel.setVisible(true);
					ForgotPasswordView.this.email.setComponentError(new UserError(e.getMessage()));
				}
			}
		});
	}

	@Override
	public String getNaviagationName() {
		return ForgotPasswordView.NAVIAGATION_NAME;
	}

}
