package com.aristotle.member.test.stepdef;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import com.aristotle.core.App;
import com.aristotle.member.test.TestContext;
import com.aristotle.member.test.exception.FieldDoNotExistsException;

import cucumber.api.java.en.Given;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ContextConfiguration(classes = App.class, loader = SpringApplicationContextLoader.class)
@DirtiesContext
public class BaseStepDef {
	private final int MAX_ATTEMPTS = 5;

	protected void openUrl(WebDriver webDriver, String url) {
		log.info("Opening url : {}", url);
		webDriver.get(url);
	}

	protected void clickOnButton(String buttonId) throws FieldDoNotExistsException {
		log.info("Clicking On Button : {}", buttonId);
    	WebDriver webDriver = TestContext.getCurrentContext().getWebDriver();
    	WebElement buttonWebElement = getElement(webDriver, By.id(buttonId));
    	buttonWebElement.click();
	}
	protected void clearTextFieldValue(String fieldId) throws FieldDoNotExistsException {
    	WebDriver webDriver = TestContext.getCurrentContext().getWebDriver();
    	clearTextFieldValue(webDriver, fieldId);
	}
	protected void clearTextFieldValue(WebDriver webDriver, String fieldId) throws FieldDoNotExistsException {
		log.info("Clearing field : {}", fieldId);
		WebElement textFieldWebElement = getElement(webDriver, By.id(fieldId));
		textFieldWebElement.clear();
	}
	protected void enterTextFieldValue(String fieldId, String value) throws FieldDoNotExistsException {
		log.info("Enetring value: {} into field : {}", value, fieldId);
    	WebDriver webDriver = TestContext.getCurrentContext().getWebDriver();
		enterTextFieldValue(webDriver, fieldId, value);
	}
	protected void enterTextFieldValue(WebDriver webDriver, String fieldId, String value) throws FieldDoNotExistsException {
		log.info("Enetring value: {} into field : {}", value, fieldId);
		WebElement textFieldWebElement = getElement(webDriver, By.id(fieldId));
		textFieldWebElement.sendKeys(value);
	}
	protected void selectCheckBoxTextFieldValue(String fieldId) throws FieldDoNotExistsException {
		log.info("Selecting checbox field : {}", fieldId);
    	WebDriver webDriver = TestContext.getCurrentContext().getWebDriver();
		selectCheckBoxTextFieldValue(webDriver, fieldId);
	}
	protected void selectCheckBoxTextFieldValue(WebDriver webDriver, String fieldId) throws FieldDoNotExistsException {
		log.info("Selecting checbox field : {}", fieldId);
		selectCheckBoxTextFieldValue(webDriver, fieldId, true);
	}
	protected void unselectCheckBoxTextFieldValue(String fieldId) throws FieldDoNotExistsException {
    	WebDriver webDriver = TestContext.getCurrentContext().getWebDriver();
		selectCheckBoxTextFieldValue(webDriver, fieldId);
	}
	protected void unselectCheckBoxTextFieldValue(WebDriver webDriver, String fieldId) throws FieldDoNotExistsException {
		log.info("UnSelecting checbox field : {}", fieldId);
		selectCheckBoxTextFieldValue(webDriver, fieldId, false);
	}
	private void selectCheckBoxTextFieldValue(WebDriver webDriver, String fieldId, boolean value) throws FieldDoNotExistsException {
		WebElement checkBoxFieldWebElement = getElement(webDriver, By.id(fieldId));
		if(value && !checkBoxFieldWebElement.isSelected()){
			checkBoxFieldWebElement.click();
		}
		if(!value && checkBoxFieldWebElement.isSelected()){
			checkBoxFieldWebElement.click();
		}
	}
	
	protected void selectComboBoxValue(String fieldId, String value) throws FieldDoNotExistsException {
    	WebDriver webDriver = TestContext.getCurrentContext().getWebDriver();
    	selectComboBoxValue(webDriver, fieldId, value);
	}
	private void selectComboBoxValue(WebDriver webDriver, String fieldId, String value) throws FieldDoNotExistsException {
		WebElement comboBoxFieldWebElement = getElement(webDriver, By.id(fieldId));
		comboBoxFieldWebElement = comboBoxFieldWebElement.findElement(By.tagName("input"));
		//final Select selectBox = new Select(comboBoxFieldWebElement);
	    //selectBox.selectByValue(value);
		comboBoxFieldWebElement.sendKeys(value);
		comboBoxFieldWebElement.sendKeys(Keys.ENTER);
	}
	
	protected WebElement getElement(By by) throws FieldDoNotExistsException {
		WebDriver driver = TestContext.getCurrentContext().getWebDriver();
		return getElement(driver, by);
	}
	protected WebElement getElement(WebDriver driver, By by) throws FieldDoNotExistsException {
		return getElement(driver, by, MAX_ATTEMPTS);
	}

	protected WebElement getElement(WebDriver driver, By by, int maxAttempt) throws FieldDoNotExistsException {
		int attempts = 0;
		int size = driver.findElements(by).size();

		while (size == 0) {
			size = driver.findElements(by).size();
			if (attempts == maxAttempt) {
				throw new FieldDoNotExistsException(String.format("Could not find %s after %d seconds", by.toString(), maxAttempt));
			}

			attempts++;
			try {
				Thread.sleep(1000); // sleep for 1 second.
			} catch (Exception x) {
				throw new RuntimeException("Failed due to an exception during Thread.sleep!");
			}
		}

		if (size > 1)
			System.err.println("WARN: There are more than 1 " + by.toString() + " 's!");

		return driver.findElement(by);
	}
}
