package com.aristotle.member.service;

import java.util.Date;
import java.util.List;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Email;
import com.aristotle.core.persistance.Membership;
import com.aristotle.core.persistance.MembershipTransaction;
import com.aristotle.core.persistance.User;
import com.aristotle.core.persistance.UserLocation;

public interface MemberService {
	User login(String userName, String password) throws AppException;
	
	User getUserById(long userId) throws AppException;

	
	User register(String userName, String password, String passwordConfirm, String email, String countryCode, String mobileNumber, boolean nri) throws AppException;

	void changePassword(Long userId, String oldPassword, String newPassword, String newConfirmedPassword) throws AppException;
	
	Email getUserEmail(Long userId) throws AppException;
	
	Email updateEmail(Long emailId, String email, String confirmEmail) throws AppException;
	
	User updateUserProfilePic(Long userId, String newImageUrl) throws AppException;

	User updateUserPersonalDetails(Long userId, String name, String gender, Date dob, String idCardType, String idCardNumber, String fatherName, String motherName) throws AppException;

	User updateNriUserLocations(Long userId, Long countryId, Long countryRegionId,Long countryRegionAreadId, Long stateId, Long districtId, Long pcId, Long acId) throws AppException;

	User updateUserLocations(Long userId, Long livingStateId, Long livingDistrictId,Long livingPcId, Long livingAcId, Long votingStateId, Long votingDistrictId, Long votingPcId, Long votingAcId) throws AppException;

	List<UserLocation> getUserLocations(Long userId) throws AppException;

	List<MembershipTransaction> getUserMembershipTransactions(Long userId) throws AppException;
    
    Membership getUserMembership(Long userId) throws AppException;
}
