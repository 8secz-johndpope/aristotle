package com.aristotle.member.ui.login;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.aristotle.core.persistance.Location;
import com.aristotle.core.service.LocationService;
import com.aristotle.member.ui.NavigableView;
import com.aristotle.member.ui.util.ViewHelper;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

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

	private Button loginButton;
	private Button registerButton;
	private VerticalLayout page1;
	private VerticalLayout page2;
	private volatile boolean initialized = false;
	
	@Autowired
	private LocationService locationService;


	public RegisterView() {
	}

	public void init() {
		if(!initialized){
			buildPageOne();
			this.addComponent(page1);
			initialized = true;
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
			userName = new TextField("User Name");
			userName.setIcon(FontAwesome.USER);
			userName.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
			
			email = new TextField("Email");
			
			countryCombobox = new ComboBox();
			List<Location> countries = locationService.getAllCountries();
			for(Location oneLocation : countries){
				oneLocation.setName(oneLocation.getName() +"("+oneLocation.getIsdCode()+")");
			}
			BeanItemContainer<Location> countryContainer = new BeanItemContainer<Location>(Location.class);
			countryContainer.addAll(countries);
			countryCombobox.setCaption("Country Code");
			countryCombobox.setContainerDataSource(countryContainer);
			countryCombobox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			countryCombobox.setItemCaptionPropertyId("name");
			countryCombobox.setVisible(false);

			
			phoneNumber = new TextField("Mobile Number");

			password = new PasswordField("Password");
			password.setIcon(FontAwesome.LOCK);
			password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
			
			passwordConfirm = new PasswordField("Confirm Password");
			passwordConfirm.setIcon(FontAwesome.LOCK);
			passwordConfirm.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
			
			
			loginButton = new Button("Already Registered? Login Now", FontAwesome.SIGN_IN);
			loginButton.setStyleName(ValoTheme.BUTTON_LINK);
			
			registerButton = new Button("Register", FontAwesome.REGISTERED);
			registerButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			
			nri = new CheckBox("I am NRI");
			nri.addValueChangeListener(new Property.ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					Boolean selection = (Boolean)event.getProperty().getValue();
					countryCombobox.setVisible(selection);
				}
			});
			
			
			ViewHelper.addNaviagationClickListener(this, loginButton, LoginView.NAVIAGATION_NAME);
			
			HorizontalLayout horizontalLayout = new HorizontalLayout(countryCombobox, phoneNumber);

			page1 = new VerticalLayout(userName, password, passwordConfirm, email, nri, horizontalLayout, registerButton, loginButton);
			
			page1.setSizeFull();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	private void buildPageTwo(){
		userName = new TextField("User Name");
		userName.setIcon(FontAwesome.USER);
		userName.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		
		email = new TextField("Email");
		phoneNumber = new TextField("Mobile Number");

		password = new PasswordField("Password");
		password.setIcon(FontAwesome.LOCK);
		password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		
		passwordConfirm = new PasswordField("Confirm Password");
		passwordConfirm.setIcon(FontAwesome.LOCK);
		passwordConfirm.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		
		
		loginButton = new Button("Already Registered? Login Now", FontAwesome.SIGN_IN);
		loginButton.setStyleName(ValoTheme.BUTTON_LINK);
		
		registerButton = new Button("Register", FontAwesome.REGISTERED);
		registerButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		ViewHelper.addNaviagationClickListener(this, loginButton, LoginView.NAVIAGATION_NAME);

		
		page1 = new VerticalLayout(userName, password, passwordConfirm, email, phoneNumber, registerButton, loginButton);
		page1.addStyleName("login-panel");
		
		page1.setSizeFull();
	}

}
