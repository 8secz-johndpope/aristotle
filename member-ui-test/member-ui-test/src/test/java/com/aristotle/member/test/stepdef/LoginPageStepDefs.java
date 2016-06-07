package com.aristotle.member.test.stepdef;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.aristotle.member.test.exception.FieldDoNotExistsException;

import cucumber.api.java.en.Given;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginPageStepDefs extends BaseStepDef{
	
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
    	selectComboBoxValue(FieldIds.RegistrationPage.COUNTRY_COMOBOBOX_FIELD, value);
    }
    
   
    
    
    
}