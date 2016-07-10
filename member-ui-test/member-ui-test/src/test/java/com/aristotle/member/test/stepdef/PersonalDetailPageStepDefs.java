package com.aristotle.member.test.stepdef;

import org.openqa.selenium.WebDriver;

import com.aristotle.member.test.TestContext;
import com.aristotle.member.test.exception.FieldDoNotExistsException;

import cucumber.api.java.en.Given;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersonalDetailPageStepDefs extends BaseStepDef{
	
	@Given("Refresh Personal Detail form")
    public void clearLoginForm() throws FieldDoNotExistsException {
		WebDriver webDriver = TestContext.getCurrentContext().getWebDriver();
		webDriver.navigate().refresh();
    }

    @Given("Enter Personal Detail User Name \"([^\"]*)\"")
    public void enterUserName(String nameValue) throws FieldDoNotExistsException {
    	enterTextFieldValue(FieldIds.PersonalDetailPage.NAME_TEXTBOX_FIELD, nameValue);
    }
    @Given("Enter Personal Detail Date of Birth \"([^\"]*)\"")
    public void enterDateOfBirth(String nameValue) throws FieldDoNotExistsException {
    	enterDateFieldValue(FieldIds.PersonalDetailPage.DATE_OF_BIRTH_DATEBOX_FIELD, nameValue);
    }
    @Given("Select Personal Detail Gender \"([^\"]*)\"")
    public void selectGender(String value) throws FieldDoNotExistsException {
    	selectComboBoxValue(FieldIds.PersonalDetailPage.GENDER_COMBOBOX_FIELD, value);
    }
    @Given("Select Personal Detail ID Card Type \"([^\"]*)\"")
    public void selectIdCardType(String value) throws FieldDoNotExistsException {
    	selectComboBoxValue(FieldIds.PersonalDetailPage.ID_CARD_TYPE_COMBOBOX_FIELD, value);
    }
    @Given("Enter Personal Detail ID Card Number \"([^\"]*)\"")
    public void enterIdCardNumber(String value) throws FieldDoNotExistsException {
    	enterTextFieldValue(FieldIds.PersonalDetailPage.ID_CARD_NUMBER_COMBOBOX_FIELD, value);
    }
    @Given("Enter Personal Detail Father Name \"([^\"]*)\"")
    public void enterFatherName(String value) throws FieldDoNotExistsException {
    	enterTextFieldValue(FieldIds.PersonalDetailPage.FATHER_NAME_TEXTBOX_FIELD, value);
    }
    @Given("Enter Personal Detail Mother Name \"([^\"]*)\"")
    public void enterMotherName(String value) throws FieldDoNotExistsException {
    	enterTextFieldValue(FieldIds.PersonalDetailPage.MOTHER_NAME_TEXTBOX_FIELD, value);
    }
    
    @Given("Select Personal Detail Country \"([^\"]*)\"")
    public void selectCountry(String value) throws FieldDoNotExistsException {
    	selectComboBoxValue(FieldIds.PersonalDetailPage.COUNTRY_COMOBOBOX_FIELD, value);
    }
    @Given("Select Personal Detail Country Region \"([^\"]*)\"")
    public void selectCountryRegion(String value) throws FieldDoNotExistsException {
    	selectComboBoxValue(FieldIds.PersonalDetailPage.COUNTRY_REGION_COMOBOBOX_FIELD, value);
    }
    
    @Given("Select Personal Detail Country Region Area \"([^\"]*)\"")
    public void selectCountryRegionArea(String value) throws FieldDoNotExistsException {
    	selectComboBoxValue(FieldIds.PersonalDetailPage.COUNTRY_REGION_AREA_COMOBOBOX_FIELD, value);
    }
    
    @Given("Select Personal Detail Voting State \"([^\"]*)\"")
    public void selectVotingState(String value) throws FieldDoNotExistsException {
    	selectComboBoxValue(FieldIds.PersonalDetailPage.VOTING_STATE_COMOBOBOX_FIELD, value);
    }
    @Given("Select Personal Detail Voting District \"([^\"]*)\"")
    public void selectVotingDistrict(String value) throws FieldDoNotExistsException {
    	selectComboBoxValue(FieldIds.PersonalDetailPage.VOTING_DISTRICT_COMOBOBOX_FIELD, value);
    }
    @Given("Select Personal Detail Voting PC \"([^\"]*)\"")
    public void selectVotingPc(String value) throws FieldDoNotExistsException {
    	selectComboBoxValue(FieldIds.PersonalDetailPage.VOTING_PC_COMOBOBOX_FIELD, value);
    }
    @Given("Select Personal Detail Voting AC \"([^\"]*)\"")
    public void selectVotingAc(String value) throws FieldDoNotExistsException {
    	selectComboBoxValue(FieldIds.PersonalDetailPage.VOTING_AC_COMOBOBOX_FIELD, value);
    }
    
    @Given("Select Personal Detail Living State \"([^\"]*)\"")
    public void selectLivingState(String value) throws FieldDoNotExistsException {
    	selectComboBoxValue(FieldIds.PersonalDetailPage.LIVING_STATE_COMOBOBOX_FIELD, value);
    }
    @Given("Select Personal Detail Living District \"([^\"]*)\"")
    public void selectLivingDistrict(String value) throws FieldDoNotExistsException {
    	selectComboBoxValue(FieldIds.PersonalDetailPage.LIVING_DISTRICT_COMOBOBOX_FIELD, value);
    }
    @Given("Select Personal Detail Living PC \"([^\"]*)\"")
    public void selectLivingPc(String value) throws FieldDoNotExistsException {
    	selectComboBoxValue(FieldIds.PersonalDetailPage.LIVING_PC_COMOBOBOX_FIELD, value);
    }
    @Given("Select Personal Detail Living AC \"([^\"]*)\"")
    public void selectLivingAc(String value) throws FieldDoNotExistsException {
    	selectComboBoxValue(FieldIds.PersonalDetailPage.LIVING_AC_COMOBOBOX_FIELD, value);
    }
    @Given("Click on Personal Detail Save Button")
    public void clickOnAlreadyRegistredButton() throws FieldDoNotExistsException {
    	clickOnButton(FieldIds.PersonalDetailPage.SAVE_BUTTON_FIELD);
    }
    
    
    
}