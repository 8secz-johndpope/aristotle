package com.aristotle.core.service;

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

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
import com.aristotle.core.persistance.PasswordResetRequest;
import com.aristotle.core.persistance.Phone;
import com.aristotle.core.persistance.Phone.PhoneType;
import com.aristotle.core.persistance.User;
import com.aristotle.core.persistance.UserLocation;
import com.aristotle.core.persistance.Volunteer;
import com.aristotle.core.persistance.repo.EmailConfirmationRequestRepository;
import com.aristotle.core.persistance.repo.EmailRepository;
import com.aristotle.core.persistance.repo.InterestRepository;
import com.aristotle.core.persistance.repo.LocationRepository;
import com.aristotle.core.persistance.repo.LoginAccountRepository;
import com.aristotle.core.persistance.repo.MembershipRepository;
import com.aristotle.core.persistance.repo.PasswordResetRequestRepository;
import com.aristotle.core.persistance.repo.PhoneRepository;
import com.aristotle.core.persistance.repo.UserLocationRepository;
import com.aristotle.core.persistance.repo.UserRepository;
import com.aristotle.core.persistance.repo.VolunteerRepository;
import com.aristotle.core.service.dto.SearchUser;
import com.aristotle.core.service.dto.UserContactBean;
import com.aristotle.core.service.dto.UserPersonalDetailBean;
import com.aristotle.core.service.dto.UserRegisterBean;
import com.aristotle.core.service.dto.UserSearchResult;
import com.aristotle.core.service.dto.UserVolunteerBean;

@Service
@Transactional
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
        if (StringUtils.isEmpty(emailId)) {
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
        if (StringUtils.isEmpty(mobileNumber)) {
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
        try{
        getOrCreateEmail(userContactBean.getEmail());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            getOrCreateMobile(userContactBean.getMobile(), userContactBean.getCountryCode(), "mobile");
        } catch (Exception ex) {
            ex.printStackTrace();
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
        if (StringUtils.isEmpty(userRegisterBean.getPassword())){
            String password = generateRandompassword();
            userRegisterBean.setPassword(password);
        }
        LoginAccount loginAccount = new LoginAccount();
        loginAccount.setUser(dbUser);
        loginAccount.setPassword(passwordUtil.encryptPassword(userRegisterBean.getPassword()));

        if (!StringUtils.isEmpty(userRegisterBean.getEmailId())) {
            loginAccount.setEmail(userRegisterBean.getEmailId().toLowerCase());
            loginAccount.setUserName(userRegisterBean.getEmailId().toLowerCase());
        }
        if (StringUtils.isEmpty(loginAccount.getUserName())) {
            throw new AppException("Login User name/email must be provided");
        }
        loginAccount = loginAccountRepository.save(loginAccount);
        if (email != null) {
            sendEmailConfirmtionEmail(email.getEmail());
        }
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
        if (StringUtils.isEmpty(mobileNumber)) {
            return null;
        }
        if (StringUtils.isEmpty(countryCode)) {
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
        if (mobile.getUser() != null || mobile.isConfirmed()) {
            throwFieldAppException(fieldName, "Mobile already registered");
        }
        return mobile;
    }

    private Email getOrCreateEmail(String emailId) throws AppException {
        if (StringUtils.isEmpty(emailId)) {
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
        if (intrests == null || intrests.isEmpty()) {
            Pageable pageable = new PageRequest(0, 200);
            users = userRepository.findAll(pageable).getContent();
        } else {
            users = userRepository.searchNriUserForVolunteerIntrest(intrests);
        }
        return convertUserList(users);
    }

    @Override
    public List<UserSearchResult> searchGlobalUserForVolunteerIntrest(List<Long> intrests) throws AppException {
        List<User> users;
        if(intrests == null || intrests.isEmpty()){
            Pageable pageable = new PageRequest(0, 2000);
            users = userRepository.findAll(pageable).getContent();
        } else {
            users = userRepository.searchGlobalUserForVolunteerIntrest(intrests);
        }
        return convertUserList(users);
    }

    @Override
    public List<UserSearchResult> searchLocationUserForVolunteerIntrest(Long locationId, List<Long> intrests) throws AppException {
        List<User> users;
        if (intrests == null || intrests.isEmpty()) {
            users = userRepository.searchLocationUser(locationId);
        } else {
            users = userRepository.searchLocationUserForVolunteerIntrest(locationId, intrests);
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
            throw new AppException("Login Account already Exists");
        }


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
        User user = userRepository.findOne(userid);
        user.setProfilePic(photo);
    }

    @Override
    public User registerIvrMember(String mobileNumber, String name, String gender, String amount, String paymentMode) throws AppException {
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
        // If user existed before just make him
        Membership membership = new Membership();
        Calendar calendar = Calendar.getInstance();
        membership.setStartDate(new Date());
        calendar.add(Calendar.YEAR, 1);
        membership.setEndDate(calendar.getTime());
        membership.setSource("IVR");
        membership.setUser(user);
        membership = membershipRepository.save(membership);
        user.setMember(true);
        return user;
    }

}
