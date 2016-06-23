package com.aristotle.member.test.stepdef;

public class FieldIds {

	public static interface RegistrationPage{
		public static final String NAME_TEXTBOX_FIELD = "name";
		public static final String NRI_CHECKBOX_FIELD = "nri";
		public static final String EMAIL_TEXTBOX_FIELD = "email";
		public static final String PASSWORD_TEXTBOX_FIELD = "password";
		public static final String CONFIRM_PASSWORD_TEXTBOX_FIELD = "confirm_password";
		public static final String MOBILE_NUMBER_TEXTBOX_FIELD = "mobile_number";
		public static final String REGISTRATION_BUTTON_FIELD = "register_button";
		public static final String ALREADY_REGISTRED_BUTTON_FIELD = "login_button";

		public static final String COUNTRY_COMOBOBOX_FIELD = "country_code";
		public static final String ERROR_LABEL_FIELD = "error_label";
		public static final String SUCCESS_LABEL_FIELD = "success_label";
	}
	public static interface LoginPage{
		public static final String LOGIN_NAME_TEXTBOX_FIELD = "email_mobile_number";
		public static final String PASSWORD_TEXTBOX_FIELD = "password";
		public static final String REGISTRATION_BUTTON_FIELD = "register_button";
		public static final String LOGIN_BUTTON_FIELD = "login_button";

		public static final String COUNTRY_COMOBOBOX_FIELD = "country_code";
		public static final String ERROR_LABEL_FIELD = "error_label";
		public static final String SUCCESS_LABEL_FIELD = "success_label";
	}
	
	public static interface SecurityPage{
		public static final String CURRENT_PASSWORD_TEXTBOX_FIELD = "current_password";
		public static final String PASSWORD_TEXTBOX_FIELD = "password";
		public static final String CONFIRM_PASSWORD_TEXTBOX_FIELD = "confirm_password";
		public static final String CHANGE_PASSWORD_BUTTON_FIELD = "change_password_button";

		public static final String ERROR_LABEL_FIELD = "error_label";
		public static final String SUCCESS_LABEL_FIELD = "success_label";
	}
}
