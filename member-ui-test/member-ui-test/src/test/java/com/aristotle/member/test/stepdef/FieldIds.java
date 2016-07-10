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
	
	public static interface PersonalDetailPage{
		public static final String NAME_TEXTBOX_FIELD = "name";
		public static final String GENDER_COMBOBOX_FIELD = "gender";
		public static final String DATE_OF_BIRTH_DATEBOX_FIELD = "date_of_birth";
		public static final String ID_CARD_TYPE_COMBOBOX_FIELD = "id_card_type";
		public static final String ID_CARD_NUMBER_COMBOBOX_FIELD = "id_card_number";
		public static final String FATHER_NAME_TEXTBOX_FIELD = "father_name";
		public static final String MOTHER_NAME_TEXTBOX_FIELD = "mother_name";

		public static final String COUNTRY_COMOBOBOX_FIELD = "country";
		public static final String COUNTRY_REGION_COMOBOBOX_FIELD = "country_region";
		public static final String COUNTRY_REGION_AREA_COMOBOBOX_FIELD = "country_region_area";
		public static final String VOTING_STATE_COMOBOBOX_FIELD = "voting_state";
		public static final String VOTING_DISTRICT_COMOBOBOX_FIELD = "voting_district";
		public static final String VOTING_PC_COMOBOBOX_FIELD = "voting_parliament_constituency";
		public static final String VOTING_AC_COMOBOBOX_FIELD = "voting_assembly_constituency";
		public static final String LIVING_STATE_COMOBOBOX_FIELD = "living_state";
		public static final String LIVING_DISTRICT_COMOBOBOX_FIELD = "living_district";
		public static final String LIVING_PC_COMOBOBOX_FIELD = "living_parliament_constituency";
		public static final String LIVING_AC_COMOBOBOX_FIELD = "living_assembly_constituency";
		public static final String ERROR_LABEL_FIELD = "error_label";
		public static final String SUCCESS_LABEL_FIELD = "success_label";
		public static final String SAVE_BUTTON_FIELD = "save_button";

	}
}
