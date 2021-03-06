package com.aristotle.core.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.neo4j.cypher.internal.compiler.v2_1.functions.E;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.aristotle.core.enums.CreationType;
import com.aristotle.core.exception.AppException;
import com.aristotle.core.exception.FieldsAppException;
import com.aristotle.core.persistance.Email;
import com.aristotle.core.persistance.Email.ConfirmationType;
import com.aristotle.core.persistance.EmailConfirmationRequest;
import com.aristotle.core.persistance.Interest;
import com.aristotle.core.persistance.Location;
import com.aristotle.core.persistance.LoginAccount;
import com.aristotle.core.persistance.Membership;
import com.aristotle.core.persistance.MembershipTransaction;
import com.aristotle.core.persistance.PasswordResetRequest;
import com.aristotle.core.persistance.Phone;
import com.aristotle.core.persistance.Phone.PhoneType;
import com.aristotle.core.persistance.Sms;
import com.aristotle.core.persistance.User;
import com.aristotle.core.persistance.UserLocation;
import com.aristotle.core.persistance.Volunteer;
import com.aristotle.core.persistance.repo.EmailConfirmationRequestRepository;
import com.aristotle.core.persistance.repo.EmailRepository;
import com.aristotle.core.persistance.repo.InterestRepository;
import com.aristotle.core.persistance.repo.LocationRepository;
import com.aristotle.core.persistance.repo.LoginAccountRepository;
import com.aristotle.core.persistance.repo.MembershipRepository;
import com.aristotle.core.persistance.repo.MembershipTransactionRepository;
import com.aristotle.core.persistance.repo.PasswordResetRequestRepository;
import com.aristotle.core.persistance.repo.PhoneRepository;
import com.aristotle.core.persistance.repo.SmsRepository;
import com.aristotle.core.persistance.repo.UserLocationRepository;
import com.aristotle.core.persistance.repo.UserRepository;
import com.aristotle.core.persistance.repo.VolunteerRepository;
import com.aristotle.core.service.aws.UserSearchService;
import com.aristotle.core.service.dto.OfflineMember;
import com.aristotle.core.service.dto.SearchUser;
import com.aristotle.core.service.dto.UserContactBean;
import com.aristotle.core.service.dto.UserPersonalDetailBean;
import com.aristotle.core.service.dto.UserRegisterBean;
import com.aristotle.core.service.dto.UserSearchResult;
import com.aristotle.core.service.dto.UserSearchResultForEdting;
import com.aristotle.core.service.dto.UserUploadDto;
import com.aristotle.core.service.dto.UserVolunteerBean;

@Service
@Transactional(rollbackOn=Exception.class)
@Lazy
public class UserServiceImpl implements UserService {

    @Autowired
    private UserLocationRepository userLocationRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VolunteerRepository volunteerRepository;
    @Autowired
    private InterestRepository interestRepository;
    @Autowired
    private PasswordUtil passwordUtil;
    @Autowired
    private LoginAccountRepository loginAccountRepository;
    @Autowired
    private PasswordResetRequestRepository passwordResetRequestRepository;
    @Autowired
    private EmailConfirmationRequestRepository emailConfirmationRequestRepository;
    @Autowired
    private MembershipRepository membershipRepository;
    @Autowired
    private MembershipTransactionRepository membershipTransactionRepository;
    @Autowired
    private AwsFileManager awsFileManager;
    @Autowired
    private UserSearchService userSearchService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private SmsRepository smsRepository;
    
    @Value("${aws_access_key}")
    private String awsKey;
    @Value("${aws_access_secret}")
    private String awsSecret;

    @Value("${static_data_env:dev}")
    private String staticDataEnv;

    @Value("${registration_mail_id}")
    private String regsitrationEmailId;

    private final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private EmailManager emailManager;

    @Override
    public List<UserSearchResult> searchUsers(SearchUser searchUser) throws AppException {

        List<UserSearchResult> userSearchResults = new ArrayList<UserSearchResult>();

        searchUserForEmail(userSearchResults, searchUser.getEmail());
        searchUserForMobile(userSearchResults, searchUser.getMobileNumber(), searchUser.getCountryCode());
        return userSearchResults;
    }

    @Override
    public List<UserSearchResultForEdting> searchUserForEditing(SearchUser searchUser) throws AppException {
        List<UserSearchResultForEdting> userSearchResults = new ArrayList<UserSearchResultForEdting>();

        if (!StringUtils.isBlank(searchUser.getEmail())) {
            Email email = emailRepository.getEmailByEmailUp(searchUser.getEmail().toUpperCase());
            if (email != null && email.getUser() != null) {
                UserSearchResultForEdting userSearchResultForEdting = new UserSearchResultForEdting();
                userSearchResultForEdting.setEmail(email);
                userSearchResultForEdting.setUser(email.getUser());
                List<Phone> phones = phoneRepository.getPhonesByUserId(email.getUser().getId());
                if (phones != null && phones.size() > 0) {
                    userSearchResultForEdting.setPhone(phones.get(0));
                }
                userSearchResults.add(userSearchResultForEdting);
            }
        }

        if (!StringUtils.isBlank(searchUser.getMobileNumber())) {
            Phone mobile = phoneRepository.getPhoneByPhoneNumberAndCountryCode(searchUser.getMobileNumber(), searchUser.getCountryCode());
            if (mobile != null && mobile.getUser() != null) {
                UserSearchResultForEdting userSearchResultForEdting = new UserSearchResultForEdting();
                userSearchResultForEdting.setPhone(mobile);
                userSearchResultForEdting.setUser(mobile.getUser());
                List<Email> emails = emailRepository.getEmailsByUserId(mobile.getUser().getId());
                if (emails != null && emails.size() > 0) {
                    userSearchResultForEdting.setEmail(emails.get(0));
                }
                userSearchResults.add(userSearchResultForEdting);

            }
        }


        return userSearchResults;
    }


    private List<UserSearchResult> convertUsers(List<User> users) {
        List<UserSearchResult> userSearchResults = new ArrayList<UserSearchResult>(users.size() + 1);
        for (User oneUser : users) {
            userSearchResults.add(convertUser(oneUser));
        }
        return userSearchResults;
    }

    private UserSearchResult convertUser(User user) {
        UserSearchResult userSearchResult = new UserSearchResult();
        BeanUtils.copyProperties(user, userSearchResult);
        return userSearchResult;

    }

    private Email searchUserForEmail(List<UserSearchResult> userSearchResults, String emailId) throws AppException {
        if (StringUtils.isBlank(emailId)) {
            return null;
        }
        Email email = emailRepository.getEmailByEmailUp(emailId.toUpperCase());
        if (email != null && email.getUser() != null) {
            UserSearchResult userSearchResult = convertUserToResult(email.getUser());
            userSearchResult.setEmail(emailId);
            userSearchResults.add(userSearchResult);
        }
        return email;
    }

    private Phone searchUserForPhone(List<UserSearchResult> userSearchResults, String mobile) throws AppException {
        if (StringUtils.isBlank(mobile)) {
            return null;
        }
        Phone phone = phoneRepository.getPhoneByPhoneNumber(mobile);
        if (phone != null && phone.getUser() != null) {
            UserSearchResult userSearchResult = convertUserToResult(phone.getUser());
            userSearchResult.setMobileNumber(mobile);
            userSearchResults.add(userSearchResult);
        }
        return phone;
    }

    private UserSearchResult convertUserToResult(User user) {
        UserSearchResult userSearchResult = convertUser(user);

        Set<Phone> phones = user.getPhones();
        if (phones != null) {
            StringBuilder sb = new StringBuilder();
            for (Phone onePhone : phones) {
                sb.append(onePhone.getCountryCode() + "-" + onePhone.getPhoneNumber() + ",");
            }
            userSearchResult.setMobileNumber(sb.toString());
        }
        Set<Email> emails = user.getEmails();
        if (emails != null) {
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (Email oneEmail : emails) {
                if (!first) {
                    sb.append(",");
                }
                sb.append(oneEmail.getEmail());
                first = false;
            }
            userSearchResult.setEmail(sb.toString());
        }
        List<UserLocation> userLocations = userLocationRepository.getUserLocationByUserId(user.getId());
        for (UserLocation oneUserLocation : userLocations) {
            if (oneUserLocation.getLocation().getLocationType().getName().equalsIgnoreCase("State")) {
                if (oneUserLocation.getUserLocationType().equalsIgnoreCase("Living")) {
                    userSearchResult.setLivingState(oneUserLocation.getLocation());
                } else {
                    userSearchResult.setVotingState(oneUserLocation.getLocation());
                }
            }
            if (oneUserLocation.getLocation().getLocationType().getName().equalsIgnoreCase("District")) {
                if (oneUserLocation.getUserLocationType().equalsIgnoreCase("Living")) {
                    userSearchResult.setLivingDistrict(oneUserLocation.getLocation());
                } else {
                    userSearchResult.setVotingDistrict(oneUserLocation.getLocation());
                }
            }
            if (oneUserLocation.getLocation().getLocationType().getName().equalsIgnoreCase("AssemblyConstituency")) {
                if (oneUserLocation.getUserLocationType().equalsIgnoreCase("Living")) {
                    userSearchResult.setLivingAssemblyConstituency(oneUserLocation.getLocation());
                } else {
                    userSearchResult.setVotingAssemblyConstituency(oneUserLocation.getLocation());
                }
            }
            if (oneUserLocation.getLocation().getLocationType().getName().equalsIgnoreCase("ParliamentConstituency")) {
                if (oneUserLocation.getUserLocationType().equalsIgnoreCase("Living")) {
                    userSearchResult.setLivingParliamentConstituency(oneUserLocation.getLocation());
                } else {
                    userSearchResult.setVotingParliamentConstituency(oneUserLocation.getLocation());
                }
            }
            if (oneUserLocation.getLocation().getLocationType().getName().equalsIgnoreCase("Country")) {
                userSearchResult.setNriCountry(oneUserLocation.getLocation());
            }
            if (oneUserLocation.getLocation().getLocationType().getName().equalsIgnoreCase("CountryRegion")) {
                userSearchResult.setNriCountryRegion(oneUserLocation.getLocation());
            }
            if (oneUserLocation.getLocation().getLocationType().getName().equalsIgnoreCase("CountryRegionArea")) {
                userSearchResult.setNriCountryRegionArea(oneUserLocation.getLocation());
            }
        }
        return userSearchResult;
    }

    private Phone searchUserForMobile(List<UserSearchResult> userSearchResults, String mobileNumber, String countryCode) throws AppException {
        if (StringUtils.isBlank(mobileNumber)) {
            return null;
        }
        Phone mobile = phoneRepository.getPhoneByPhoneNumberAndCountryCode(mobileNumber, countryCode);
        if (mobile != null && mobile.getUser() != null) {
            UserSearchResult userSearchResult = convertUser(mobile.getUser());
            userSearchResult.setMobileNumber(countryCode + "-" + mobileNumber);
            userSearchResults.add(userSearchResult);
        }
        return mobile;
    }

    @Override
    public void registerUserQuick(UserContactBean userContactBean) throws AppException {
        // Just save email and phone
        Email email = null;
        Phone phone = null;
        try{
            email = getOrCreateEmail(userContactBean.getEmail());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            phone = getOrCreateMobile(userContactBean.getMobile(), userContactBean.getCountryCode(), "mobile");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (email != null && phone != null) {
            email.setPhone(phone);
        }

    }

    @Override
    public void registerUser(UserRegisterBean userRegisterBean) throws AppException {

        Email email = getOrCreateEmail(userRegisterBean.getEmailId());
        Phone indiaMobile = getOrCreateMobile(userRegisterBean.getMobileNumber(), userRegisterBean.getCountryCode(), "mobileNumber");
        Phone nriMobile = getOrCreateMobile(userRegisterBean.getNriMobileNumber(), userRegisterBean.getNriCountryCode(), "nriMobileNumber");
        if (email == null && indiaMobile == null && nriMobile == null) {
            throw new AppException("Valid Mobile number or Email must be provided");
        }

        User dbUser = new User();
        BeanUtils.copyProperties(userRegisterBean, dbUser);
        dbUser.setCreationType(CreationType.OnlineUser);
        dbUser = userRepository.save(dbUser);
        // Update email with this user
        if (email != null) {
            email.setUser(dbUser);
        }
        if (indiaMobile != null) {
            indiaMobile.setUser(dbUser);
        }
        if (nriMobile != null) {
            nriMobile.setUser(dbUser);
        }

        // Update Locations
        // Map<Long, String> locationMap = getLocationMap(userRegisterBean);
        // Currently its only create and no updates as user is only submititng details and no way to update details until login is provided
        addLocationsTouser(dbUser, userRegisterBean.getAssemblyConstituencyLivingId(), "Living");
        addLocationsTouser(dbUser, userRegisterBean.getAssemblyConstituencyVotingId(), "Voting");
        addLocationsTouser(dbUser, userRegisterBean.getDistrictLivingId(), "Living");
        addLocationsTouser(dbUser, userRegisterBean.getDistrictLivingId(), "Voting");
        addLocationsTouser(dbUser, userRegisterBean.getNriCountryId(), "Living");
        addLocationsTouser(dbUser, userRegisterBean.getNriCountryRegionAreaId(), "Living");
        addLocationsTouser(dbUser, userRegisterBean.getNriCountryRegionId(), "Living");
        addLocationsTouser(dbUser, userRegisterBean.getParliamentConstituencyLivingId(), "Living");
        addLocationsTouser(dbUser, userRegisterBean.getParliamentConstituencyVotingId(), "Voting");
        addLocationsTouser(dbUser, userRegisterBean.getStateLivingId(), "Living");
        addLocationsTouser(dbUser, userRegisterBean.getStateVotingId(), "Voting");

        if (userRegisterBean.isVolunteer()) {
            Volunteer volunteer = new Volunteer();
            BeanUtils.copyProperties(userRegisterBean, volunteer);
            volunteer.setUser(dbUser);
            volunteer = volunteerRepository.save(volunteer);

            volunteer.setInterests(new HashSet<Interest>());
            System.out.println("userRegisterBean.getInterests()=" + userRegisterBean.getInterests());
            if (userRegisterBean.getInterests() != null) {
                for (Long oneInterestId : userRegisterBean.getInterests()) {
                    Interest oneInterest = interestRepository.findOne(oneInterestId);
                    if (oneInterest != null) {
                        volunteer.getInterests().add(oneInterest);
                    }
                }
            }

        }
        // create user login account
        if (StringUtils.isBlank(userRegisterBean.getPassword())) {
            String password = generateRandompassword();
            userRegisterBean.setPassword(password);
        }
        LoginAccount loginAccount = new LoginAccount();
        loginAccount.setUser(dbUser);
        loginAccount.setPassword(passwordUtil.encryptPassword(userRegisterBean.getPassword()));

        if (!StringUtils.isBlank(userRegisterBean.getEmailId())) {
            loginAccount.setEmail(userRegisterBean.getEmailId().toLowerCase());
            loginAccount.setUserName(userRegisterBean.getEmailId().toLowerCase());
        }
        if (StringUtils.isBlank(loginAccount.getUserName())) {
            throw new AppException("Login User name/email must be provided");
        }
        loginAccount = loginAccountRepository.save(loginAccount);
        if (email != null) {
            sendEmailConfirmtionEmail(email.getEmail());
        }
        sendMemberForIndexing(dbUser);
        //
    }

    private Map<Long, String> getLocationMap(UserRegisterBean userRegisterBean) {
        Map<Long, String> returnMap = new HashMap<Long, String>();
        returnMap.put(userRegisterBean.getAssemblyConstituencyLivingId(), "Living");
        returnMap.put(userRegisterBean.getAssemblyConstituencyVotingId(), "Voting");
        returnMap.put(userRegisterBean.getDistrictLivingId(), "Living");
        returnMap.put(userRegisterBean.getDistrictLivingId(), "Voting");
        returnMap.put(userRegisterBean.getNriCountryId(), "Living");
        returnMap.put(userRegisterBean.getNriCountryRegionAreaId(), "Living");
        returnMap.put(userRegisterBean.getNriCountryRegionId(), "Living");
        returnMap.put(userRegisterBean.getParliamentConstituencyLivingId(), "Living");
        returnMap.put(userRegisterBean.getParliamentConstituencyVotingId(), "Voting");
        returnMap.put(userRegisterBean.getStateLivingId(), "Living");
        returnMap.put(userRegisterBean.getStateVotingId(), "Voting");
        return returnMap;
    }

    private void addLocationsTouser(User dbUser, Long locationId, String type) throws AppException {
        if (locationId == null || locationId <= 0) {
            return;
        }
        UserLocation userLocation = new UserLocation();
        userLocation.setUser(dbUser);
        Location location = locationRepository.findOne(locationId);
        if (location == null) {
            throw new AppException("Invalid Location id [" + locationId + "]");
        }
        userLocation.setLocation(location);
        userLocation.setUserLocationType(type);

        userLocation = userLocationRepository.save(userLocation);
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

    private Email getOrCreateEmail(String emailId) throws AppException {
        return getOrCreateEmail(emailId, true);
    }
    private Email getOrCreateEmail(String emailId, boolean failIfExists) throws AppException {
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
        if (failIfExists && (email.isConfirmed() || email.getUser() != null)) {
            throwFieldAppException("email", "Email is already registered");
        }
        email.setNewsLetter(true);
        return email;
    }

    private void throwFieldAppException(String fieldName, String error) throws FieldsAppException {
        FieldsAppException fieldsAppException = new FieldsAppException(error);
        fieldsAppException.addFieldError(fieldName, error);
        throw fieldsAppException;
    }

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
    public List<UserLocation> getUserLocations(Long userId) throws AppException {
        return userLocationRepository.getUserLocationByUserId(userId);
    }

    @Override
    public List<UserSearchResult> searchUserByEmail(String emailId) throws AppException {
        List<UserSearchResult> list = new ArrayList<UserSearchResult>();
        searchUserForEmail(list, emailId);
        return list;
    }

    @Override
    public List<UserSearchResult> searchUserByMobile(String mobile) throws AppException {
        List<UserSearchResult> list = new ArrayList<UserSearchResult>();
        searchUserForPhone(list, mobile);
        return list;
    }


    private List<UserSearchResult> convertUserList(List<User> users) {
        if (users == null) {
            return Collections.emptyList();
        }
        List<UserSearchResult> returnList = new ArrayList<UserSearchResult>(users.size());
        for (User oneUser : users) {
            returnList.add(convertUserToResult(oneUser));
        }
        return returnList;
    }
    @Override
    public List<UserSearchResult> searchNriUserForVolunteerIntrest(List<Long> intrests) throws AppException {
        List<User> users;
        Pageable pageable = new PageRequest(0, 1000);
        if (intrests == null || intrests.isEmpty()) {
            users = userRepository.searchNriOnly(pageable).getContent();
        } else {
            users = userRepository.searchNriUserForVolunteerIntrest(intrests, pageable).getContent();
        }
        return convertUserList(users);
    }

    @Override
    public List<UserSearchResult> searchGlobalUserForVolunteerIntrest(List<Long> intrests) throws AppException {
        List<User> users;
        Pageable pageable = new PageRequest(0, 2000);
        if(intrests == null || intrests.isEmpty()){

            users = userRepository.findAll(pageable).getContent();
        } else {
            users = userRepository.searchGlobalUserForVolunteerIntrest(intrests, pageable).getContent();
        }
        return convertUserList(users);
    }

    @Override
    public List<UserSearchResult> searchLocationUserForVolunteerIntrest(Long locationId, List<Long> intrests) throws AppException {
        List<User> users;
        Pageable pageable = new PageRequest(0, 2000);
        if (intrests == null || intrests.isEmpty()) {
            users = userRepository.searchLocationUser(locationId, pageable).getContent();
        } else {
            users = userRepository.searchLocationUserForVolunteerIntrest(locationId, intrests, pageable).getContent();
        }
        return convertUserList(users);
    }

    @Override
    public User getUserById(Long userId) throws AppException {
        return userRepository.findOne(userId);
    }

    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) throws AppException {
        LoginAccount loginAccount = loginAccountRepository.getLoginAccountByUserId(userId);
        if (!passwordUtil.checkPassword(oldPassword, loginAccount.getPassword())) {
            throw new AppException("Old password Do not match");
        }
        loginAccount.setPassword(passwordUtil.encryptPassword(newPassword));
        loginAccount = loginAccountRepository.save(loginAccount);
    }

    @Override
    public void updatePersonalDetails(Long userId, UserPersonalDetailBean userPersonalDetailBean) throws AppException {
        User user = userRepository.findOne(userId);
        user.setName(userPersonalDetailBean.getName());
        user.setGender(userPersonalDetailBean.getGender());
        user.setIdentityType(userPersonalDetailBean.getIdentityType());
        user.setIdentityNumber(userPersonalDetailBean.getIdentityNumber());
        user.setDateOfBirth(userPersonalDetailBean.getDateOfBirth());
        user.setFatherName(userPersonalDetailBean.getFatherName());
        user.setMotherName(userPersonalDetailBean.getMotherName());
        user.setNri(userPersonalDetailBean.isNri());
        updateLocations(user, userPersonalDetailBean);

        user = userRepository.save(user);
        sendMemberForIndexing(user);

    }

    private void updateLocations(User user, UserPersonalDetailBean userPersonalDetailBean) {
        updateUserLocation(user, "Living", "AssemblyConstituency", userPersonalDetailBean.getEditUserAssemblyConstituencyLivingId());
        updateUserLocation(user, "Voting", "AssemblyConstituency", userPersonalDetailBean.getEditUserAssemblyConstituencyVotingId());
        updateUserLocation(user, "Living", "ParliamentConstituency", userPersonalDetailBean.getEditUserParliamentConstituencyLivingId());
        updateUserLocation(user, "Voting", "ParliamentConstituency", userPersonalDetailBean.getEditUserParliamentConstituencyVotingId());
        updateUserLocation(user, "Living", "District", userPersonalDetailBean.getEditUserDistrictLivingId());
        updateUserLocation(user, "Voting", "District", userPersonalDetailBean.getEditUserDistrictVotingId());
        updateUserLocation(user, "Living", "State", userPersonalDetailBean.getEditUserStateLivingId());
        updateUserLocation(user, "Voting", "State", userPersonalDetailBean.getEditUserStateVotingId());
        updateUserLocation(user, "Living", "Country", userPersonalDetailBean.getEditUserNriCountryId());
        updateUserLocation(user, "Living", "CountryRegion", userPersonalDetailBean.getEditUserNriCountryRegionId());
        updateUserLocation(user, "Living", "CountryRegionArea", userPersonalDetailBean.getEditUserNriCountryRegionAreaId());
    }
    
    private void updateUserLocation(User user, String userLocationType, String locationType, Long locationId) {
        UserLocation userLocation = userLocationRepository.getUserLocationByUserIdAndLocationTypesAndUserLocationType(user.getId(), userLocationType, locationType);
        if(locationId == null || locationId <= 0 ){
            // delete User Location
            if (userLocation != null) {
                userLocationRepository.delete(userLocation);
            }
        } else {
            Location location = locationRepository.findOne(locationId);
            if(location == null){
            	return;
            }
            if (userLocation == null) {
                userLocation = new UserLocation();
                userLocation.setUser(user);
                userLocation.setUserLocationType(userLocationType);
                userLocation.setLocation(location);
                userLocation = userLocationRepository.save(userLocation);
            } else {
                userLocation.setLocation(location);
                userLocation = userLocationRepository.save(userLocation);
            }
        }
    }

    @Override
    public void updateVolunteerDetails(Long userId, UserVolunteerBean userVolunteerBean) throws AppException {
        User user = userRepository.findOne(userId);
        if (!userVolunteerBean.isEditUserVolunteer()) {
            user.setVolunteer(false);
            return;
        }

        Volunteer volunteer = volunteerRepository.getVolunteersByUserId(userId);
        if (volunteer == null) {
            volunteer = new Volunteer();
        }
        volunteer.setDomainExpertise(userVolunteerBean.getEditUserDomainExpertise());
        volunteer.setEducation(userVolunteerBean.getEditUserEducation());
        volunteer.setEmergencyContactCountryCode(userVolunteerBean.getEditUserEmergencyContactCountryCode());
        volunteer.setEmergencyContactCountryIso2(userVolunteerBean.getEditUserEmergencyContactCountryIso2());
        volunteer.setEmergencyContactName(userVolunteerBean.getEditUserEmergencyContactName());
        volunteer.setEmergencyContactNo(userVolunteerBean.getEditUserEmergencyContactNo());
        volunteer.setEmergencyContactRelation(userVolunteerBean.getEditUserEmergencyContactRelation());
        volunteer.setExistingMemberCountryCode(userVolunteerBean.getEditUserExistingMemberCountryCode());
        volunteer.setExistingMemberCountryIso2(userVolunteerBean.getEditUserExistingMemberCountryIso2());
        volunteer.setExistingMemberEmail(userVolunteerBean.getEditUserExistingMemberEmail());
        volunteer.setExistingMemberMobile(userVolunteerBean.getEditUserExistingMemberMobile());
        volunteer.setExistingMemberName(userVolunteerBean.getEditUserExistingMemberName());
        volunteer.setOffences(userVolunteerBean.getEditUserOffences());
        volunteer.setPastOrganisation(userVolunteerBean.getEditUserPastOrganisation());
        volunteer.setProfessionalBackground(userVolunteerBean.getEditUserProfessionalBackground());

            
        if(volunteer.getInterests() == null){
            volunteer.setInterests(new HashSet<Interest>());
        } else {
            volunteer.getInterests().clear();
        }

        if (userVolunteerBean.getEditUserInterests() != null) {
            for (Long oneInterestId : userVolunteerBean.getEditUserInterests()) {
                Interest oneInterest = interestRepository.findOne(oneInterestId);
                if (oneInterest != null) {
                    volunteer.getInterests().add(oneInterest);
                }
            }
        }
        volunteer = volunteerRepository.save(volunteer);

    }

    private void generateLoginAccountsForAllEmail() {
        List<Email> emails = emailRepository.findAll();
        int totalFailed = 0;
        int totalSuccess = 0;
        for (Email oneEmail : emails) {
            try {
                generateUserLoginAccount(oneEmail.getEmail());
                totalSuccess++;
            } catch (Exception ex) {
                totalFailed++;
                ex.printStackTrace();
            }
        }
        System.out.println("Total Success : " + totalSuccess);
        System.out.println("Total Failed : " + totalFailed);
    }
    final String RANDOM_CHAR = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    final Random random = new Random();
    @Override
    public void generateUserLoginAccount(String emailId) throws AppException {
        if ("all".equals(emailId)) {
            generateLoginAccountsForAllEmail();
            return;
        }
        Email email = emailRepository.getEmailByEmailUp(emailId.toUpperCase());
        if (email == null) {
            throw new AppException("Email Is not registered");
        }
        User user = email.getUser();
        if (user == null) {
            throw new AppException("Email(user) Is not registered");
        }
        LoginAccount loginAccount = loginAccountRepository.getLoginAccountByUserId(user.getId());
        if (loginAccount == null) {
            loginAccount = new LoginAccount();
            loginAccount.setEmail(emailId.toLowerCase());
            String password = generateRandompassword();
            loginAccount.setPassword(passwordUtil.encryptPassword(password));
            loginAccount.setUser(user);
            loginAccount.setUserName(emailId.toLowerCase());
            loginAccount = loginAccountRepository.save(loginAccount);
            sendLoginAccountDetails(loginAccount, password);
        } else {
            //throw new AppException("Login Account already Exists");
        }
    }

    @Override
    public void generateUserLoginAccountForMobile(String mobile) throws AppException {
        Phone phone = phoneRepository.getPhoneByPhoneNumber(mobile);
        generateUserLoginAccountForMobile(phone);
    }

    private void generateUserLoginAccountForMobile(Phone phone) throws AppException {
        if (phone == null) {
            throw new AppException("Mobile Is not registered");
        }
        User user = phone.getUser();
        if (user == null) {
            throw new AppException("Phone(user) Is not registered");
        }
        LoginAccount loginAccount = loginAccountRepository.getLoginAccountByUserId(user.getId());
        if (loginAccount == null) {
            loginAccount = new LoginAccount();
            String password = generateRandompassword();
            loginAccount.setPassword(passwordUtil.encryptPassword(password));
            loginAccount.setUser(user);
            loginAccount.setUserName(phone.getPhoneNumber());
            loginAccount = loginAccountRepository.save(loginAccount);
            sendLoginAccountDetails(loginAccount, password);
        } else {
            throw new AppException("Login Account already Exists");
        }
    }
    private String generateUserLoginAccountForMobileAndMembershipId(Phone phone, String membershipid) throws AppException {
    	String password = null;
        if (phone == null) {
            throw new AppException("Mobile Is not registered");
        }
        User user = phone.getUser();
        if (user == null) {
            throw new AppException("Phone(user) Is not registered");
        }
        LoginAccount loginAccount = loginAccountRepository.getLoginAccountByUserId(user.getId());
        if (loginAccount == null) {
            loginAccount = new LoginAccount();
            password = generateRandompassword();
            loginAccount.setPassword(passwordUtil.encryptPassword(password));
            loginAccount.setUser(user);
            loginAccount.setUserName(membershipid);
            loginAccount = loginAccountRepository.save(loginAccount);
            //sendLoginAccountDetails(loginAccount, password);
        }
        return password;
    }


    private void sendLoginAccountDetails(LoginAccount loginAccount, String password) throws AppException {
        StringBuilder sb = new StringBuilder();
        sb.append("Hello " + loginAccount.getUser().getName());
        sb.append("<br>");
        sb.append("<p>Your account at <a href=\"http://www.swarajabhiyan.org\">www.swarajabhiyan.org</a> is ready to use. Click on Sign in at the top of the page and enter following details.");
        sb.append("<br>");
        sb.append("user name : " + loginAccount.getUserName());
        sb.append("<br>");
        sb.append("password  : " + password);
        sb.append("</p><br>");
        sb.append("<br>");
        sb.append("<br>");
        sb.append("<p><b>Once Loged in, please visit 'My Account' Section from top of the page and make sure your details are updated correctly.</b></p>");
        sb.append("<br>");
        sb.append("<br>Thanks");
        sb.append("<br>Swaraj Abhiyan Team ");
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
        emailManager.sendEmail(loginAccount.getEmail(), "Registration", regsitrationEmailId, "Your Swaraj Abhiyan Account is ready", contentWithOutHtml, sb.toString());

    }

    private String generateRandompassword() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            sb.append(RANDOM_CHAR.charAt(random.nextInt(RANDOM_CHAR.length())));
        }
        return sb.toString();
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

    @Override
    public void updatePassword(String email, String newPassword, String token) throws AppException {
        PasswordResetRequest passwordResetRequest = passwordResetRequestRepository.getPasswordResetRequestByToken(token);
        if (passwordResetRequest == null) {
            throw new AppException("Invalid Token");
        }
        if (passwordResetRequest.getValidTill().getTime() < System.currentTimeMillis()) {
            passwordResetRequestRepository.delete(passwordResetRequest);
            throw new AppException("Token expired, please request again using forgot password option");
        }
        if (!passwordResetRequest.getUserName().equals(email.toLowerCase())) {
            throw new AppException("Invalid Token");
        }

        LoginAccount loginAccount = loginAccountRepository.findOne(passwordResetRequest.getLoginAccountId());
        loginAccount.setPassword(passwordUtil.encryptPassword(newPassword));
        loginAccount = loginAccountRepository.save(loginAccount);
        passwordResetRequestRepository.delete(passwordResetRequest);
    }

    private void confirmAllEmail() {
        List<Email> emails = emailRepository.findAll();
        int totalFailed = 0;
        int totalSuccess = 0;
        for (Email oneEmail : emails) {
            if (oneEmail.isConfirmed()) {
                continue;
            }
            try {
                sendEmailConfirmtionEmail(oneEmail.getEmail());
                totalSuccess++;
            } catch (Exception ex) {
                totalFailed++;
                ex.printStackTrace();
            }
        }
        System.out.println("Total Success : " + totalSuccess);
        System.out.println("Total Failed : " + totalFailed);
    }
    @Override
    public void sendMembershipConfirmtionEmail(String emailId) throws AppException {
        if ("all".equals(emailId)) {
            //confirmAllEmail();
            return;
        }
        Email email = emailRepository.getEmailByEmailUp(emailId.toUpperCase());
        if (email == null) {
            throw new AppException("No accounts exists for email " + emailId);
        }
        EmailConfirmationRequest emailConfirmationRequest = emailConfirmationRequestRepository.getEmailConfirmationRequestByEmail(emailId.toLowerCase());
        if (emailConfirmationRequest == null) {
            emailConfirmationRequest = new EmailConfirmationRequest();
        }
        emailConfirmationRequest.setEmail(emailId.toLowerCase());
        emailConfirmationRequest.setToken(UUID.randomUUID().toString());
        emailConfirmationRequest = emailConfirmationRequestRepository.save(emailConfirmationRequest);
        sendMembershipConfirmationEmail(emailId.toLowerCase(), email.getUser(), emailConfirmationRequest);

    }
    private void sendMembershipConfirmationEmail(String emailId, User user,EmailConfirmationRequest emailConfirmationRequest) throws AppException {
        String emailValidationUrl = "http://www.swarajabhiyan.org/email/verify?email=" + emailId + "&token=" + emailConfirmationRequest.getToken();
        StringBuilder sb = new StringBuilder();
        sb.append("Hello "+user.getName());
        sb.append("<br>");
        sb.append("<br>");
        sb.append("<p>Thankyou for registering at <a href=\"http://www.swarajabhiyan.org\">Swaraj Abhiyan</a> and becoming its valuable member</p>");
        sb.append("<br>");
        sb.append("<p>Your Membership ID is : "+ user.getMembershipNumber()+"</p>");
        sb.append("<br>");
        sb.append("<p>Also as part of registration process you need to validate your email by clicking <a href=\"" + emailValidationUrl + "\" >here</a> or copy following url and open it in a browser.</p>");
        sb.append("<br>");
        sb.append("<p>" + emailValidationUrl + "</p>");
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
        emailManager.sendEmail(emailId, "Member Registration", regsitrationEmailId, "Welcome to Swaraj Abhiyan", contentWithOutHtml, sb.toString());

    }
    @Override
    public void sendEmailConfirmtionEmail(String emailId) throws AppException {
        if ("all".equals(emailId)) {
            confirmAllEmail();
            return;
        }
        Email email = emailRepository.getEmailByEmailUp(emailId.toUpperCase());
        if (email == null) {
            throw new AppException("No accounts exists for email " + emailId);
        }
        EmailConfirmationRequest emailConfirmationRequest = emailConfirmationRequestRepository.getEmailConfirmationRequestByEmail(emailId.toLowerCase());
        if (emailConfirmationRequest == null) {
            emailConfirmationRequest = new EmailConfirmationRequest();
        }
        emailConfirmationRequest.setEmail(emailId.toLowerCase());
        emailConfirmationRequest.setToken(UUID.randomUUID().toString());
        emailConfirmationRequest = emailConfirmationRequestRepository.save(emailConfirmationRequest);
        sendEmailConfirmationEmail(emailId.toLowerCase(), emailConfirmationRequest);

    }

    private void sendEmailConfirmationEmail(String emailId, EmailConfirmationRequest emailConfirmationRequest) throws AppException {
        String emailValidationUrl = "http://www.swarajabhiyan.org/email/verify?email=" + emailId + "&token=" + emailConfirmationRequest.getToken();
        StringBuilder sb = new StringBuilder();
        sb.append("Hello ");
        sb.append("<br>");
        sb.append("<br>");
        sb.append("<p>Thankyou for registering at <a href=\"http://www.swarajabhiyan.org\">www.swarajabhiyan.org</a></p>");
        sb.append("<br>");
        sb.append("<p>As part of registration process you need to validate your email by clicking <a href=\"" + emailValidationUrl + "\" >here</a> or copy following url and open it in a browser.</p>");
        sb.append("<br>");
        sb.append("<p>" + emailValidationUrl + "</p>");
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
        emailManager.sendEmail(emailId, "Registration", regsitrationEmailId, "Swaraj Abhiyan Email Verification", contentWithOutHtml, sb.toString());

    }

    @Override
    public void confirmEmail(String emailId, String token) throws AppException {

        EmailConfirmationRequest emailConfirmationRequest = emailConfirmationRequestRepository.getEmailConfirmationRequestByEmail(emailId);
        if (emailConfirmationRequest == null) {
            throw new AppException("Invalid Request - 1001");
        }
        if (!emailConfirmationRequest.getToken().equals(token)) {
            throw new AppException("Invalid Request - 1002");
        }
        Email email = emailRepository.getEmailByEmailUp(emailId.toUpperCase());
        if (email == null) {
            throw new AppException("Invalid Request - 1003");
        }
        email.setConfirmationType(ConfirmationType.VIA_EMAIL_VERIFICATION_FLOW);
        email.setConfirmed(true);
        email.setConfirmationDate(new Date());
        email = emailRepository.save(email);
        emailConfirmationRequestRepository.delete(emailConfirmationRequest);
    }

    @Override
    public void updateUserProfilePic(Long userid, String photo) throws AppException {
        updateUserProfilePicPrivate(userid, photo);
    }

    public User updateUserProfilePicPrivate(Long userid, String photo) throws AppException {
        User user = userRepository.findOne(userid);
        user.setProfilePic(photo);
        return user;
    }

    @Override
    public User registerOnlineMember(Long loggedInUserId, String mobileNumber, String name, String amount, String paymentMode, String transactionId, String fees) throws AppException {
    	User user = userRepository.findOne(loggedInUserId);
    	createOnlineUserMembership(user, "Online", transactionId, amount);
    	return user;
    }
    @Override
    public User registerIvrMember(String mobileNumber, String name, String gender, String amount, String paymentMode, String state, String district, String msg) throws AppException {
        String countryCode = "91";
        Phone phone = phoneRepository.getPhoneByPhoneNumberAndCountryCode(mobileNumber, countryCode);
        if (phone == null) {
            phone = new Phone();
            phone.setCountryCode(countryCode);
            phone.setPhoneNumber(mobileNumber);
            phone.setPhoneType(PhoneType.MOBILE);
            phone = phoneRepository.save(phone);
        }
        User user = phone.getUser();
        if (user == null) {
            user = new User();
            user.setName(name);
            if ("M".equalsIgnoreCase(gender)) {
                user.setGender("Male");
            }
            if ("F".equalsIgnoreCase(gender)) {
                user.setGender("Female");
            }
            user.setCreationType(CreationType.IVR);
            user.setMember(true);
            user = userRepository.save(user);

            phone.setUser(user);
        }
        //
        user.setIvrState(state);
        user.setIvrDistrict(district);
        if(!StringUtils.isBlank(state)){
        	Location stateLocation = locationRepository.getLocationByNameUpAndLocationTypeId(state.toUpperCase(), 4L);
        	addUserLocation(user, stateLocation, "Living");
        	addUserLocation(user, stateLocation, "Voting");
        }
        if (StringUtils.isBlank(user.getSmsMessage())) {
            user.setSmsMessage(msg);
        }
        // If user existed before just make him
        Membership membership = membershipRepository.getMembershipByUserId(user.getId());
        if(membership == null){
        	membership = new Membership();
            membership.setStartDate(new Date());
            membership.setSource("IVR");
            membership.setUser(user);
            
            MembershipTransaction membershipTransaction = new MembershipTransaction();
            membershipTransaction.setMembership(membership);
            membershipTransaction.setSource("IVR");
            Calendar calendar = Calendar.getInstance();
            membershipTransaction.setSourceTransactionId(user.getId()+"_"+amount+"_"+calendar.get(Calendar.YEAR)+(calendar.get(Calendar.MONTH)+1)+calendar.get(Calendar.DATE));
            membershipTransaction.setTransactionDate(new Date());
            membershipTransaction.setAmount(amount);
            membershipTransaction = membershipTransactionRepository.save(membershipTransaction);

        }
        membership.setEndDate(getMembershipEndDate());
        membership = membershipRepository.save(membership);
        membership.setMembershipId(getMembershipId(user, membership));
        user.setMember(true);
        sendMemberForIndexing(user);
        return user;
    }
    
    private String getMembershipId(User user, Membership membership){
    	String membershipId = user.getId().toString();
    	if(user.isNri()){
    		membershipId = "NR"+ membership.getId();
    	}else{
    		List<UserLocation> userLocations = userLocationRepository.getUserLocationByUserId(user.getId());
    		UserLocation livingState = null;
    		UserLocation votingState = null;
    		for(UserLocation oneUserLocation : userLocations){
    			if(oneUserLocation.getLocation().getLocationType().getName().equals("State") ){
    				if(oneUserLocation.getUserLocationType().equals("Living")){
    					livingState = oneUserLocation;
    				}else{
    					votingState = oneUserLocation;
    				}
    			}
    		}
        	if(livingState != null){
            	membershipId = livingState.getLocation().getStateCode() + membership.getId();
        	}else if(votingState != null ){
            	membershipId = votingState.getLocation().getStateCode() + membership.getId();
        	}
    	}
        user.setMembershipNumber(membershipId);
    	return membershipId;
    }
    private Date getMembershipEndDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, 31);
        calendar.set(Calendar.YEAR, 2018);
        calendar.set(Calendar.MONTH, 2);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }
    
    private Membership createOnlineUserMembership(User user, String source, String sourceTransactionId, String fees){
    	MembershipTransaction membershipTransaction = membershipTransactionRepository.getMembershipTransactionBySourceTransactionId(sourceTransactionId);
    	if(membershipTransaction != null){
    		//means we have already processed it, no need to reprocess it
    		return membershipTransaction.getMembership();
    	}
    	// If user existed before just make him
    	Membership membership = membershipRepository.getMembershipByUserId(user.getId());
        Calendar calendar = Calendar.getInstance();
    	if(membership == null){
    		membership = new Membership();
    		membership.setStartDate(new Date());
            membership.setEndDate(getMembershipEndDate());
    	}else{
    		calendar.setTime(membership.getEndDate());
    		calendar.add(Calendar.YEAR, 1);
            membership.setEndDate(calendar.getTime());
    	}
        membership.setSource(source);
        membership.setUser(user);
        membership = membershipRepository.save(membership);
        user.setMember(true);
        membership.setMembershipId(getMembershipId(user, membership));
        
        membershipTransaction = new MembershipTransaction();
        membershipTransaction.setMembership(membership);
        membershipTransaction.setSource(source);
        membershipTransaction.setSourceTransactionId(sourceTransactionId);
        membershipTransaction.setTransactionDate(new Date());
        membershipTransaction.setAmount(fees);
        
        membershipTransaction = membershipTransactionRepository.save(membershipTransaction);
        return membership;
    }
    
    private Membership createOfflineUserMembership(User user, String fees){
    	MembershipTransaction membershipTransaction = null;
    	// If user existed before just make him
    	Membership membership = membershipRepository.getMembershipByUserId(user.getId());
        Calendar calendar = Calendar.getInstance();
    	if(membership == null){
    		membership = new Membership();
    		membership.setStartDate(new Date());
            membership.setEndDate(getMembershipEndDate());
    	}else{
    		calendar.setTime(membership.getEndDate());
    		calendar.add(Calendar.YEAR, 1);
            membership.setEndDate(calendar.getTime());
    	}
        membership.setSource("Admin-Offline");
        membership.setUser(user);
        membership = membershipRepository.save(membership);
        user.setMember(true);
        membership.setMembershipId(getMembershipId(user, membership));
        
        membershipTransaction = new MembershipTransaction();
        membershipTransaction.setMembership(membership);
        membershipTransaction.setSource("Admin-Offline");
        membershipTransaction.setTransactionDate(new Date());
        membershipTransaction.setAmount(fees);
        
        membershipTransaction = membershipTransactionRepository.save(membershipTransaction);
        return membership;
    }

    @Override
    public User uploadUserProfilePic(InputStream fileInputStream, User user, String fileName) throws AppException {
        try {
            String bucketName = "static.swarajabhiyan.org";

            if (!StringUtils.isBlank(user.getProfilePic())) {
                try {
                    awsFileManager.deleteFileFromS3(awsKey, awsSecret, bucketName, user.getProfilePic());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            System.out.println("Uploading File");
            String subdDirectory = "";
            System.out.println("subdDirectory = " + subdDirectory);
            String remoteFileName = "profile/" + staticDataEnv + "/" + user.getId() + "/" + System.currentTimeMillis() + getFileType(fileName);
            awsFileManager.uploadFileToS3(awsKey, awsSecret, bucketName, remoteFileName, fileInputStream, "image/jpeg");
            return updateUserProfilePicPrivate(user.getId(), remoteFileName);
        } catch (Exception e) {
            throw new AppException("Failed to upload photo ");
        }

    }

    private String getFileType(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    @Override
    public UserSearchResultForEdting saveUserFromAdminPanel(UserSearchResultForEdting userSearchResultForEdting) throws AppException {
        User user = userRepository.save(userSearchResultForEdting.getUser());
        userSearchResultForEdting.setUser(user);
        if (!StringUtils.isEmpty(userSearchResultForEdting.getPhone().getPhoneNumber())) {
            Phone existingPhone = phoneRepository.getPhoneByPhoneNumber(userSearchResultForEdting.getPhone().getPhoneNumber());
            if (existingPhone != null && !existingPhone.getUser().getId().equals(user.getId())) {
                throw new AppException("Phone Number already used by other member [" + existingPhone.getUser().getName() + "]");
            }
            Phone phone = userSearchResultForEdting.getPhone();
            phone.setUser(user);
            phone = phoneRepository.save(phone);
        }
        sendMemberForIndexing(user);
        return userSearchResultForEdting;
    }

    @Override
    public void checkUserStatus(List<UserUploadDto> users) throws AppException {
    	Email email = null;
    	Phone phone =  null;
        Phone referenceMobile = null;

        for(UserUploadDto oneUserUploadDto : users){
        	email = null;
        	phone =  null;
        	referenceMobile = null;
        	if (StringUtils.isBlank(oneUserUploadDto.getEmail()) && StringUtils.isBlank(oneUserUploadDto.getPhone().trim()) && StringUtils.isEmpty(oneUserUploadDto.getReferencePhone())) {
            	oneUserUploadDto.setErrorMessage("either email or phone or reference mobile must be provided");
            }
            if(StringUtils.isEmpty(oneUserUploadDto.getName())){
        		oneUserUploadDto.setErrorMessage("Name can not be empty");
            }
            if (!StringUtils.isBlank(oneUserUploadDto.getEmail())) {
                email = emailRepository.getEmailByEmailUp(oneUserUploadDto.getEmail().toUpperCase());
                if (email != null) {
                    oneUserUploadDto.setEmailAlreadyExists(true);
                    oneUserUploadDto.setUserIdForEmail(email.getUserId());
                }
            }
            System.out.println("checking Phone number [" + oneUserUploadDto.getPhone() + "]");
            if (!StringUtils.isBlank(oneUserUploadDto.getPhone().trim())) {
                System.out.println("Get Phone by Phone number [" + oneUserUploadDto.getPhone() + "] and countryCode [91]");
                phone = phoneRepository.getPhoneByPhoneNumberAndCountryCode(oneUserUploadDto.getPhone(), "91");
                System.out.println("Found Phone " + phone);
                if (phone != null) {
                    oneUserUploadDto.setPhoneAlreadyExists(true);
                    oneUserUploadDto.setUserIdForPhone(phone.getUserId());
                }
            }
            if (phone != null && phone.getUser() != null && email != null && email.getUser() != null && phone.getUser().getId() != email.getUser().getId()) {
            	oneUserUploadDto.setErrorMessage("Two Different User already exists for phone " + oneUserUploadDto.getPhone()+ " and email :" + oneUserUploadDto.getEmail());
            }
            if(!StringUtils.isEmpty(oneUserUploadDto.getReferencePhone())){
            	referenceMobile = phoneRepository.getPhoneByPhoneNumberAndCountryCode(oneUserUploadDto.getReferencePhone(), "91");
            	if (referenceMobile == null) {
            		oneUserUploadDto.setErrorMessage("No Such mobile registered :" + oneUserUploadDto.getReferencePhone());
                }
            }
            
        }

    }

    @Override
    public void saveMembers(List<UserUploadDto> users, boolean createUserNamePassword, Location state, Location district, Location pc, Location ac) throws AppException {
        for (UserUploadDto oneUserUploadDto : users) {
            try {
                saveMember(oneUserUploadDto, createUserNamePassword, state, district, pc, ac);
                oneUserUploadDto.setUserCreated(true);
            } catch (Exception ex) {
            	ex.printStackTrace();
                oneUserUploadDto.setErrorMessage("Failed:"+ex.getMessage());
                oneUserUploadDto.setUserCreated(false);
            }
        }
    }
    @Override
    public void saveMember(UserUploadDto oneUserUploadDto, boolean createUserNamePassword, Location state, Location district, Location pc, Location ac) throws AppException {
        Email email = getOrCreateEmail(oneUserUploadDto.getEmail(), false);
        Phone phone = getOrCreateMobile(oneUserUploadDto.getPhone(), "91", "mobile", false);

        if (phone != null && phone.getUser() != null && email != null && email.getUser() != null && phone.getUser().getId() != email.getUser().getId()) {
            throw new AppException("Two Different User already exists for phone " + oneUserUploadDto.getPhone()+ " and email :" + oneUserUploadDto.getEmail());
        }
        
        Phone referenceMobile = null;
        if(!StringUtils.isEmpty(oneUserUploadDto.getReferencePhone())){
        	referenceMobile = phoneRepository.getPhoneByPhoneNumberAndCountryCode(oneUserUploadDto.getReferencePhone(), "91");
        	if (referenceMobile == null) {
                throw new AppException("No Such mobile registered " + oneUserUploadDto.getReferencePhone());
            }
        }
        if(StringUtils.isEmpty(oneUserUploadDto.getName())){
        	throw new AppException("Name can not be empty");
        }

        if (email != null && phone != null) {
            email.setPhone(phone);
        }
        if (email == null && phone == null && referenceMobile == null) {
            throw new AppException("either email or phone or reference mobile must be provided");
        }
        User dbUser = null;
        if(email != null){
        	dbUser = email.getUser();
        }
        if(phone != null){
        	dbUser = phone.getUser();
        }
        if(dbUser == null){
        	dbUser = new User();
            dbUser.setCreationType(CreationType.Admin_Imported_Via_Csv);
        }else{
        	System.out.println("Existing User found");
        }
        dbUser.setName(oneUserUploadDto.getName());
        dbUser.setMember(true);
        if(referenceMobile != null){
        	dbUser.setReferenceUser(referenceMobile.getUser());
        	dbUser.setReferenceMobileNumber(referenceMobile.getPhoneNumber());
        }

        dbUser = userRepository.save(dbUser);
        
        //create Membership
        Membership membership = membershipRepository.getMembershipByUserId(dbUser.getId());
        if(membership == null){
        	membership = new Membership();
            membership.setStartDate(new Date());
            membership.setSource("ADMIN");
            membership.setUser(dbUser);
            
            MembershipTransaction membershipTransaction = new MembershipTransaction();
            membershipTransaction.setMembership(membership);
            membershipTransaction.setSource("ADMIN");
            Calendar calendar = Calendar.getInstance();
            if(StringUtils.isEmpty(oneUserUploadDto.getTxnId())){
            	membershipTransaction.setSourceTransactionId(dbUser.getId()+"_"+50+"_"+calendar.get(Calendar.YEAR)+(calendar.get(Calendar.MONTH)+1)+calendar.get(Calendar.DATE));
            }else{
            	membershipTransaction.setSourceTransactionId(oneUserUploadDto.getTxnId());
            }
            membershipTransaction.setTransactionDate(new Date());
            membershipTransaction.setAmount("50");
            membershipTransaction = membershipTransactionRepository.save(membershipTransaction);
            membership.setEndDate(getMembershipEndDate());
            membership = membershipRepository.save(membership);
        }
        

        addUserLocation(dbUser, state, "Living");
        addUserLocation(dbUser, state, "Voting");
        addUserLocation(dbUser, district, "Living");
        addUserLocation(dbUser, district, "Voting");
        addUserLocation(dbUser, ac, "Living");
        addUserLocation(dbUser, ac, "Voting");
        addUserLocation(dbUser, pc, "Living");
        addUserLocation(dbUser, pc, "Voting");
        
        if (email != null) {
            email.setUser(dbUser);
        	sendMembershipConfirmtionEmail(email.getEmail());
            generateUserLoginAccount(email.getEmail());
        }
        if (phone != null) {
            phone.setUser(dbUser);
        }
        membership.setMembershipId(getMembershipId(dbUser, membership));

        if(email == null && phone != null){
        	String password = generateUserLoginAccountForMobileAndMembershipId(phone, dbUser.getMembershipNumber());
        	if(password != null){
        		Sms sms = new Sms();
            	String message = "Dear ##MemberName##, your membership id for swaraj abhiyan is ##ID## and your password is ##password##. You can login to swarajabhiyan.org to view your details. Thanks, Swaraj Abhiyan.";
            	message = message.replace("##MemberName##", dbUser.getName());
            	message = message.replace("##ID##", dbUser.getMembershipNumber());
            	message = message.replace("##password##", password);
            	sms.setMessage(message);
            	sms.setPhone(phone);
            	sms.setPromotional(false);
            	sms.setStatus("PENDING");
            	sms.setUser(dbUser);
            	smsService.sendSmsAsync(sms);
        	}
        }
        sendMemberForIndexing(dbUser);
    }

    private void addUserLocation(User dbuser, Location location, String userLocationType) {
        if (location == null || location.getId() <= 0) {
            return;
        }
        location = locationRepository.findOne(location.getId());
        UserLocation userLocation = userLocationRepository.getUserLocationByUserIdAndLocationTypesAndUserLocationType(dbuser.getId(), userLocationType, location.getLocationType().getName());
        if(userLocation == null){
        	userLocation = new UserLocation();
        }
        userLocation.setLocation(location);
        userLocation.setUser(dbuser);
        userLocation.setUserLocationType(userLocationType);
        userLocation = userLocationRepository.save(userLocation);
    }

	@Override
	public List<MembershipTransaction> getUserMembershipTransactions(Long userId) throws AppException {
		Membership membership = membershipRepository.getMembershipByUserId(userId);
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
	public User saveOfflineMember(OfflineMember member) throws AppException {
		if(StringUtils.isEmpty(member.getReferenceMobile()) && StringUtils.isEmpty(member.getMobile())){
			throw new AppException("Please provide either reference mobile number or mobile number");
		}
		if(StringUtils.isNotEmpty(member.getReferenceMobile()) && StringUtils.isNotEmpty(member.getMobile())){
			throw new AppException("Please provide either reference mobile number or mobile number");
		}
		
		Phone existingPhone = phoneRepository.getPhoneByPhoneNumberAndCountryCode(member.getReferenceMobile(), "91");
		if(existingPhone == null){
			throw new AppException("No such phone["+member.getReferenceMobile()+"] found");
		}
		
		User newUser = new User();
		newUser.setName(member.getName());
		newUser.setCreationType(CreationType.Admin_Created);
		newUser.setReferenceMobileNumber(member.getReferenceMobile());
		newUser.setReferenceUser(existingPhone.getUser());
		addUserLocation(newUser, member.getSelectedState(), "Living");
        addUserLocation(newUser, member.getSelectedState(), "Voting");
        addUserLocation(newUser, member.getSelectedDistrict(), "Living");
        addUserLocation(newUser, member.getSelectedDistrict(), "Voting");
        addUserLocation(newUser, member.getSelectedAc(), "Living");
        addUserLocation(newUser, member.getSelectedAc(), "Voting");
        addUserLocation(newUser, member.getSelectedPc(), "Living");
        addUserLocation(newUser, member.getSelectedPc(), "Voting");
        if(member.getCreatedBy() != null){
        	newUser.setCreatorId(member.getCreatedBy().getId());
        	newUser.setModifierId(member.getCreatedBy().getId());
        }
        
        newUser = userRepository.save(newUser);
        
        createOfflineUserMembership(newUser, "50.00");
        sendMemberForIndexing(newUser);
        return newUser;
		
	}

	@Override
	public List<UserSearchResult> getUsersCreatedBy(Long userId) throws AppException {
		Pageable pageable = new PageRequest(0, 50);
		Page<User> users = userRepository.searchUserByCreatorId(userId, pageable);
		return convertUsers(users.getContent());
	}
	
	private void sendMemberForIndexing(User user) throws AppException{
		if(user.isMember()){
			userSearchService.sendUserForIndexing(user.getId().toString());
		
		}
	}

	@Override
	public List<String> deleteUserByMemberId(String memberId) throws AppException {
		Membership membership = membershipRepository.getMembershipByMembershipId(memberId);
		if(membership == null){
			throw new AppException("No Membership found with Id "+memberId);
		}
		User user = membership.getUser();
		List<String> returnList = new ArrayList<>();
		returnList.add(deletePhoneRecord(user));
		returnList.add(deleteEmailRecord(user));
		returnList.add(deleteLoginAccountRecord(user));
		returnList.add(deleteSmsRecord(user));
		returnList.add(deleteUserLocationRecord(user));
		returnList.add(deleteMembershipTransactionRecord(membership));
		returnList.add(deleteMembershipRecord(membership));
		returnList.add(deleteUserRecord(user));
		userSearchService.deleteUser(user.getId());
		return returnList;
	}
	private String deleteUserRecord(User user){
		userRepository.delete(user);
		return "Deleted User : "+user.getId()+" ("+user.getName()+")";
	}
	private String deleteLoginAccountRecord(User user){
		LoginAccount loginAccount = loginAccountRepository.getLoginAccountByUserId(user.getId());
		if(loginAccount == null){
			return "No Login Account Deleted";
		}
		loginAccountRepository.delete(loginAccount);
		return "Deleted Login Record Record : "+loginAccount.getId()+", "+loginAccount.getUserName();
	}
	private String deleteUserLocationRecord(User user){
		List<UserLocation> list = userLocationRepository.getUserLocationByUserId(user.getId());
		if(list == null || list.isEmpty()){
			return "No User Location Deleted";
		}
		StringBuilder sb = new StringBuilder("Deleted UserLocation Record : ");
		for(UserLocation oneUserLocation : list){
			sb.append(oneUserLocation.getId());
			sb.append("(");
			sb.append(oneUserLocation.getLocation().getName());
			sb.append(", ");
			sb.append(oneUserLocation.getUserLocationType());
			sb.append(")");
			sb.append(", ");
			userLocationRepository.delete(oneUserLocation);
		}
		return sb.toString();
	}
	private String deleteSmsRecord(User user){
		List<Sms> list = smsRepository.getSmsByUserId(user.getId());
		if(list == null || list.isEmpty()){
			return "No SMS Deleted";
		}
		StringBuilder sb = new StringBuilder("Deleted SMS Record : ");
		for(Sms oneSms : list){
			smsRepository.delete(oneSms);
			sb.append(oneSms.getId());
			sb.append("(");
			sb.append(oneSms.getMessage());
			sb.append(")");
			sb.append(", ");
		}
		return sb.toString();
	}
	private String deleteEmailRecord(User user){
		List<Email> list = emailRepository.getEmailsByUserId(user.getId());
		if(list == null || list.isEmpty()){
			return "No EMAIL Deleted";
		}
		StringBuilder sb = new StringBuilder("Deleted Email Record : ");
		for(Email oneEmail : list){
			emailRepository.delete(oneEmail);
			sb.append(oneEmail.getId());
			sb.append("(");
			sb.append(oneEmail.getEmail());
			sb.append(")");
			sb.append(", ");
		}
		return sb.toString();
	}
	private String deleteMembershipTransactionRecord(Membership membership){
		List<MembershipTransaction> list = membershipTransactionRepository.getMembershipTransactionByMembershipId(membership.getId());
		if(list == null || list.isEmpty()){
			return "No Membership transaction Deleted";
		}
		StringBuilder sb = new StringBuilder("Deleted Membership Transactions : ");
		for(MembershipTransaction oneMemebrshipTransaction : list){
			membershipTransactionRepository.delete(oneMemebrshipTransaction);
			sb.append(oneMemebrshipTransaction.getId());
			sb.append(", ");
		}
		return sb.toString();
	}
	private String deleteMembershipRecord(Membership membership){
		membershipRepository.delete(membership);
		return "Deleted Membership : "+membership.getId()+" ("+membership.getMembershipId()+")";
	}
	private String deletePhoneRecord(User user){
		List<Phone> list = phoneRepository.getPhonesByUserId(user.getId());
		if(list == null || list.isEmpty()){
			return "No Phone Number Deleted";
		}
		StringBuilder sb = new StringBuilder("Deleted Phone Record : ");
		for(Phone onePhone : list){
			phoneRepository.delete(onePhone);
			sb.append(onePhone.getId());
			sb.append("(");
			sb.append(onePhone.getPhoneNumber());
			sb.append(")");
			sb.append(", ");
		}
		return sb.toString();
	}
}
