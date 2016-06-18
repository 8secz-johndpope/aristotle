package com.aristotle.member.test;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class TestContext {

	private static TestContext instance;
	private WebDriver webDriver;
	private Map<String, Object> data;
	private TestContext(){
		data = new HashMap<String, Object>();
	}
	public static void startTest(){
		instance = new TestContext();
		boolean firefox = false;
		
		WebDriver webDriver;
		if(firefox){
			webDriver = new FirefoxDriver();
		}else{
			if(System.getProperties().get("phantomjsBinaryPath") == null ){
				System.getProperties().put("phantomjs.binary.path", "/usr/local/software/phantomJS/current/bin/phantomjs");
			}else{
				System.getProperties().put("phantomjs.binary.path", System.getProperties().get("phantomjsBinaryPath"));
			}
			DesiredCapabilities capabilities = DesiredCapabilities.phantomjs();
			webDriver = new PhantomJSDriver(capabilities);
			webDriver.manage().window().setSize(new Dimension(1600, 1200));
		}
		
		instance.setWebDriver(webDriver);
	}
	public static void finishTest(){
		instance.webDriver.close();
		instance.webDriver.quit();
		instance = null;
	}
	public static TestContext getCurrentContext(){
		return instance;
	}
	
	public void setWebDriver(WebDriver webDriver){
		this.webDriver = webDriver;
	}
	public WebDriver getWebDriver(){
		return this.webDriver;
	}
	public void setData(String name, Object dataValue){
		data.put(name, dataValue);
	}
	public <T> T getData(String key, Class<T> clz){
		return (T)data.get(key);
	}
}
