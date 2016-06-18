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
	private static boolean isFirefox(){
		try{
			String firefox = (String)System.getProperties().get("firefox");
			if(firefox == null){
				firefox = System.getenv("firefox");
			}
			if(firefox == null){
				return false;
			}
			return Boolean.parseBoolean(firefox);
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
		
	}
	public static void startTest(){
		instance = new TestContext();
		boolean firefox = isFirefox();
		
		WebDriver webDriver;
		if(firefox){
			webDriver = new FirefoxDriver();
		}else{
			String phantomJsPath = (String)System.getProperties().get("phantomjsBinaryPath");
			if(phantomJsPath == null){
				phantomJsPath = System.getenv("phantomjsBinaryPath");
			}
			if(phantomJsPath == null){
				throw new RuntimeException("phantomjsBinaryPath is null.Test wont run");
			}
			System.out.println("phantomJsPath = "+ phantomJsPath);
			System.getProperties().put("phantomjs.binary.path", phantomJsPath);

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
