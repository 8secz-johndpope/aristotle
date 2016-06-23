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
public class SecurityPageStepDefs extends BaseStepDef{
	
	@Given("Clear Security form")
    public void clearRegistrationForm() throws FieldDoNotExistsException {
		WebDriver webDriver = TestContext.getCurrentContext().getWebDriver();
		webDriver.navigate().refresh();
    }
	
    @Given("Enter Security Current Password \"([^\"]*)\"")
    public void enterCurrentPassword(String nameValue) throws FieldDoNotExistsException {
    	enterTextFieldValue(FieldIds.SecurityPage.CURRENT_PASSWORD_TEXTBOX_FIELD, nameValue);
    }
    
    @Given("Enter Security New Password \"([^\"]*)\"")
    public void enterNewPassword(String nameValue) throws FieldDoNotExistsException {
    	enterTextFieldValue(FieldIds.SecurityPage.PASSWORD_TEXTBOX_FIELD, nameValue);
    }
    @Given("Enter Security New Confirm Password \"([^\"]*)\"")
    public void enterNewConfirmPassword(String nameValue) throws FieldDoNotExistsException {
    	enterTextFieldValue(FieldIds.SecurityPage.CONFIRM_PASSWORD_TEXTBOX_FIELD, nameValue);
    }
    @Given("Click on Security Change Password Button")
    public void clickOnLoginPageRegistrationButton() throws FieldDoNotExistsException {
    	clickOnButton(FieldIds.SecurityPage.CHANGE_PASSWORD_BUTTON_FIELD);
    }
    @Given("Security Change Password Button should be enabled")
    public void registrationButtonShouldBeEnabled() throws FieldDoNotExistsException {
    	WebElement registrationButton = getElement(By.id(FieldIds.SecurityPage.CHANGE_PASSWORD_BUTTON_FIELD));
    	Assert.assertTrue(registrationButton.isEnabled());
    }
}