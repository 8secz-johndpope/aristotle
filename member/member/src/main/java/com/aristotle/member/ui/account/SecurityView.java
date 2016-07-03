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
import com.vaadin.ui.Panel;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

//@SpringComponent
//@UIScope
@SpringView(name = SecurityView.NAVIAGATION_NAME)
public class SecurityView extends VerticalLayout implements NavigableView{

	private static final long serialVersionUID = 1L;
	public static final String NAVIAGATION_NAME = "security";

	private PasswordField currentPassword;
	private PasswordField newPassword;
	private PasswordField newConfirmPassword;
	private VerticalLayout content;
	private Button changePasswordButton;
	private Label errorLabel;
	private Label successLabel;

	
	@Autowired
	private MemberService memberService;
	@Autowired
	private VaadinSessionUtil vaadinSessionUtil;
	@Autowired
	private UiComponentsUtil uiComponentsUtil;

	private ContextHelp contextHelp;
	private volatile boolean initialized = false;


	public SecurityView() {
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
		
		contextHelp = new ContextHelp();
		contextHelp.extend(UI.getCurrent());
		contextHelp.setFollowFocus(true);
		
		currentPassword = uiComponentsUtil.buildPasswordField(contextHelp, FontAwesome.LOCK, "Current Password" , "Enter your current password, its case senstive. Means 'Password' and 'PASSWORD' will not match");
		newPassword = uiComponentsUtil.buildPasswordField(contextHelp, FontAwesome.LOCK, "Password" , "Enter new password, its case senstive. Means 'Password' and 'PASSWORD' will not match");
		newConfirmPassword = uiComponentsUtil.buildPasswordField(contextHelp, FontAwesome.LOCK, "Confirm Password" , "Enter password same as above, its case senstive. Means 'Password' and 'PASSWORD' will not match");

		changePasswordButton = uiComponentsUtil.buildButton(ValoTheme.BUTTON_FRIENDLY, FontAwesome.SIGN_IN, "Change Password");

		errorLabel = uiComponentsUtil.buildErrorlabel();
		successLabel = uiComponentsUtil.buildSuccessLabel();

		
		content = new VerticalLayout(errorLabel, successLabel, currentPassword, newPassword, newConfirmPassword, changePasswordButton);
		content.setSizeFull();
		Panel securityPanel = new Panel("Security Details");
		securityPanel.setContent(content);
		
		

		this.addComponent(securityPanel);
	}
	
	private void addListeners(){
		changePasswordButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					errorLabel.setVisible(false);
					successLabel.setVisible(false);
					User loggedInUser = vaadinSessionUtil.getLoggedInUserFromSession();
					memberService.changePassword(loggedInUser.getId(), currentPassword.getValue(), newPassword.getValue(), newConfirmPassword.getValue());
					successLabel.setValue("Password updated successfully");
					successLabel.setVisible(true);

				} catch (Exception e) {
					errorLabel.setValue(e.getMessage());
					errorLabel.setVisible(true);
				}
			}
		});
	}

	@Override
	public String getNaviagationName() {
		return SecurityView.NAVIAGATION_NAME;
	}

}
