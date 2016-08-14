package com.aristotle.member.ui.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.User;
import com.aristotle.member.service.MemberService;

@Component
public class LoginHelper {

	@Autowired
	private MemberService memberService;
	@Autowired
	private VaadinSessionUtil vaadinSessionUtil;
	
	public User login(String userName, String password) throws AppException{
		User user = memberService.login(userName, password);
    	vaadinSessionUtil.setLoggedInUserinSession(user);
    	return user;
	}
}
