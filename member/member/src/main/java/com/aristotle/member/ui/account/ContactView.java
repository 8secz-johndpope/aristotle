package com.aristotle.member.ui.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.jonatan.contexthelp.ContextHelp;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Email;
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
import com.vaadin.ui.HorizontalLayout;
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
@SpringView(name = ContactView.NAVIAGATION_NAME)
public class ContactView extends VerticalLayout implements NavigableView{

	private static final long serialVersionUID = 1L;
	public static final String NAVIAGATION_NAME = "contact";

	private TextField existingEmail;

	private TextField email;
	private TextField confirmEmail;

	private VerticalLayout content;
	private Button saveEmailButton;
	private Button editEmailButton;
	private Button cancelEditEmailButton;

	private Label errorLabel;
	private Label successLabel;
	private Label emailLabel;

	private Email userEmail;
	
	@Autowired
	private MemberService memberService;
	@Autowired
	private VaadinSessionUtil vaadinSessionUtil;
	@Autowired
	private UiComponentsUtil uiComponentsUtil;

	private ContextHelp contextHelp;
	private volatile boolean initialized = false;


	public ContactView() {
	}

	public void init() throws AppException {
		if(!initialized){
			this.setWidth("90%");
			buildUiScreen();
			addListeners();
			loadUserData();
			initialized = true;
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		try{
			init();	
		}catch(Exception ex){
			uiComponentsUtil.setLabelError(errorLabel, ex);
		}
		
	}
	
	private void loadUserData() throws AppException{
		
		User loggedInUser = vaadinSessionUtil.getLoggedInUserFromSession();

		userEmail = memberService.getUserEmail(loggedInUser.getId());
		if(userEmail == null){
			existingEmail.setVisible(false);
			emailLabel.setValue("No Registered email, please register one now");
			setEditEmailLayoutVisibility(true);
		}else{
			existingEmail.setVisible(true);
			existingEmail.setValue(userEmail.getEmail());
			if(userEmail.isConfirmed()){
				emailLabel.setValue("Confirmed");
				emailLabel.addStyleName(ValoTheme.NOTIFICATION_SUCCESS);

			}else{
				emailLabel.setValue("Not Confirmed");
				emailLabel.addStyleName(ValoTheme.NOTIFICATION_FAILURE);
			}
		}
	}
	
	private void setEditEmailLayoutVisibility(boolean visibility){
		email.setVisible(visibility);
		confirmEmail.setVisible(visibility);
		saveEmailButton.setVisible(visibility);
		cancelEditEmailButton.setVisible(visibility);
	}
	
	private void buildUiScreen(){
		this.addStyleName(ValoTheme.LAYOUT_CARD);
		MarginInfo marginInfo = new MarginInfo(true);
		this.setMargin(marginInfo);
		
		contextHelp = new ContextHelp();
		contextHelp.extend(UI.getCurrent());
		contextHelp.setFollowFocus(true);
		
		emailLabel = new Label();
		
		existingEmail = uiComponentsUtil.buildTextField(contextHelp, FontAwesome.ENVELOPE, "Registered Email", "Registered Email");
		existingEmail.setWidth("400px");

		existingEmail.setEnabled(false);
		email = uiComponentsUtil.buildTextField(contextHelp, FontAwesome.ENVELOPE, "Email", "Enter New Email. We will send a confirmation email on this email id");
		confirmEmail = uiComponentsUtil.buildTextField(contextHelp, FontAwesome.ENVELOPE, "Confirm Email", "Enter New Email same as above");
		email.setWidth("400px");
		confirmEmail.setWidth("400px");
		editEmailButton = uiComponentsUtil.buildButton(ValoTheme.BUTTON_LINK, FontAwesome.EDIT, "Edit Email");

		saveEmailButton = uiComponentsUtil.buildButton(ValoTheme.BUTTON_FRIENDLY, FontAwesome.SIGN_IN, "Update Email");
		cancelEditEmailButton = uiComponentsUtil.buildButton(ValoTheme.BUTTON_QUIET, FontAwesome.UNDO, "Cancel");

		setEditEmailLayoutVisibility(false);
		
		errorLabel = uiComponentsUtil.buildErrorlabel();
		successLabel = uiComponentsUtil.buildSuccessLabel();

		HorizontalLayout emailLayout = new HorizontalLayout(existingEmail, emailLabel, editEmailButton);
		
		HorizontalLayout updateEmailButtonLayout = new HorizontalLayout(saveEmailButton, cancelEditEmailButton);

		
		content = new VerticalLayout(errorLabel, successLabel, emailLayout, email, confirmEmail, updateEmailButtonLayout);
		Panel contactPanel = new Panel("Contact Details");
		contactPanel.setContent(content);

		this.addComponent(contactPanel);
	}
	
	private void addListeners(){
		saveEmailButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				try{
					errorLabel.setVisible(false);
					memberService.updateEmail(userEmail.getId(), email.getValue(), confirmEmail.getValue());
					setEditEmailLayoutVisibility(false);
					loadUserData();
					successLabel.setValue("Email updated succesfully. Please check your email to confirm");
					successLabel.setVisible(true);
					email.setValue("");
					confirmEmail.setValue("");
				}catch(Exception ex){
					uiComponentsUtil.setLabelError(errorLabel, ex);
				}
				
			}
		});
		editEmailButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				setEditEmailLayoutVisibility(true);
			}
		});
		cancelEditEmailButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				setEditEmailLayoutVisibility(false);
			}
		});
		
	}

	@Override
	public String getNaviagationName() {
		return ContactView.NAVIAGATION_NAME;
	}

}
