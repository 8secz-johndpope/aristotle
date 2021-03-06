package com.aristotle.core.service;

import java.io.InputStream;
import java.util.List;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Location;
import com.aristotle.core.persistance.Membership;
import com.aristotle.core.persistance.MembershipTransaction;
import com.aristotle.core.persistance.User;
import com.aristotle.core.persistance.UserLocation;
import com.aristotle.core.service.dto.OfflineMember;
import com.aristotle.core.service.dto.SearchUser;
import com.aristotle.core.service.dto.UserContactBean;
import com.aristotle.core.service.dto.UserPersonalDetailBean;
import com.aristotle.core.service.dto.UserRegisterBean;
import com.aristotle.core.service.dto.UserSearchResult;
import com.aristotle.core.service.dto.UserSearchResultForEdting;
import com.aristotle.core.service.dto.UserUploadDto;
import com.aristotle.core.service.dto.UserVolunteerBean;

public interface UserService {

    void registerUserQuick(UserContactBean userContactBean) throws AppException;

    void registerUser(UserRegisterBean userRegisterBean) throws AppException;

    List<UserSearchResult> searchUsers(SearchUser searchUser) throws AppException;

    List<UserSearchResultForEdting> searchUserForEditing(SearchUser searchUser) throws AppException;

    UserSearchResultForEdting saveUserFromAdminPanel(UserSearchResultForEdting userSearchResultForEdting) throws AppException;

    User login(String userName, String password) throws AppException;

    User getUserById(Long userId) throws AppException;

    List<UserLocation> getUserLocations(Long userId) throws AppException;

    List<UserSearchResult> searchUserByEmail(String emailId) throws AppException;

    List<UserSearchResult> searchUserByMobile(String mobile) throws AppException;

    List<UserSearchResult> searchNriUserForVolunteerIntrest(List<Long> intrests) throws AppException;

    List<UserSearchResult> searchGlobalUserForVolunteerIntrest(List<Long> intrests) throws AppException;

    List<UserSearchResult> searchLocationUserForVolunteerIntrest(Long locationId, List<Long> intrests) throws AppException;

    void changePassword(Long userId, String oldPassword, String newPassword) throws AppException;
    
    void updatePersonalDetails(Long userId, UserPersonalDetailBean userPersonalDetailBean) throws AppException;

    void updateVolunteerDetails(Long userId, UserVolunteerBean userVolunteerBean) throws AppException;

    void generateUserLoginAccount(String email) throws AppException;

    void generateUserLoginAccountForMobile(String phone) throws AppException;

    void sendPasswordResetEmail(String email) throws AppException;

    void updatePassword(String email, String newPassword, String token) throws AppException;

    void sendEmailConfirmtionEmail(String email) throws AppException;
    
    public void sendMembershipConfirmtionEmail(String emailId) throws AppException;

    void confirmEmail(String email, String token) throws AppException;

    void updateUserProfilePic(Long userid, String photo) throws AppException;

    User registerIvrMember(String mobileNumber, String name, String gender, String amount, String paymentMode, String state, String district, String msg) throws AppException;

    User registerOnlineMember(Long loggedInUserId, String mobileNumber, String name, String amount, String paymentMode, String transactionId,String fees) throws AppException;

    User uploadUserProfilePic(InputStream fileInputStream, User user, String fileName) throws AppException;

    void checkUserStatus(List<UserUploadDto> users) throws AppException;

    void saveMembers(List<UserUploadDto> users, boolean createUserNamePassword, Location state, Location district, Location pc, Location ac) throws AppException;
    
    void saveMember(UserUploadDto oneUserUploadDto, boolean createUserNamePassword, Location state, Location district, Location pc, Location ac) throws AppException;
    
    List<MembershipTransaction> getUserMembershipTransactions(Long userId) throws AppException;
    
    Membership getUserMembership(Long userId) throws AppException;
    
    User saveOfflineMember(OfflineMember member) throws AppException;
    
    List<UserSearchResult> getUsersCreatedBy(Long userId) throws AppException;
    
    List<String> deleteUserByMemberId(String memberId) throws AppException;
    
}
