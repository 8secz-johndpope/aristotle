package com.aristotle.member.test.stepdef;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.aristotle.member.test.TestContext;
import com.aristotle.member.test.exception.FieldDoNotExistsException;

import cucumber.api.java.en.Given;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RegisterPageStepDefs extends BaseStepDef{
	
	@Given("Clear Registeration form")
    public void clearLoginForm() throws FieldDoNotExistsException {
		WebDriver webDriver = TestContext.getCurrentContext().getWebDriver();
		webDriver.navigate().refresh();
    }

	@Given("Register new User as \"([^\"]*)\"")
    public void enterUserName(String userIdentifier, List<Map<String, String>> userDetails) throws FieldDoNotExistsException {
		for(Map<String, String> oneUserDetail : userDetails){
			enterUserName(oneUserDetail.get(FieldIds.RegistrationPage.NAME_TEXTBOX_FIELD));
			enterRegistrationEmail(oneUserDetail.get(FieldIds.RegistrationPage.EMAIL_TEXTBOX_FIELD));
			enterRegistrationPassword(oneUserDetail.get(FieldIds.RegistrationPage.PASSWORD_TEXTBOX_FIELD));
			enterRegistrationConfirmPassword(oneUserDetail.get(FieldIds.RegistrationPage.CONFIRM_PASSWORD_TEXTBOX_FIELD));
			enterRegistrationMobileNumber(oneUserDetail.get(FieldIds.RegistrationPage.MOBILE_NUMBER_TEXTBOX_FIELD));
			setCheckBoxFieldValue(FieldIds.RegistrationPage.NRI_CHECKBOX_FIELD, oneUserDetail.get(FieldIds.RegistrationPage.NRI_CHECKBOX_FIELD));
			selectRegistrationCountry(oneUserDetail.get(FieldIds.RegistrationPage.COUNTRY_COMOBOBOX_FIELD));
		}
		clickOnRegistrationButton();
    }
	
    @Given("Enter Registeration Name \"([^\"]*)\"")
    public void enterUserName(String nameValue) throws FieldDoNotExistsException {
    	enterTextFieldValue(FieldIds.RegistrationPage.NAME_TEXTBOX_FIELD, nameValue);
    }
    
    @Given("Select Registeration NRI checkbox")
    public void selectNriCheckbox() throws FieldDoNotExistsException {
    	selectCheckBoxTextFieldValue(FieldIds.RegistrationPage.NRI_CHECKBOX_FIELD);
    }
    
    @Given("Enter Registeration Email \"([^\"]*)\"")
    public void enterRegistrationEmail(String nameValue) throws FieldDoNotExistsException {
    	enterTextFieldValue(FieldIds.RegistrationPage.EMAIL_TEXTBOX_FIELD, nameValue);
    }
    @Given("Enter Registeration Password \"([^\"]*)\"")
    public void enterRegistrationPassword(String nameValue) throws FieldDoNotExistsException {
    	enterTextFieldValue(FieldIds.RegistrationPage.PASSWORD_TEXTBOX_FIELD, nameValue);
    }
    @Given("Enter Registeration Confirm Password \"([^\"]*)\"")
    public void enterRegistrationConfirmPassword(String nameValue) throws FieldDoNotExistsException {
    	enterTextFieldValue(FieldIds.RegistrationPage.CONFIRM_PASSWORD_TEXTBOX_FIELD, nameValue);
    }
    @Given("Enter Registeration Mobile Number \"([^\"]*)\"")
    public void enterRegistrationMobileNumber(String nameValue) throws FieldDoNotExistsException {
    	enterTextFieldValue(FieldIds.RegistrationPage.MOBILE_NUMBER_TEXTBOX_FIELD, nameValue);
    }
    @Given("Click on Registration Button")
    public void clickOnRegistrationButton() throws FieldDoNotExistsException {
    	clickOnButton(FieldIds.RegistrationPage.REGISTRATION_BUTTON_FIELD);
    }
    @Given("Click on Already Registerd Button")
    public void clickOnAlreadyRegistredButton() throws FieldDoNotExistsException {
    	clickOnButton(FieldIds.RegistrationPage.ALREADY_REGISTRED_BUTTON_FIELD);
    }
    @Given("Registration Button should be disabled")
    public void registrationButtonShouldBeDisabled() throws FieldDoNotExistsException {
    	WebElement registrationButton = getElement(By.id(FieldIds.RegistrationPage.REGISTRATION_BUTTON_FIELD));
    	Assert.assertFalse(registrationButton.isEnabled());
    }
    @Given("Registration Button should be enabled")
    public void registrationButtonShouldBeEnabled() throws FieldDoNotExistsException {
    	WebElement registrationButton = getElement(By.id(FieldIds.RegistrationPage.REGISTRATION_BUTTON_FIELD));
    	Assert.assertTrue(registrationButton.isEnabled());
    }
    @Given("Select Registration Country \"([^\"]*)\"")
    public void selectRegistrationCountry(String value) throws FieldDoNotExistsException {
    	if(value == null){
    		return;
    	}
    	selectComboBoxValue(FieldIds.RegistrationPage.COUNTRY_COMOBOBOX_FIELD, value);
    }
    
   
    
    
    
}