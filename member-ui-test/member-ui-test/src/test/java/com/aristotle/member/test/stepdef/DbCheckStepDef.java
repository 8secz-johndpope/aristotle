package com.aristotle.member.test.stepdef;

import java.util.List;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Email;
import com.aristotle.core.persistance.Phone;
import com.aristotle.core.persistance.User;
import com.aristotle.core.persistance.repo.EmailRepository;
import com.aristotle.core.persistance.repo.PhoneRepository;
import com.aristotle.core.persistance.repo.UserRepository;
import com.aristotle.member.test.exception.FieldDoNotExistsException;

import cucumber.api.java.en.Given;

public class DbCheckStepDef extends BaseStepDef{
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private EmailRepository emailRepository;
	@Autowired
	private PhoneRepository phoneRepository;
	
	@Given("Check one user exists")
    public void checkOneUserExists(List<User> users) throws FieldDoNotExistsException, AppException {
		List<User> dbUsers = userRepository.findAll();
		Assert.assertEquals(1, dbUsers.size());
		User expectedUser = users.get(0);
		User dbUser = dbUsers.get(0);
		assertUserEquals(expectedUser, dbUser);

    }
	@Given("Check one email exists")
    public void checkOneEmailExists(List<Email> emails) throws FieldDoNotExistsException, AppException {
		List<Email> dbEmails = emailRepository.findAll();
		Assert.assertEquals(1, dbEmails.size());
		assertEmailEquals(emails.get(0), dbEmails.get(0));
    }
	@Given("Check one phone exists")
    public void checkOnePhoneExists(List<Phone> phones) throws FieldDoNotExistsException, AppException {
		List<Phone> dbPhones = phoneRepository.findAll();
		Assert.assertEquals(1, dbPhones.size());
		assertPhoneEquals(phones.get(0), dbPhones.get(0));
    }
	@Given("Check email and user are connected")
    public void checkEmailAndUserAreConnected() throws FieldDoNotExistsException, AppException {
		List<Email> dbEmails = emailRepository.findAll();
		List<User> dbUsers = userRepository.findAll();

		Assert.assertEquals(1, dbEmails.size());
		Assert.assertEquals(1, dbUsers.size());
		
		User dbUser = dbUsers.get(0);
		Email dbEmail = dbEmails.get(0);
		Assert.assertEquals(dbUser.getId(), dbEmail.getUserId());

    }
	@Given("Check phone and user are connected")
    public void checkPhoneAndUserAreConnected() throws FieldDoNotExistsException, AppException {
		List<Phone> dbPhones = phoneRepository.findAll();
		List<User> dbUsers = userRepository.findAll();

		Assert.assertEquals(1, dbPhones.size());
		Assert.assertEquals(1, dbUsers.size());
		
		User dbUser = dbUsers.get(0);
		Phone dbPhone = dbPhones.get(0);
		Assert.assertEquals(dbUser.getId(), dbPhone.getUserId());

    }
	@Given("Check email and phone are connected")
    public void checkEmailAndPhoneAreConnected() throws FieldDoNotExistsException, AppException {
		List<Email> dbEmails = emailRepository.findAll();
		List<Phone> dbPhones = phoneRepository.findAll();

		Assert.assertEquals(1, dbEmails.size());
		Assert.assertEquals(1, dbPhones.size());
		
		Phone dbPhone = dbPhones.get(0);
		Email dbEmail = dbEmails.get(0);
		Assert.assertEquals(dbPhone.getId(), dbEmail.getPhoneId());

    }
	private void assertUserEquals(User expectedUser, User dbUser){
		Assert.assertEquals(expectedUser.getName(), dbUser.getName());
		Assert.assertEquals(expectedUser.getCreationType(), dbUser.getCreationType());
		Assert.assertEquals(expectedUser.isNri(), dbUser.isNri());
		Assert.assertEquals(expectedUser.isSuperAdmin(), dbUser.isSuperAdmin());
		Assert.assertEquals(expectedUser.isDonor(), dbUser.isDonor());
		Assert.assertEquals(expectedUser.isMember(), dbUser.isMember());
	}
	private void assertEmailEquals(Email expectedEmail, Email dbEmail){
		Assert.assertEquals(expectedEmail.getEmail(), dbEmail.getEmail());
		Assert.assertEquals(expectedEmail.getEmailUp(), dbEmail.getEmailUp());
		Assert.assertEquals(expectedEmail.isConfirmed(), dbEmail.isConfirmed());
		Assert.assertEquals(expectedEmail.getConfirmationType(), dbEmail.getConfirmationType());
		Assert.assertEquals(expectedEmail.isNewsLetter(), dbEmail.isNewsLetter());
	}
	private void assertPhoneEquals(Phone expectedEmail, Phone dbEmail){
		Assert.assertEquals(expectedEmail.getCountryCode(), dbEmail.getCountryCode());
		Assert.assertEquals(expectedEmail.getPhoneNumber(), dbEmail.getPhoneNumber());
		Assert.assertEquals(expectedEmail.isConfirmed(), dbEmail.isConfirmed());
		Assert.assertEquals(expectedEmail.getPhoneType(), dbEmail.getPhoneType());
	}
}
