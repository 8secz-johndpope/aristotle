package com.aristotle.member.ui.account;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.easyuploads.UploadField;
import org.vaadin.easyuploads.UploadField.FieldType;
import org.vaadin.jonatan.contexthelp.ContextHelp;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Location;
import com.aristotle.core.persistance.User;
import com.aristotle.core.service.LocationService;
import com.aristotle.member.service.MemberService;
import com.aristotle.member.ui.NavigableView;
import com.aristotle.member.ui.util.NavigatorUtil;
import com.aristotle.member.ui.util.UiComponentsUtil;
import com.aristotle.member.ui.util.VaadinSessionUtil;
import com.aristotle.member.ui.util.ViewHelper;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.UserError;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.PopupDateField;
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
	
	//Personal Detail Form Component
	private Panel personalDetailPanel;

	private TextField userName;
	private ComboBox gender;
	private ComboBox idCardType;
	private TextField idCardNumber;
	private PopupDateField dateOfBirth;
	private TextField fatherName;
	private TextField motherName;
	private UploadField uploadField;
	private Button uploadImageButton;
	private Image userImage;

    
	private VerticalLayout personalDetailContentPanel;
	private Button saveButton;
	private Label errorLabel;
	private Label successLabel;

	//Location Form Related Component
	private Panel locationPanel;
	private VerticalLayout locationContentPanel;

	private CheckBox nri;
	private ComboBox country;
	private ComboBox countryRegion;
	private ComboBox countryRegionArea;

	private ComboBox livingState;
	private ComboBox livingDistrict;
	private ComboBox livingAc;
	private ComboBox livingPc;
	
	private ComboBox votingState;
	private ComboBox votingDistrict;
	private ComboBox votingAc;
	private ComboBox votingPc;

	@Autowired
	private MemberService memberService;
	@Autowired
	private VaadinSessionUtil vaadinSessionUtil;
	@Autowired
	private UiComponentsUtil uiComponentsUtil;
	@Autowired
	private LocationService locationService;

	private ContextHelp contextHelp;
	private volatile boolean initialized = false;

	public PersonalDetailView() {
	}

	public void init() throws AppException {
		if(!initialized){
			this.setWidth("100%");
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
	
	private void buildUiScreen(){
		this.addStyleName(ValoTheme.LAYOUT_CARD);
		personalDetailPanel = new Panel("Personal Details");
		
		contextHelp = new ContextHelp();
		contextHelp.extend(UI.getCurrent());
		contextHelp.setFollowFocus(true);
		
		userName = uiComponentsUtil.buildTextField(contextHelp, FontAwesome.USER, "Name", "Enter your email or phone number with which you registered");
		
		uploadImageButton = uiComponentsUtil.buildButton(ValoTheme.BUTTON_FRIENDLY, FontAwesome.UPLOAD, "Upload");
		
		gender = uiComponentsUtil.buildComboBox(contextHelp, "Gender", "Select Gender", "Male"," Female", "Others");
		
		idCardType = uiComponentsUtil.buildComboBox(contextHelp, "ID card Type", "Select Gender", "Ration Card"," Driving License", "Voter Card", "Passport");
		idCardNumber = uiComponentsUtil.buildTextField(contextHelp, FontAwesome.CREDIT_CARD, "ID Card Number", "Please enter ID card number. i.e. if yopu have selected Passport in Card Type field, then enter Passport number");
				
		dateOfBirth = uiComponentsUtil.buildPopupDateField("Date of Birth");
		
		fatherName = uiComponentsUtil.buildTextField(contextHelp, FontAwesome.MALE, "Father Name", "Please enter your father name");
		motherName = uiComponentsUtil.buildTextField(contextHelp, FontAwesome.FEMALE, "Mother Name", "Please enter your mother name");

		
		
		errorLabel = uiComponentsUtil.buildErrorlabel();
		successLabel = uiComponentsUtil.buildSuccessLabel();
		
		uploadField = new UploadField();
		uploadField.setDisplayUpload(false);
		uploadField.setAcceptFilter(".jpg");
	    uploadField.setFieldType(FieldType.FILE);
	    
		userImage = new Image();
		userImage.setHeight("200px");
		userImage.setWidth("200px");
		
		saveButton = uiComponentsUtil.buildButton(ValoTheme.BUTTON_FRIENDLY, FontAwesome.SAVE, "Save");

		HorizontalLayout idCardLayout = new HorizontalLayout(idCardType, idCardNumber);
		
		VerticalLayout imageUploadButtonLayout = new VerticalLayout(uploadField, uploadImageButton);
		HorizontalLayout imageUploadLayout = new HorizontalLayout(userImage, imageUploadButtonLayout);

		personalDetailContentPanel = new VerticalLayout(errorLabel, imageUploadLayout, userName, gender, dateOfBirth, idCardLayout, fatherName, motherName, saveButton);
		personalDetailContentPanel.addStyleName("login-panel");
		
		//personalDetailContentPanel.setSizeFull();
		//personalDetailPanel.setContent(personalDetailContentPanel);
		
		
		//Create Location Panel
		nri = new CheckBox("NRI");
		
		Label nriCountrySelectorLabel = new Label("Currently Living - NRI location");
		nriCountrySelectorLabel.setStyleName(ValoTheme.LABEL_H3);
		
		try {
			country = uiComponentsUtil.buildCountryComboBox(contextHelp, null, "Country", "Select Country where you live currently. Other then India");
			country.setVisible(true);
			countryRegion = uiComponentsUtil.buildComboBox(contextHelp, "Country Region", "Select Country Region where you live currently", "England", "Wales");
			countryRegionArea = uiComponentsUtil.buildComboBox(contextHelp, "Country Region Area", "Select Country Region Area where you live currently", "London");

			livingState = uiComponentsUtil.buildStateComboBox(contextHelp, null, "Living State", "Select state where you live currently");
			livingState = uiComponentsUtil.buildComboBox(contextHelp, "Living District", "Select District where you live currently");
			livingPc = uiComponentsUtil.buildComboBox(contextHelp, "Living Parliament Constituency", "Select Parliament Constituency where you live currently");
			livingAc = uiComponentsUtil.buildComboBox(contextHelp, "Living Assembly Constituency", "Select Assembly Constituency where you live currently");

			votingState = uiComponentsUtil.buildStateComboBox(contextHelp, null, "Voting State", "Select state where you Registered as Voter");
			votingDistrict = uiComponentsUtil.buildComboBox(contextHelp, "Voting District", "Select District where you Registered as Voter");
			votingPc = uiComponentsUtil.buildComboBox(contextHelp, "Voting Parliament Constituency", "Select Parliament Constituency where you Registered as Voter");
			votingAc = uiComponentsUtil.buildComboBox(contextHelp, "Voting Assembly Constituency", "Select Assembly Constituency where you Registered as Voter");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		locationContentPanel = new VerticalLayout(nri, nriCountrySelectorLabel, country, countryRegion, countryRegionArea, livingState, livingState,livingPc, livingAc, votingState, votingDistrict, votingPc, votingAc );

		//locationPanel = new Panel("Location");
		//locationPanel.setContent(locationContentPanel);
		HorizontalLayout horizontalLayout = new HorizontalLayout(personalDetailContentPanel, locationContentPanel);
		personalDetailPanel.setContent(horizontalLayout);
		this.addComponent(horizontalLayout);
	}
	private void loadCountries() throws AppException{
		List<Location> countries = locationService.getAllCountries();
		for(Location oneLocation : countries){
			oneLocation.setName(oneLocation.getName() +"("+oneLocation.getIsdCode()+")");
		}
		country = uiComponentsUtil.buildCountryComboBox(contextHelp, FontAwesome.FLAG, "Country Code", "Select your country where you live");

	}
	private void loadUserData() throws AppException{
		
		User loggedInUser = vaadinSessionUtil.getLoggedInUserFromSession();

		User dbUser = memberService.getUserById(loggedInUser.getId());
		loadUserPersonalData(dbUser);
	}
	private void loadUserPersonalData(User dbUser){
		userName.setValue(dbUser.getName());
		gender.setValue(dbUser.getGender());
		idCardType.setValue(dbUser.getIdentityType());
		idCardNumber.setValue(dbUser.getIdentityNumber());
		dateOfBirth.setValue(dbUser.getDateOfBirth());
		fatherName.setValue(dbUser.getFatherName());
		motherName.setValue(dbUser.getMotherName());
		if(dbUser.getProfilePic() != null){
			userImage.setSource(new ExternalResource(dbUser.getProfilePic()));
		}
	}
	private void loadUserLocationData(User dbUser){
		userName.setValue(dbUser.getName());
		gender.setValue(dbUser.getGender());
		idCardType.setValue(dbUser.getIdentityType());
		idCardNumber.setValue(dbUser.getIdentityNumber());
		dateOfBirth.setValue(dbUser.getDateOfBirth());
		fatherName.setValue(dbUser.getFatherName());
		motherName.setValue(dbUser.getMotherName());
		if(dbUser.getProfilePic() != null){
			userImage.setSource(new ExternalResource(dbUser.getProfilePic()));
		}
	}
	private void addListeners(){
		uploadImageButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				showUploadedImage();
			}
		});
		uploadField.addListener(new Listener(){
	        @Override
	        public void componentEvent(Event event)
	        {
	        	showUploadedImage();
	        }
	    });
		saveButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				successLabel.setVisible(false);
				errorLabel.setVisible(false);
				User loggedInUser= vaadinSessionUtil.getLoggedInUserFromSession();
				loggedInUser = memberService.updateUserPersonalDetails(loggedInUser.getId(), userName.getValue(), String.valueOf(gender.getValue()), dateOfBirth.getValue(), String.valueOf(idCardType.getValue()), idCardNumber.getValue(), fatherName.getValue(), motherName.getValue());
				vaadinSessionUtil.setLoggedInUserinSession(loggedInUser);
				successLabel.setVisible(true);
				successLabel.setValue("personal Details updated successfully");
			}
		});
		
	}
	private void showUploadedImage() {
		FileResource resource = new FileResource((File)uploadField.getValue());
	    userImage.setSource(resource);
	}
	@Override
	public String getNaviagationName() {
		return PersonalDetailView.NAVIAGATION_NAME;
	}

}
