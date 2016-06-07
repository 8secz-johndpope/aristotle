package com.aristotle.member.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristotle.core.enums.CreationType;
import com.aristotle.core.exception.AppException;
import com.aristotle.core.exception.FieldsAppException;
import com.aristotle.core.persistance.Email;
import com.aristotle.core.persistance.LoginAccount;
import com.aristotle.core.persistance.Phone;
import com.aristotle.core.persistance.User;
import com.aristotle.core.persistance.Email.ConfirmationType;
import com.aristotle.core.persistance.Phone.PhoneType;
import com.aristotle.core.persistance.repo.EmailRepository;
import com.aristotle.core.persistance.repo.LoginAccountRepository;
import com.aristotle.core.persistance.repo.PhoneRepository;
import com.aristotle.core.persistance.repo.UserRepository;
import com.aristotle.core.service.PasswordUtil;

@Service
@Transactional
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
	private PasswordUtil passwordUtil;
	private final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
	
	@Override
	public User login(String userName, String password) throws AppException {
        LoginAccount loginAccount = loginAccountRepository.getLoginAccountByUserName(userName.toLowerCase());
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
            throw new AppException("Email id must be provided");
        }
        LoginAccount loginAccount = loginAccountRepository.getLoginAccountByUserName(emailId.toString());
        if(loginAccount != null){
        	throwFieldAppException("userName", "Email ["+emailId+"] already regsitered, please try some other user name");
        }
        
        User user = new User();
        user.setCreationType(CreationType.SelfServiceUser);
        user.setName(name);
        user.setNri(nri);
        user = userRepository.save(user);
        
        loginAccount = new LoginAccount();
        loginAccount.setEmail(emailId);
        loginAccount.setPassword(password);
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
            throwFieldAppException("email", "Email is already registered");
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

}
