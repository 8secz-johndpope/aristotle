package com.aristotle.member.test;

public class Urls {

	public static String BASE_URL = "http://localhost:8100/";
	static{
		String baseUrl = System.getenv("MEMBER_SERVER_BASE_URL");
		if(baseUrl == null){
			baseUrl = System.getProperty("MEMBER_SERVER_BASE_URL");
		}
		if(baseUrl != null){
			BASE_URL = baseUrl;
			System.out.println("BASE_URL = " +BASE_URL);
		}
	}
	public static final String REGISTER_URL = BASE_URL+"#!register";
	public static final String LOGIN_URL = BASE_URL+"#!login";
	public static final String HOME_URL = BASE_URL+"#!home";
	public static final String SECURITY_URL = BASE_URL+"#!security";


}
