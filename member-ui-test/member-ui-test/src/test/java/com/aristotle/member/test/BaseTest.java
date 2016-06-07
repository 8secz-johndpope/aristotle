package com.aristotle.member.test;

import cucumber.api.java.After;
import cucumber.api.java.Before;

public class BaseTest {

	
	@Before
	public void beforeScenario() {
		TestContext.startTest();
	}

	@After
	public void afterScenario() {
		TestContext.finishTest();
	}
}
