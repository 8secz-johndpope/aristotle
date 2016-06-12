package com.aristotle.member.test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;

public class BaseTest {

	private static int screenShotCount = 1;

	private Scenario scenario;
	@Before
	public void before(Scenario scenario) {
	    this.scenario = scenario;
	}
	
	@Before
	public void beforeScenario() {
		TestContext.startTest();
	}

	@After
	public void afterScenario() throws IOException {
		try{
			takeScreenShotWithSecnarioName();
		}finally{
			TestContext.finishTest();
		}
		
	}
	private void takeScreenShotWithSecnarioName() throws IOException{
		String ssName = scenario.getName();
		ssName = ssName.replaceAll(" ", "_");
		ssName = ssName.replaceAll("\\(", "_");
		ssName = ssName.replaceAll("\\)", "_");
		ssName = ssName.replaceAll("\\.", "_");
		ssName = ssName + ".png";

		takeScreenShot(ssName);
	}
    @Given("Take Screen shot as \"([^\"]*)\"")
    public void takeScreenShot(String fileName) throws IOException {
    	WebDriver webDriver = TestContext.getCurrentContext().getWebDriver();
    	TakesScreenshot screenshotdriver = (TakesScreenshot)webDriver;
        File srcFile = screenshotdriver.getScreenshotAs(OutputType.FILE);
        String count = String.valueOf(screenShotCount);
        if(screenShotCount < 10){
        	count = "0000"+screenShotCount;
        }else if(screenShotCount < 100){
        	count = "000"+screenShotCount;
        }else if(screenShotCount < 1000){
        	count = "00"+screenShotCount;
        }else if(screenShotCount < 10000){
        	count = "0"+screenShotCount;
        }
        if(scenario.isFailed()){
        	FileUtils.copyFile(srcFile, new File("/tmp/ss/failed/"+count+fileName));
        }else{
        	FileUtils.copyFile(srcFile, new File("/tmp/ss/"+count+fileName));
        }
        
        screenShotCount++;
    }
}
