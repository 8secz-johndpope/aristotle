package com.aristotle.member.service;

import java.util.Date;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Email;
import com.aristotle.core.persistance.User;

public interface MemberService {
	User login(String userName, String password) throws AppException;
	
	User getUserById(long userId) throws AppException;

	
	User register(String userName, String password, String passwordConfirm, String email, String countryCode, String mobileNumber, boolean nri) throws AppException;

	void changePassword(Long userId, String oldPassword, String newPassword, String newConfirmedPassword) throws AppException;
	
	Email getUserEmail(Long userId) throws AppException;
	
	Email updateEmail(Long emailId, String email, String confirmEmail) throws AppException;
	
	User updateUserPersonalDetails(Long userId, String name, String gender, Date dob, String idCardType, String idCardNumber, String fatherName, String motherName);

}
