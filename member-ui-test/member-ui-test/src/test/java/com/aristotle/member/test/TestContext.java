package com.aristotle.member.test;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestContext {

	private static TestContext instance;
	private WebDriver webDriver;
	private Map<String, Object> data;
	private TestContext(){
		data = new HashMap<String, Object>();
	}
	public static void startTest(){
		instance = new TestContext();
		WebDriver webDriver = new FirefoxDriver();
		instance.setWebDriver(webDriver);
	}
	public static void finishTest(){
		instance.webDriver.close();
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
