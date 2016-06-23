package com.aristotle.member.service;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.User;

public interface MemberService {
	User login(String userName, String password) throws AppException;
	
	User register(String userName, String password, String passwordConfirm, String email, String countryCode, String mobileNumber, boolean nri) throws AppException;

	void changePassword(Long userId, String oldPassword, String newPassword, String newConfirmedPassword) throws AppException;
}
