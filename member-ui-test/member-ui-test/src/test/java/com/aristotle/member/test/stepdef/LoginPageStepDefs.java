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
public class LoginPageStepDefs extends BaseStepDef{
	
	@Given("Clear Login form")
    public void clearRegistrationForm() throws FieldDoNotExistsException {
		WebDriver webDriver = TestContext.getCurrentContext().getWebDriver();
		webDriver.navigate().refresh();
    }
	
	//@Given("Register new User as \"([^\"]*)\"")
    public void enterUserName(String userIdentifier, List<Map<String, String>> userDetails) throws FieldDoNotExistsException {
		for(Map<String, String> oneUserDetail : userDetails){
		}
    }
	
    @Given("Enter Login Email/Mobile Number \"([^\"]*)\"")
    public void enterLoginName(String nameValue) throws FieldDoNotExistsException {
    	enterTextFieldValue(FieldIds.LoginPage.LOGIN_NAME_TEXTBOX_FIELD, nameValue);
    }
    
    @Given("Enter Login Password \"([^\"]*)\"")
    public void enterRegistrationPassword(String nameValue) throws FieldDoNotExistsException {
    	enterTextFieldValue(FieldIds.LoginPage.PASSWORD_TEXTBOX_FIELD, nameValue);
    }
    @Given("Click on LoginPage Registration Button")
    public void clickOnLoginPageRegistrationButton() throws FieldDoNotExistsException {
    	clickOnButton(FieldIds.LoginPage.REGISTRATION_BUTTON_FIELD);
    }
    @Given("Click on LoginPage Login Button")
    public void clickOnAlreadyRegistredButton() throws FieldDoNotExistsException {
    	clickOnButton(FieldIds.LoginPage.LOGIN_BUTTON_FIELD);
    }
    @Given("LoginPage Login Button should be disabled")
    public void registrationButtonShouldBeDisabled() throws FieldDoNotExistsException {
    	WebElement registrationButton = getElement(By.id(FieldIds.LoginPage.LOGIN_BUTTON_FIELD));
    	Assert.assertFalse(registrationButton.isEnabled());
    }
    @Given("Login Button should be enabled")
    public void registrationButtonShouldBeEnabled() throws FieldDoNotExistsException {
    	WebElement registrationButton = getElement(By.id(FieldIds.LoginPage.LOGIN_BUTTON_FIELD));
    	Assert.assertTrue(registrationButton.isEnabled());
    }
}