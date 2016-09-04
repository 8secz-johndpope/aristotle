package com.aristotle.member.service;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.mortbay.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aristotle.core.enums.CreationType;
import com.aristotle.core.exception.AppException;
import com.aristotle.core.exception.FieldsAppException;
import com.aristotle.core.persistance.Email;
import com.aristotle.core.persistance.LoginAccount;
import com.aristotle.core.persistance.Membership;
import com.aristotle.core.persistance.MembershipTransaction;
import com.aristotle.core.persistance.PasswordResetRequest;
import com.aristotle.core.persistance.Phone;
import com.aristotle.core.persistance.User;
import com.aristotle.core.persistance.UserLocation;
import com.aristotle.core.persistance.Email.ConfirmationType;
import com.aristotle.core.persistance.Location;
import com.aristotle.core.persistance.Phone.PhoneType;
import com.aristotle.core.persistance.repo.EmailRepository;
import com.aristotle.core.persistance.repo.LocationRepository;
import com.aristotle.core.persistance.repo.LoginAccountRepository;
import com.aristotle.core.persistance.repo.MembershipRepository;
import com.aristotle.core.persistance.repo.MembershipTransactionRepository;
import com.aristotle.core.persistance.repo.PasswordResetRequestRepository;
import com.aristotle.core.persistance.repo.PhoneRepository;
import com.aristotle.core.persistance.repo.UserLocationRepository;
import com.aristotle.core.persistance.repo.UserRepository;
import com.aristotle.core.service.EmailManager;
import com.aristotle.core.service.PasswordUtil;
import com.google.gdata.util.common.base.StringUtil;

@Service
@Transactional(rollbackFor = Exception.class)
public class MemberServiceImpl implements MemberService{

	@Autowired
	private LoginAccountRepository loginAccountRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private EmailRepository emailRepository;
	@Autowired
	private PhoneRepository phoneRepository;
	@Autowired
	private UserLocationRepository userLocationRepository;
	@Autowired
	private LocationRepository locationRepository;
	@Autowired
	private MembershipRepository membershipRepository;
	@Autowired
	private MembershipTransactionRepository membershipTransactionRepository;
	@Autowired
	private PasswordUtil passwordUtil;
	@Autowired
	private PasswordResetRequestRepository passwordResetRequestRepository;
	@Autowired
    private EmailManager emailManager;
	@Value("${registration_mail_id}")
    private String regsitrationEmailId;
	private final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    
    private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public User login(String userName, String password) throws AppException {
        LoginAccount loginAccount = loginAccountRepository.getLoginAccountByUserName(userName.toLowerCase());
        if (loginAccount == null) {
        	loginAccount = loginAccountRepository.getLoginAccountByEmail(userName.toLowerCase());
        }
        if (loginAccount == null) {
        	loginAccount = loginAccountRepository.getLoginAccountByPhone(userName.toLowerCase());
        }
        logger.info("Login Account : "+loginAccount);
        if (loginAccount == null) {
            throw new AppException("Invalid user name/password");
        }
        if (!passwordUtil.checkPassword(password, loginAccount.getPassword())) {
            throw new AppException("Invalid user name/password");
        }
        return loginAccount.getUser();
    }

	@Override
	public User register(String name, String password, String passwordConfirm, String emailId, String countryCode, String mobileNumber,  boolean nri) throws AppException {
		Email email = null;
        Phone phone = null;
        if(!password.equals(passwordConfirm)){
        	throw new AppException("Password do not match");
        }
        email = getOrCreateEmail(emailId);
        phone = getOrCreateMobile(mobileNumber, countryCode, "mobile");
        if (email == null) {
            throw new AppException("Email must be provided");
        }
        LoginAccount loginAccount = loginAccountRepository.getLoginAccountByUserName(emailId.toString());
        if(loginAccount != null){
        	throwFieldAppException("userName", "Email ["+emailId+"] already regsitered, please try some other user name");
        }
        if(nri && phone == null){
        	throwFieldAppException("phone", "Phone Number must be provided");

        }
        User user = new User();
        user.setCreationType(CreationType.SelfServiceUser);
        user.setName(name);
        user.setNri(nri);
        user = userRepository.save(user);
        
        addNriCountry(user, countryCode);
        
        loginAccount = new LoginAccount();
        loginAccount.setEmail(emailId.toLowerCase());
        loginAccount.setPhone(mobileNumber);
        loginAccount.setPassword(passwordUtil.encryptPassword(password));
        loginAccount.setUserName(emailId);
        loginAccount.setUser(user);

        loginAccount = loginAccountRepository.save(loginAccount);
        
        email.setUser(user);
        if(phone != null){
        	phone.setUser(user);
        	email.setPhone(phone);
        }
		return user;
	}
	
	private void addNriCountry(User user, String countryCode){
		if("91".equals(countryCode) || StringUtils.isEmpty(countryCode)){
			return;
		}
		Location country = locationRepository.getLocationByIsdCode(countryCode);
		if(country == null){
			return;
		}
		updateUserLocation(user, UserLocation.LIVING, "Country", country.getId());

	}
	
	@Override
    public void changePassword(Long userId, String oldPassword, String newPassword, String newConfirmedPassword) throws AppException {
		if(!newPassword.equals(newConfirmedPassword)){
            throw new AppException("Password Do not match");
		}
        LoginAccount loginAccount = loginAccountRepository.getLoginAccountByUserId(userId);
        if (!passwordUtil.checkPassword(oldPassword, loginAccount.getPassword())) {
            throw new AppException("Old password Do not match");
        }
        loginAccount.setPassword(passwordUtil.encryptPassword(newPassword));
        loginAccount = loginAccountRepository.save(loginAccount);
    }
	
	private Email getOrCreateEmail(String emailId) throws AppException {
        if (StringUtils.isBlank(emailId)) {
            return null;
        }
        Matcher matcher = pattern.matcher(emailId);
        if (!matcher.matches()) {
            throwFieldAppException("email", "Invalid Email id");
        }
        Email email = emailRepository.getEmailByEmailUp(emailId.toUpperCase());
        if (email == null) {
            email = new Email();
            email.setConfirmed(false);
            email.setEmail(emailId);
            email.setEmailUp(emailId.toUpperCase());
            email.setNewsLetter(true);
            email.setConfirmationType(ConfirmationType.UN_CONFIRNED);
            email = emailRepository.save(email);
        }
        if (email.isConfirmed() || email.getUser() != null) {
            throwFieldAppException("email", "Email ["+emailId+"] is already registered");
        }
        email.setNewsLetter(true);
        return email;
    }
	private Phone getOrCreateMobile(String mobileNumber, String countryCode, String fieldName) throws AppException {
        return getOrCreateMobile(mobileNumber, countryCode, fieldName, true);
    }
    private Phone getOrCreateMobile(String mobileNumber, String countryCode, String fieldName, boolean failIfExists) throws AppException {
        if (StringUtils.isBlank(mobileNumber)) {
            return null;
        }
        if (StringUtils.isBlank(countryCode)) {
            throwFieldAppException(fieldName, "Country Code must be provided");
        }
        Phone mobile = phoneRepository.getPhoneByPhoneNumberAndCountryCode(mobileNumber, countryCode);
        if (mobile == null) {
            mobile = new Phone();
            mobile.setCountryCode(countryCode);
            mobile.setPhoneNumber(mobileNumber);
            if (countryCode.equals("91")) {
                mobile.setPhoneType(PhoneType.MOBILE);
            } else {
                mobile.setPhoneType(PhoneType.NRI_MOBILE);
            }

            mobile = phoneRepository.save(mobile);
        }
        if (failIfExists && (mobile.getUser() != null || mobile.isConfirmed())) {
            throwFieldAppException(fieldName, "Mobile ["+mobile.getPhoneNumber()+"] already registered");
        }
        return mobile;
    }

    private void throwFieldAppException(String fieldName, String error) throws FieldsAppException {
        FieldsAppException fieldsAppException = new FieldsAppException(error);
        fieldsAppException.addFieldError(fieldName, error);
        throw fieldsAppException;
    }

	@Override
	public Email getUserEmail(Long userId) throws AppException {
		List<Email> userEmails = emailRepository.getEmailsByUserId(userId);
		if(userEmails == null || userEmails.isEmpty()){
			return null;
		}
		return userEmails.get(0);
	}

	@Override
	public Email updateEmail(Long emailId, String newEmailId, String confirmNewEmailId) throws AppException {
		if(StringUtil.isEmpty(newEmailId) || StringUtil.isEmpty(confirmNewEmailId)){
            throw new AppException("Please enter new email");
		}
		if(!newEmailId.equals(confirmNewEmailId)){
            throw new AppException("Emails do not match");
		}
		Matcher matcher = pattern.matcher(newEmailId);
        if (!matcher.matches()) {
        	throw new AppException("Invalid new Email Id, must be in format email@website.domain");
        }
        matcher = pattern.matcher(confirmNewEmailId);
        if (!matcher.matches()) {
        	throw new AppException("Invalid new confirmed email Id, must be in format email@website.domain");
        }
		Email existingEmail = emailRepository.getEmailByEmailUp(newEmailId.toUpperCase());
		if(existingEmail != null && existingEmail.getUser() != null ){
			throw new AppException("Email["+  newEmailId + "] is already registered");
		}
		Email email = emailRepository.findOne(emailId);
		email.setEmail(newEmailId);
		email.setEmailUp(newEmailId.toUpperCase());
		email.setConfirmed(false);
		email.setConfirmationType(ConfirmationType.UN_CONFIRNED);
		email.setConfirmationDate(null);
		return emailRepository.save(email);
	}

	@Override
	public User getUserById(long userId) throws AppException {
		return userRepository.findOne(userId);
	}

	@Override
	public User updateUserPersonalDetails(Long userId, String name, String gender, Date dob, String idCardType, String idCardNumber, String fatherName, String motherName, String aboutMe) throws AppException{
		User user = userRepository.findOne(userId);
		user.setName(name);
		user.setGender(gender);
		user.setDateOfBirth(dob);
		user.setIdentityType(idCardType);
		user.setIdentityNumber(idCardNumber);
		user.setFatherName(fatherName);
		user.setMotherName(motherName);
		user.setProfile(aboutMe);
		user = userRepository.save(user);
		return user;
	}

	@Override
	public User updateNriUserLocations(Long userId, Long countryId, Long countryRegionId, Long countryRegionAreadId, Long stateId, Long districtId, Long pcId, Long acId) throws AppException {
		User user = userRepository.findOne(userId);
		if(user == null){
			throw new AppException("No such user found ["+userId+"]");
		}
		user.setNri(true);
		//First Delete India Living Location If exists
		deleteUserLocation(user, UserLocation.LIVING, "State");
		deleteUserLocation(user, UserLocation.LIVING, "District");
		deleteUserLocation(user, UserLocation.LIVING, "ParliamentConstituency");
		deleteUserLocation(user, UserLocation.LIVING, "AssemblyConstituency");

		updateUserLocation(user, UserLocation.LIVING, "Country", countryId);
		updateUserLocation(user, UserLocation.LIVING, "CountryRegion", countryRegionId);
		updateUserLocation(user, UserLocation.LIVING, "CountryRegionArea", countryRegionAreadId);
		updateUserLocation(user, UserLocation.VOTING, "State", stateId);
		updateUserLocation(user, UserLocation.VOTING, "District", districtId);
		updateUserLocation(user, UserLocation.VOTING, "ParliamentConstituency", pcId);
		updateUserLocation(user, UserLocation.VOTING, "AssemblyConstituency", acId);
		
		return user;
	}
	private void deleteUserLocation(User user, String userLocationType, String locationType){
		UserLocation currentUserLocation = userLocationRepository.getUserLocationByUserIdAndLocationTypesAndUserLocationType(user.getId(), userLocationType, locationType);
		if(currentUserLocation !=null){
			userLocationRepository.delete(currentUserLocation);
		}
	}
	private void updateUserLocation(User user, String userLocationType, String locationType, Long locationId){
		logger.info("Updating User Location for User :{}, userLocationType:{}, locationType:{}, locationId:{}", user.getId(), userLocationType, locationType, locationId);
		UserLocation currentUserLocation = userLocationRepository.getUserLocationByUserIdAndLocationTypesAndUserLocationType(user.getId(), userLocationType, locationType);
		if(locationId == null){
			if(currentUserLocation !=null){
				userLocationRepository.delete(currentUserLocation);
			}
			return;
		}
		
		Location location = locationRepository.findOne(locationId);
		if(currentUserLocation == null){
			currentUserLocation = new UserLocation();
		}
		currentUserLocation.setLocation(location);
		currentUserLocation.setUser(user);
		currentUserLocation.setUserLocationType(userLocationType);
		currentUserLocation = userLocationRepository.save(currentUserLocation);
	}

	@Override
	public User updateUserLocations(Long userId, Long livingStateId, Long livingDistrictId, Long livingPcId, Long livingAcId, Long votingStateId, Long votingDistrictId, Long votingPcId, Long votingAcId)
			throws AppException {
		User user = userRepository.findOne(userId);
		if(user == null){
			throw new AppException("No such user found ["+userId+"]");
		}
		user.setNri(false);
		//First Delete NRI Living Location If exists
		deleteUserLocation(user, UserLocation.LIVING, "Country");
		deleteUserLocation(user, UserLocation.LIVING, "CountryRegion");
		deleteUserLocation(user, UserLocation.LIVING, "CountryRegionArea");

		updateUserLocation(user, UserLocation.LIVING, "State", livingStateId);
		updateUserLocation(user, UserLocation.LIVING, "District", livingDistrictId);
		updateUserLocation(user, UserLocation.LIVING, "ParliamentConstituency", livingPcId);
		updateUserLocation(user, UserLocation.LIVING, "AssemblyConstituency", livingAcId);

		updateUserLocation(user, UserLocation.VOTING, "State", votingStateId);
		updateUserLocation(user, UserLocation.VOTING, "District", votingDistrictId);
		updateUserLocation(user, UserLocation.VOTING, "ParliamentConstituency", votingPcId);
		updateUserLocation(user, UserLocation.VOTING, "AssemblyConstituency", votingAcId);
		
		return user;
	}

	@Override
	public List<UserLocation> getUserLocations(Long userId) throws AppException {
		return userLocationRepository.getUserLocationByUserId(userId);
	}

	@Override
	public User updateUserProfilePic(Long userId, String newImageUrl) throws AppException {
		User user = userRepository.findOne(userId);
		user.setProfilePic(newImageUrl);
		return user;
	}
	
	public List<MembershipTransaction> getUserMembershipTransactions(Long userId) throws AppException {
		Membership membership = membershipRepository.getMembershipByUserId(userId);
		logger.info("Membership found is : {}", membership);
		if(membership == null){
			return Collections.emptyList();
		}
		return membershipTransactionRepository.getMembershipTransactionByMembershipId(membership.getId());
	}

	@Override
	public Membership getUserMembership(Long userId) throws AppException {
		return membershipRepository.getMembershipByUserId(userId);
	}
	
	@Override
    public void sendPasswordResetEmail(String emailId) throws AppException {
        Email email = emailRepository.getEmailByEmailUp(emailId.toUpperCase());
        if (email == null) {
            throw new AppException("No accounts exists for email " + emailId);
        }
        User user = email.getUser();
        if (user == null) {
            throw new AppException("No accounts exists for email " + emailId);
        }
        LoginAccount loginAccount = loginAccountRepository.getLoginAccountByUserId(user.getId());
        if (loginAccount == null) {
            throw new AppException("No accounts exists for email " + emailId);
        }

        PasswordResetRequest passwordResetRequest = passwordResetRequestRepository.getPasswordResetRequestByLoginAccountId(loginAccount.getId());
        if (passwordResetRequest == null) {
            passwordResetRequest = new PasswordResetRequest();
        }
        passwordResetRequest.setUserName(emailId.toLowerCase());
        passwordResetRequest.setToken(UUID.randomUUID().toString());
        passwordResetRequest.setLoginAccountId(loginAccount.getId());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, 24);
        passwordResetRequest.setValidTill(calendar.getTime());
        passwordResetRequest = passwordResetRequestRepository.save(passwordResetRequest);
        sendPasswordEmail(loginAccount, passwordResetRequest);
    }

    private void sendPasswordEmail(LoginAccount loginAccount, PasswordResetRequest passwordResetRequest) throws AppException {
        StringBuilder sb = new StringBuilder();
        sb.append("Dear " + loginAccount.getUser().getName());
        sb.append("<br>");
        sb.append("<p>A password reset request has been submitted on your behalf at <a href=\"http://www.swarajabhiyan.org\">www.swarajabhiyan.org</a>");
        sb.append("<br><br>");
        sb.append("<p>If you feel that this has been done in error, delete and disregard this email. Your account is still secure and no one has been given access to it. It is not locked and your password has not been reset. Someone could have mistakenly entered your email address.</p>");
        sb.append("<br><br>");
        sb.append("<p>Follow the link below to change your password.</p>");
        sb.append("<br>http://www.swarajabhiyan.org/password/update?token=" + passwordResetRequest.getToken());
        sb.append("<br>");
        sb.append("<br>");
        sb.append("<br>");
        sb.append("<br>Thanks");
        sb.append("<br>Swaraj Abhiyan Team");
        sb.append("<br><br>++++++++++++++++++++++++++++");
        sb.append("<br>Website : www.swarajabhiyan.org");
        sb.append("<br>Email Id: contact@swarajabhiyan.org");
        sb.append("<br>Helpline no : +91-7210222333");
        sb.append("<br>Twitter Handle : https://twitter.com/swaraj_abhiyan");
        sb.append("<br>Facebook Pages : https://www.facebook.com/swarajabhiyan");
        sb.append("<br>Facebook group : https://www.facebook.com/groups/swarajabhiyan/");
        sb.append("<br>Volunteer Registration : http://www.swarajabhiyan.org/register");
        sb.append("<br>Swaraj Abhiyan Channel https://www.youtube.com/SwarajAbhiyanTV");
        sb.append("<br>Head Office : A-189, Sec-43, Noida UP");

        // now send Email
        String contentWithOutHtml = sb.toString();
        contentWithOutHtml = contentWithOutHtml.replaceAll("<br>", "\n");
        contentWithOutHtml = contentWithOutHtml.replaceAll("\\<[^>]*>", "");
        emailManager.sendEmail(loginAccount.getEmail(), "Password Reset", regsitrationEmailId, "Swaraj Abhiyan Password Reset Request", contentWithOutHtml, sb.toString());

    }
}
