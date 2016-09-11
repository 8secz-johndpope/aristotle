package com.aristotle.member.ui.login;

import java.awt.Checkbox;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.vaadin.jonatan.contexthelp.ContextHelp;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Location;
import com.aristotle.core.persistance.User;
import com.aristotle.core.service.LocationService;
import com.aristotle.member.service.MemberService;
import com.aristotle.member.ui.NavigableView;
import com.aristotle.member.ui.account.PersonalDetailView;
import com.aristotle.member.ui.util.LoginHelper;
import com.aristotle.member.ui.util.UiComponentsUtil;
import com.aristotle.member.ui.util.ViewHelper;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.wcs.wcslib.vaadin.widget.recaptcha.ReCaptcha;
import com.wcs.wcslib.vaadin.widget.recaptcha.shared.ReCaptchaOptions;

//@SpringComponent
//@UIScope
@SpringView(name = RegisterView.NAVIAGATION_NAME)
public class RegisterView extends VerticalLayout implements NavigableView{

	private static final long serialVersionUID = 1L;
	public static final String NAVIAGATION_NAME = "register";

	private PasswordField password;
	private PasswordField passwordConfirm;

	private TextField userName;
	private TextField email;
	private ComboBox countryCombobox;
	private TextField phoneNumber;
	private CheckBox nri;
	private CheckBox becomeMember;


	private Button loginButton;
	private Button registerButton;
	private VerticalLayout page1;
	private volatile boolean initialized = false;
	private ReCaptcha captcha;
	private String selectedCountryIsdCode;
	private Label errorLabel;
	private Label successLabel;
	private Label memberLabel;
	private Image image;

	private ContextHelp contextHelp;

	
	@Autowired
	private LocationService locationService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private UiComponentsUtil uiComponentsUtil;
	@Autowired
	private LoginHelper loginHelper;
	@Value("${captcha_site_key}")
	private String captchaSiteKey;
	@Value("${captcha_site_secret}")
	private String captchSiteSecret;


	public RegisterView() {
	}

	public void init() {
		if(!initialized){
			contextHelp = new ContextHelp();
			contextHelp.extend(UI.getCurrent());
			contextHelp.setFollowFocus(true);
			
			buildPageOne();
			addListners();
			
			
			
			this.addComponent(page1);
			initialized = true;
    		this.setWidth("500px");

		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		init();
	}

	@Override
	public String getNaviagationName() {
		return RegisterView.NAVIAGATION_NAME;
	}
	
	private void buildPageOne(){
		try{
			
			this.addStyleName(ValoTheme.LAYOUT_CARD);
			this.addStyleName("panel-with-3d");
			MarginInfo marginInfo = new MarginInfo(true);
			this.setMargin(marginInfo);
			
			userName = uiComponentsUtil.buildTextField(contextHelp, FontAwesome.USER, "Name", "Enter Your Full Name");
			
			email = uiComponentsUtil.buildTextField(contextHelp, FontAwesome.ENVELOPE, "Email", "Enter your email, where we will send you your registration details");

			HorizontalLayout nameEmailLayout = new HorizontalLayout(userName, email);

			password = uiComponentsUtil.buildPasswordField(contextHelp, FontAwesome.LOCK, "Password", "Enter a hard to guesss password to keep your account secure");	

			passwordConfirm = uiComponentsUtil.buildPasswordField(contextHelp, FontAwesome.LOCK, "Confirm Password", "Re-Enter same password");	

			HorizontalLayout passwordLayout = new HorizontalLayout(password, passwordConfirm);
			
			
			List<Location> countries = locationService.getAllCountries();
			for(Location oneLocation : countries){
				oneLocation.setName(oneLocation.getName() +"("+oneLocation.getIsdCode()+")");
			}
			countryCombobox = uiComponentsUtil.buildCountryComboBoxWithIsdCode(contextHelp, FontAwesome.FLAG, "Country Code", "Select your country where you live");
			countryCombobox.setVisible(false);
			
			phoneNumber = uiComponentsUtil.buildTextField(contextHelp, FontAwesome.MOBILE, "Mobile Number", "Enter your mobile number <b>WITHOUT</b> country code<br> i.e. 9876543210");

			HorizontalLayout phoneLayout = new HorizontalLayout(countryCombobox, phoneNumber);

			loginButton = new Button("Already Registered? Login Now", FontAwesome.SIGN_IN);
			loginButton.setStyleName(ValoTheme.BUTTON_LINK);
			loginButton.setId("login_button");
			
			registerButton = new Button("Register", FontAwesome.REGISTERED);
			registerButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			registerButton.setId("register_button");
			registerButton.setDisableOnClick(true);

			captcha = new ReCaptcha(
					captchSiteSecret, captchaSiteKey,
				    new ReCaptchaOptions() {{//your options
				        theme = "white";
				    }}
				);
			captcha.setId("captcha");
			
			nri = new CheckBox("I am NRI");
			nri.setId("nri");
			becomeMember = new CheckBox("Register as member");
			becomeMember.setId("becomeMember");
			
			errorLabel = uiComponentsUtil.buildErrorlabel();
			
			
			successLabel = uiComponentsUtil.buildSuccessLabel();
			memberLabel = new Label();
			memberLabel.setContentMode(ContentMode.HTML);
			memberLabel.setValue("You are registering as <b>supporter only</b>, to become member please select option above");

			image = new Image();
			image.setWidth("300px");
			image.setSource(new ExternalResource("https://pbs.twimg.com/profile_images/599546377253814272/S1-kHFM8.png"));
			
			
			page1 = new VerticalLayout(image, errorLabel, successLabel, becomeMember, memberLabel, nameEmailLayout, passwordLayout, nri, phoneLayout, captcha, registerButton, loginButton);
			page1.setSizeFull();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	private void addListners(){
		ViewHelper.addNaviagationClickListener(this, loginButton, LoginView.NAVIAGATION_NAME);
		nri.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				Boolean selection = (Boolean)event.getProperty().getValue();
				countryCombobox.setVisible(selection);
				if(selection){
					selectedCountryIsdCode = "91";
				}
			}
		});
		becomeMember.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				Boolean selection = (Boolean)event.getProperty().getValue();
				if(selection){
					memberLabel.setValue("You are registering as <b>member, you will be redirected to pay membership fee of Rs. 50</b>");
				}else{
					memberLabel.setValue("You are registering as <b>supporter only</b>, to become member please select option above");
				}
			}
		});
		
		
		countryCombobox.addValueChangeListener(new Property.ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				Location selectedCountry = (Location) event.getProperty().getValue();
				if(selectedCountry != null){
					selectedCountryIsdCode = selectedCountry.getIsdCode();
				}
			}
		});
		
		registerButton.addClickListener(new Button.ClickListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				errorLabel.setVisible(false);
				successLabel.setVisible(false);
				registerButton.setEnabled(false);
				
				boolean isNri = nri.getValue();
				String selectedCountryIsdCode = "91";
				if(isNri){
					Location selectedCountry = (Location) countryCombobox.getValue();
					if(selectedCountry != null){
						selectedCountryIsdCode = selectedCountry.getIsdCode();
					}	else{
						selectedCountryIsdCode = null;
					}
				}
				
				try {
					if(!isValidCaptcha()){
						throw new AppException("Captcha is not valid");
						
					}
					memberService.register(userName.getValue(), password.getValue(), passwordConfirm.getValue(), email.getValue(), selectedCountryIsdCode, phoneNumber.getValue(), nri.getValue());
					successLabel.setVisible(true);
					successLabel.setValue("Member Registered Succesfully");
					
					loginHelper.login(email.getValue(), password.getValue());
					Boolean selection = becomeMember.getValue();
					if(selection){
						Notification.show("Welcome "+userName.getValue()+", Please complete the payment, you will be redirected to payment page now", Type.HUMANIZED_MESSAGE);
						Page.getCurrent().open("https://www.instamojo.com/SwarajAbhiyan/membership-fee-for-swaraj-abhiyannew/", Page.getCurrent().getWindowName());
						registerButton.setEnabled(true);
					}else{
						Notification.show("Welcome "+userName.getValue()+", Please Complete Your profile", Type.HUMANIZED_MESSAGE);
						getUI().getNavigator().navigateTo(PersonalDetailView.NAVIAGATION_NAME);
						registerButton.setEnabled(true);
					}
					
				} catch (Exception e) {
					//Notification.show(e.getMessage(), Notification.Type.ERROR_MESSAGE);
				    captcha.reload();
				    uiComponentsUtil.setLabelError(errorLabel, e);
					e.printStackTrace();
				}finally {
					registerButton.setEnabled(true);
				}
				
			}
		});
		

	}
	private boolean isValidCaptcha(){
		if(captchaSiteKey.equals("6LeIxAcTAAAAAJcZVRqyHh71UMIEGNQ_MXjiZKhI")){//Test Key
			return true;
		}
		return captcha.validate();
	}

}
