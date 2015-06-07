package com.aristotle.core.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.aristotle.core.enums.CreationType;
import com.aristotle.core.exception.AppException;
import com.aristotle.core.exception.FieldsAppException;
import com.aristotle.core.persistance.Email;
import com.aristotle.core.persistance.Email.ConfirmationType;
import com.aristotle.core.persistance.Interest;
import com.aristotle.core.persistance.Location;
import com.aristotle.core.persistance.LoginAccount;
import com.aristotle.core.persistance.Phone;
import com.aristotle.core.persistance.Phone.PhoneType;
import com.aristotle.core.persistance.User;
import com.aristotle.core.persistance.UserLocation;
import com.aristotle.core.persistance.Volunteer;
import com.aristotle.core.persistance.repo.EmailRepository;
import com.aristotle.core.persistance.repo.InterestRepository;
import com.aristotle.core.persistance.repo.LocationRepository;
import com.aristotle.core.persistance.repo.LoginAccountRepository;
import com.aristotle.core.persistance.repo.PhoneRepository;
import com.aristotle.core.persistance.repo.UserLocationRepository;
import com.aristotle.core.persistance.repo.UserRepository;
import com.aristotle.core.persistance.repo.VolunteerRepository;
import com.aristotle.core.service.dto.SearchUser;
import com.aristotle.core.service.dto.UserContactBean;
import com.aristotle.core.service.dto.UserRegisterBean;
import com.aristotle.core.service.dto.UserSearchResult;

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

    private final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private PhoneRepository phoneRepository;

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
            for (Long oneInterestId : userRegisterBean.getInterests()) {
                Interest oneInterest = interestRepository.findOne(oneInterestId);
                if (oneInterest != null) {
                    volunteer.getInterests().add(oneInterest);
                }
            }
        }
        // create user login account
        if (!StringUtils.isEmpty(userRegisterBean.getPassword())) {
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
            Pageable pageable = new PageRequest(0, 200);
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

}
