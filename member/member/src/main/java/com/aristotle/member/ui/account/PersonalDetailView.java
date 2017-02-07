package com.aristotle.member.ui.account;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.vaadin.easyuploads.UploadField;
import org.vaadin.easyuploads.UploadField.FieldType;
import org.vaadin.jonatan.contexthelp.ContextHelp;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Location;
import com.aristotle.core.persistance.User;
import com.aristotle.core.persistance.UserLocation;
import com.aristotle.core.service.AwsFileManager;
import com.aristotle.core.service.LocationService;
import com.aristotle.core.service.UserService;
import com.aristotle.member.service.AwsProfileImageUtil;
import com.aristotle.member.service.MemberService;
import com.aristotle.member.ui.NavigableView;
import com.aristotle.member.ui.util.NavigatorUtil;
import com.aristotle.member.ui.util.UiComponentsUtil;
import com.aristotle.member.ui.util.VaadinSessionUtil;
import com.aristotle.member.ui.util.ViewHelper;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
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
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

//@SpringComponent
//@UIScope
@SpringView(name = PersonalDetailView.NAVIAGATION_NAME)
public class PersonalDetailView extends VerticalLayout implements NavigableView {

	private static final long serialVersionUID = 1L;
	public static final String NAVIAGATION_NAME = "personal";

	// Personal Detail Form Component
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
	private TextArea aboutMe;

	private VerticalLayout personalDetailContentPanel;
	private Button saveButton;
	private Label errorLabel;
	private Label successLabel;

	// Location Form Related Component
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

	private VerticalLayout nriLocationpanel;

	private VerticalLayout livingLocationpanel;

	private VerticalLayout votingLocationpanel;
	private Label votingSelectorLabel;

	@Autowired
	private MemberService memberService;
	@Autowired
	private VaadinSessionUtil vaadinSessionUtil;
	@Autowired
	private UiComponentsUtil uiComponentsUtil;
	@Autowired
	private LocationService locationService;
	@Autowired
	private AwsProfileImageUtil awsProfileImageUtil;

	private ContextHelp contextHelp;
	private volatile boolean initialized = false;

	public PersonalDetailView() {
	}

	public void init() throws AppException {
		if (!initialized) {
			this.setWidth("100%");
			buildUiScreen();
			addListeners();
			loadUserData();
			initialized = true;
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		try {
			init();
		} catch (Exception ex) {
			uiComponentsUtil.setLabelError(errorLabel, ex);
		}

	}

	private void buildUiScreen() {
		personalDetailPanel = new Panel("Personal Details");

		contextHelp = new ContextHelp();
		contextHelp.extend(UI.getCurrent());
		contextHelp.setFollowFocus(true);

		userName = uiComponentsUtil.buildTextField(contextHelp, FontAwesome.USER, "Name", "Enter your email or phone number with which you registered");

		uploadImageButton = uiComponentsUtil.buildButton(ValoTheme.BUTTON_FRIENDLY, FontAwesome.UPLOAD, "Upload");

		gender = uiComponentsUtil.buildComboBox(contextHelp, "Gender", "Select Gender", "Male", " Female", "Others");

		idCardType = uiComponentsUtil.buildComboBox(contextHelp, "ID card Type", "Select Your identity card type", "Ration Card", "Driving License", "Voter Card", "Passport");
		idCardNumber = uiComponentsUtil.buildTextField(contextHelp, FontAwesome.CREDIT_CARD, "ID Card Number",
				"Please enter ID card number. i.e. if yopu have selected Passport in Card Type field, then enter Passport number");

		dateOfBirth = uiComponentsUtil.buildPopupDateField(contextHelp, "Date of Birth", "Enter your Date of Birth");

		fatherName = uiComponentsUtil.buildTextField(contextHelp, FontAwesome.MALE, "Father Name", "Please enter your father name");
		motherName = uiComponentsUtil.buildTextField(contextHelp, FontAwesome.FEMALE, "Mother Name", "Please enter your mother name");

		aboutMe = new TextArea();
		aboutMe.setValue("The quick brown fox jumps over the lazy dog.");
		aboutMe.setRows(10);
		aboutMe.setImmediate(true);
		aboutMe.setSizeFull();
		aboutMe.setMaxLength(250);
		aboutMe.setWidth("370px");
		aboutMe.setCaption("About me");
        
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

		personalDetailContentPanel = new VerticalLayout(imageUploadLayout, new HorizontalLayout(userName, gender), dateOfBirth, idCardLayout, new HorizontalLayout(fatherName, motherName), aboutMe);
		// personalDetailContentPanel.addStyleName(ValoTheme.LAYOUT_CARD);
		// personalDetailContentPanel.addStyleName("round-corner");
		personalDetailContentPanel.addStyleName("spacious");

		personalDetailContentPanel.setSizeFull();
		personalDetailPanel.setContent(personalDetailContentPanel);

		// Create Location Panel
		nri = new CheckBox("NRI");
		nri.setId("nri");

		try {
			country = uiComponentsUtil.buildCountryComboBox(contextHelp, null, "Country", "Select Country where you live currently. Other then India");
			country.setVisible(true);
			countryRegion = uiComponentsUtil.buildComboBox(contextHelp, "Country Region", "Select Country Region where you live currently", "England", "Wales");
			countryRegionArea = uiComponentsUtil.buildComboBox(contextHelp, "Country Region Area", "Select Country Region Area where you live currently", "London");

			livingState = uiComponentsUtil.buildStateComboBox(contextHelp, null, "Living State", "Select state");
			livingDistrict = uiComponentsUtil.buildComboBox(contextHelp, "Living District", "Select District");
			livingPc = uiComponentsUtil.buildComboBox(contextHelp, "Living Parliament Constituency", "Select Parliament Constituency");
			livingAc = uiComponentsUtil.buildComboBox(contextHelp, "Living Assembly Constituency", "Select Assembly Constituency");

			votingState = uiComponentsUtil.buildStateComboBox(contextHelp, null, "Voting State", "Select State");
			votingDistrict = uiComponentsUtil.buildComboBox(contextHelp, "Voting District", "Select District");
			votingPc = uiComponentsUtil.buildComboBox(contextHelp, "Voting Parliament Constituency", "Select Parliament Constituency");
			votingAc = uiComponentsUtil.buildComboBox(contextHelp, "Voting Assembly Constituency", "Select Assembly Constituency");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Label nriCountrySelectorLabel = new Label("Currently Living - NRI location");
		nriCountrySelectorLabel.setStyleName(ValoTheme.LABEL_H3);

		nriLocationpanel = new VerticalLayout(nriCountrySelectorLabel, new HorizontalLayout(country, countryRegion), countryRegionArea);
		nriLocationpanel.addStyleName(ValoTheme.LAYOUT_CARD);
		nriLocationpanel.addStyleName("round-corner");
		nriLocationpanel.addStyleName("spacious");

		Label livingSelectorLabel = new Label("Currently Living Location(India)");
		livingSelectorLabel.setStyleName(ValoTheme.LABEL_H3);
		livingLocationpanel = new VerticalLayout(livingSelectorLabel,  new HorizontalLayout(livingState, livingDistrict),  new HorizontalLayout(livingPc, livingAc));
		livingLocationpanel.addStyleName(ValoTheme.LAYOUT_CARD);
		livingLocationpanel.addStyleName("round-corner");
		livingLocationpanel.addStyleName("spacious");

		votingSelectorLabel = new Label("Location Where You Registerd as Voter or native location(India)");
		votingSelectorLabel.setStyleName(ValoTheme.LABEL_H3);
		votingLocationpanel = new VerticalLayout(votingSelectorLabel,  new HorizontalLayout(votingState, votingDistrict),  new HorizontalLayout(votingPc, votingAc));
		votingLocationpanel.addStyleName(ValoTheme.LAYOUT_CARD);
		votingLocationpanel.addStyleName("round-corner");
		votingLocationpanel.addStyleName("spacious");

		locationContentPanel = new VerticalLayout(nri, nriLocationpanel, livingLocationpanel, votingLocationpanel);
		// locationContentPanel.addStyleName(ValoTheme.LAYOUT_CARD);
		// locationContentPanel.addStyleName("round-corner");
		locationContentPanel.addStyleName("spacious");
		locationContentPanel.setWidth("500px");

		locationPanel = new Panel("Location");
		locationPanel.setContent(locationContentPanel);

		HorizontalLayout horizontalLayout = new HorizontalLayout(personalDetailPanel, locationPanel);

		this.addComponent(horizontalLayout);
		this.addComponent(successLabel);
		this.addComponent(errorLabel);
		this.addComponent(saveButton);
	}

	private void toggleLocations(boolean nri) {
		if (nri) {
			nriLocationpanel.setVisible(true);
			votingSelectorLabel.setValue("Location Where you Lived in India/Registered as Voter/Home Location");

			livingLocationpanel.setVisible(false);
		} else {
			nriLocationpanel.setVisible(false);

			votingSelectorLabel.setValue("Location Where You Registerd as Voter or native Location(India)");

			livingLocationpanel.setVisible(true);

		}

	}

	private void loadUserData() throws AppException {

		User loggedInUser = vaadinSessionUtil.getLoggedInUserFromSession();

		User dbUser = memberService.getUserById(loggedInUser.getId());
		loadUserPersonalData(dbUser);
		loadUserLocationData(dbUser);
		toggleLocations(dbUser.isNri());
	}

	private void loadUserPersonalData(User dbUser) {
		uiComponentsUtil.setTextFieldValue(userName, dbUser.getName());
		gender.setValue(dbUser.getGender());
		idCardType.setValue(dbUser.getIdentityType());
		uiComponentsUtil.setTextFieldValue(idCardNumber, dbUser.getIdentityNumber());
		dateOfBirth.setValue(dbUser.getDateOfBirth());
		if (dbUser.getFatherName() == null) {

		}
		uiComponentsUtil.setTextFieldValue(fatherName, dbUser.getFatherName());
		uiComponentsUtil.setTextFieldValue(motherName, dbUser.getMotherName());
		uiComponentsUtil.setTextAreaValue(aboutMe, dbUser.getProfile());
		if (dbUser.getProfilePic() != null) {
			userImage.setSource(new ExternalResource("https://static.swarajabhiyan.org/" + dbUser.getProfilePic()));
		}
		nri.setValue(dbUser.isNri());
	}

	private void loadUserLocationData(User dbUser) {
		try {
			List<UserLocation> userLocations = memberService.getUserLocations(dbUser.getId());
			if (dbUser.isNri()) {
				loadUserLocation(country, userLocations, "Living", "Country");
				loadUserLocation(countryRegion, userLocations, "Living", "CountryRegion");
				loadUserLocation(countryRegionArea, userLocations, "Living", "CountryRegionArea");
				loadUserLocation(votingState, userLocations, "Voting", "State");
				loadUserLocation(votingDistrict, userLocations, "Voting", "District");
				loadUserLocation(votingPc, userLocations, "Voting", "ParliamentConstituency");
				loadUserLocation(votingAc, userLocations, "Voting", "AssemblyConstituency");
			} else {
				loadUserLocation(livingState, userLocations, "Living", "State");
				loadUserLocation(livingDistrict, userLocations, "Living", "District");
				loadUserLocation(livingPc, userLocations, "Living", "ParliamentConstituency");
				loadUserLocation(livingAc, userLocations, "Living", "AssemblyConstituency");
				loadUserLocation(votingState, userLocations, "Voting", "State");
				loadUserLocation(votingDistrict, userLocations, "Voting", "District");
				loadUserLocation(votingPc, userLocations, "Voting", "ParliamentConstituency");
				loadUserLocation(votingAc, userLocations, "Voting", "AssemblyConstituency");
			}
		} catch (AppException e) {
			e.printStackTrace();
		}
	}

	private void loadUserLocation(ComboBox comboBox, List<UserLocation> userLocations, String userLocationType, String locationType) {
		for (UserLocation oneUserLocation : userLocations) {
			if (oneUserLocation.getUserLocationType().equalsIgnoreCase(userLocationType) && oneUserLocation.getLocation().getLocationType().getName().equals(locationType)) {
				comboBox.setValue(oneUserLocation.getLocation());
				break;
			}
		}
	}

	private void addListeners() {
		uploadImageButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					successLabel.setVisible(false);
					errorLabel.setVisible(false);
					User loggedInUser = vaadinSessionUtil.getLoggedInUserFromSession();

					awsProfileImageUtil.uploadImage((File) uploadField.getValue(), loggedInUser);
					vaadinSessionUtil.setLoggedInUserinSession(loggedInUser);
					uiComponentsUtil.setLabelMessage(successLabel, "Profile Pic uploaded Succesfully");
					// userImage.setSource(new
					// ExternalResource("https://static.swarajabhiyan.org/"+loggedInUser.getProfilePic()));

				} catch (Exception ex) {
					System.out.println("Unable to upload image for " + vaadinSessionUtil.getLoggedInUserFromSession());
					ex.printStackTrace();
					uiComponentsUtil.setLabelError(errorLabel, ex);
				}
			}
		});
		uploadField.addListener(new Listener() {
			@Override
			public void componentEvent(Event event) {
				showUploadedImage();
			}
		});
		saveButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				successLabel.setVisible(false);
				errorLabel.setVisible(false);
				try {
					User loggedInUser = vaadinSessionUtil.getLoggedInUserFromSession();
					loggedInUser = memberService.updateUserPersonalDetails(loggedInUser.getId(), userName.getValue(), String.valueOf(gender.getValue()), dateOfBirth.getValue(), String.valueOf(idCardType.getValue()),
							idCardNumber.getValue(), fatherName.getValue(), motherName.getValue(), aboutMe.getValue());
					if (nri.getValue()) {
						memberService.updateNriUserLocations(loggedInUser.getId(), getLocationId(country), getLocationId(countryRegion), getLocationId(countryRegionArea), getLocationId(votingState),
								getLocationId(votingDistrict), getLocationId(votingPc), getLocationId(votingAc));
					} else {
						memberService.updateUserLocations(loggedInUser.getId(), getLocationId(livingState), getLocationId(livingDistrict), getLocationId(livingPc), getLocationId(livingAc), getLocationId(votingState),
								getLocationId(votingDistrict), getLocationId(votingPc), getLocationId(votingAc));
					}
					vaadinSessionUtil.setLoggedInUserinSession(loggedInUser);
					successLabel.setVisible(true);
					successLabel.setValue("personal Details updated successfully");
				} catch (AppException ex) {
					ex.printStackTrace();
					uiComponentsUtil.setLabelError(errorLabel, ex);
				} catch (Exception ex) {
					System.out.println("Error occured for " + vaadinSessionUtil.getLoggedInUserFromSession());
					ex.printStackTrace();
					uiComponentsUtil.setLabelMessage(errorLabel, "Internal Error occured. Please contact IT Team.");
				}
			}
		});
		nri.addValueChangeListener(new Property.ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				toggleLocations(nri.getValue());
			}
		});
		livingState.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				try {
					onStateChange(livingState, livingDistrict, livingPc, livingAc);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		livingDistrict.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				try {
					onDistrictChange(livingDistrict, livingAc);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		votingState.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				try {
					onStateChange(votingState, votingDistrict, votingPc, votingAc);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		votingDistrict.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				try {
					onDistrictChange(votingDistrict, votingAc);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		country.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				try {
					Location selectedCountry = (Location) country.getValue();
					if (selectedCountry != null) {
						List<Location> countryRegions = locationService.getAllChildLocations(selectedCountry.getId());
						uiComponentsUtil.loadLocationsToComboBox(countryRegion, countryRegions);
						uiComponentsUtil.loadLocationsToComboBox(countryRegionArea, Collections.emptyList());
					} else {
						uiComponentsUtil.loadLocationsToComboBox(countryRegion, Collections.emptyList());
						uiComponentsUtil.loadLocationsToComboBox(countryRegionArea, Collections.emptyList());
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		countryRegion.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				try {
					Location selectedCountryRegion = (Location) countryRegion.getValue();
					if (selectedCountryRegion != null) {
						List<Location> countryRegionAreas = locationService.getAllChildLocations(selectedCountryRegion.getId());
						uiComponentsUtil.loadLocationsToComboBox(countryRegionArea, countryRegionAreas);
					} else {
						uiComponentsUtil.loadLocationsToComboBox(countryRegionArea, Collections.emptyList());
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	}

	private void onStateChange(ComboBox stateComboBox, ComboBox districtComboBox, ComboBox pcComboBox, ComboBox acComboBox) throws AppException {
		Location selectedState = (Location) stateComboBox.getValue();
		if (selectedState != null) {
			List<Location> districts = locationService.getAllDistrictOfState(selectedState.getId());
			List<Location> pcs = locationService.getAllParliamentConstituenciesOfState(selectedState.getId());
			uiComponentsUtil.loadLocationsToComboBox(districtComboBox, districts);
			uiComponentsUtil.loadLocationsToComboBox(pcComboBox, pcs);
			uiComponentsUtil.loadLocationsToComboBox(acComboBox, Collections.emptyList());
		} else {
			uiComponentsUtil.loadLocationsToComboBox(districtComboBox, Collections.emptyList());
			uiComponentsUtil.loadLocationsToComboBox(pcComboBox, Collections.emptyList());
			uiComponentsUtil.loadLocationsToComboBox(acComboBox, Collections.emptyList());
		}
	}

	private void onDistrictChange(ComboBox districtComboBox, ComboBox acComboBox) throws AppException {
		Location selectedDistrict = (Location) districtComboBox.getValue();
		if (selectedDistrict != null) {
			List<Location> acs = locationService.getAllAssemblyConstituenciesOfDistrict(selectedDistrict.getId());
			uiComponentsUtil.loadLocationsToComboBox(acComboBox, acs);
		} else {
			uiComponentsUtil.loadLocationsToComboBox(acComboBox, Collections.emptyList());
		}
	}

	private Long getLocationId(ComboBox locationComboBox) {
		Location location = (Location) locationComboBox.getValue();
		if (location == null) {
			return null;
		}
		return location.getId();

	}

	private void showUploadedImage() {
		try {
			File file = (File) uploadField.getValue();
			// FileResource resource = new FileResource(file);
			StreamSource streamSource = new StreamSource() {
				private static final long serialVersionUID = 1L;
				FileInputStream fileInputStream = new FileInputStream(file);

				@Override
				public InputStream getStream() {
					return fileInputStream;
				}
			};
			StreamResource streamResource = new StreamResource(streamSource, UUID.randomUUID().toString() + ".jpg");
			userImage.setSource(streamResource);
			System.out.println("File Name : " + streamResource.getFilename());

			userImage.markAsDirty();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public String getNaviagationName() {
		return PersonalDetailView.NAVIAGATION_NAME;
	}

}
