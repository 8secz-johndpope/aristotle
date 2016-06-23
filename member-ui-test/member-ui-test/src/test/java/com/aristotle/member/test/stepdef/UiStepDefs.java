package com.aristotle.member.test.stepdef;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.springframework.beans.factory.annotation.Autowired;

import com.aristotle.core.service.UserService;
import com.aristotle.member.test.TestContext;
import com.aristotle.member.test.Urls;
import com.aristotle.member.test.exception.FieldDoNotExistsException;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UiStepDefs extends BaseStepDef{
	
	@Autowired
	private UserService userService;
	
    @Given("Open Registration page")
    public void open_registeration_page() throws IOException {
    	WebDriver webDriver = TestContext.getCurrentContext().getWebDriver();
    	openUrl(webDriver, Urls.REGISTER_URL);
    }
    @Given("Open Login page")
    public void open_login_page() throws IOException {
    	WebDriver webDriver = TestContext.getCurrentContext().getWebDriver();
    	openUrl(webDriver, Urls.LOGIN_URL);
    }
    @Given("Open Security page")
    public void open_security_page() throws IOException {
    	WebDriver webDriver = TestContext.getCurrentContext().getWebDriver();
    	openUrl(webDriver, Urls.SECURITY_URL);
    }
    @Given("Check Field exists \"([^\"]*)\"")
	public void checkFieldExists(String fieldId) throws FieldDoNotExistsException {
		WebDriver webDriver = TestContext.getCurrentContext().getWebDriver();
		getElement(webDriver, By.id(fieldId));
	}
    @Given("Check Field \"([^\"]*)\" is empty")
	public void checkFieldExistsWithEmptyValue(String fieldId) throws FieldDoNotExistsException {
		WebDriver webDriver = TestContext.getCurrentContext().getWebDriver();
		WebElement webElement = getElement(webDriver, By.id(fieldId));
		Assert.assertEquals("", webElement.getText());
	}
    @Given("Check Field \"([^\"]*)\" is enabled and visible")
	public void checkFieldisEnabled(String fieldId) throws FieldDoNotExistsException {
		WebDriver webDriver = TestContext.getCurrentContext().getWebDriver();
		WebElement webElement = getElement(webDriver, By.id(fieldId));
		System.out.println("ID : "+ fieldId);
		if("register_button".equals(fieldId)){
			System.out.println("Yes foind it");
		}
		Assert.assertTrue(webElement.isEnabled());
		Assert.assertTrue(webElement.isDisplayed());
	}
    @Given("Check Checkbox Field \"([^\"]*)\" is enabled and visible")
	public void checkCheckboxFieldisEnabled(String fieldId) throws FieldDoNotExistsException {
		WebDriver webDriver = TestContext.getCurrentContext().getWebDriver();
		WebElement webElement = getElement(webDriver, By.id(fieldId));
		//Vaadin generate actual Input field with ID input inside checkbox id div
		webElement = webElement.findElement(By.tagName("input"));
		Assert.assertTrue(webElement.isEnabled());
		Assert.assertTrue(webElement.isDisplayed());
	}
    @Given("Check Field \"([^\"]*)\" is not checked")
	public void checkFieldExistsWithuncheckedValue(String fieldId) throws FieldDoNotExistsException {
		WebDriver webDriver = TestContext.getCurrentContext().getWebDriver();
		WebElement webElement = getElement(webDriver, By.id(fieldId));
		//Vaadin generate actual Input field with ID input inside checkbox id div
		webElement = webElement.findElement(By.tagName("input"));
		Assert.assertFalse(webElement.isSelected());
	}
    
    @Given("Check Field \"([^\"]*)\" is checked")
	public void checkFieldExistsWithcheckedValue(String fieldId) throws FieldDoNotExistsException {
		WebDriver webDriver = TestContext.getCurrentContext().getWebDriver();
		WebElement webElement = getElement(webDriver, By.id(fieldId));
		//Vaadin generate actual Input field with ID input inside checkbox id div
		webElement = webElement.findElement(By.tagName("input"));
		Assert.assertTrue(webElement.isSelected());
	}
    
    @Given("Wait for field \"([^\"]*)\" to appear")
	public void waitForField(String fieldId) throws FieldDoNotExistsException {
		WebDriver webDriver = TestContext.getCurrentContext().getWebDriver();
		getElement(webDriver, By.id(fieldId));
	}
    @Given("Check that login page has been opened")
	public void waitForLoginPage() throws FieldDoNotExistsException {
		WebDriver webDriver = TestContext.getCurrentContext().getWebDriver();
		webDriver.getCurrentUrl().contains(Urls.LOGIN_URL);
	}
    @Given("Check that home page has been opened")
	public void waitForHomePage() throws FieldDoNotExistsException {
		WebDriver webDriver = TestContext.getCurrentContext().getWebDriver();
		webDriver.getCurrentUrl().contains(Urls.HOME_URL);
	}
    
    @Given("Check For Error \"([^\"]*)\" to appear")
	public void checkForError(String error) throws FieldDoNotExistsException {
		WebDriver webDriver = TestContext.getCurrentContext().getWebDriver();
		WebElement errorLabelWebElement = getElement(webDriver, By.id(FieldIds.RegistrationPage.ERROR_LABEL_FIELD));
		Assert.assertEquals(error, errorLabelWebElement.getText());
	}
    
	@Given("Check Field do not exists \"([^\"]*)\"")
	public void checkFieldDoNotExists(String fieldId) {
		WebDriver webDriver = TestContext.getCurrentContext().getWebDriver();
		WebElement giveWebElement = null;
		try{
			giveWebElement = getElement(webDriver, By.id(fieldId), 1);	
		}catch(FieldDoNotExistsException ex){
			//Its fine
		}
		if(giveWebElement != null){
			throw new RuntimeException("Field ["+fieldId+"] exists");
		}
		
	}
    
    
    
    
}