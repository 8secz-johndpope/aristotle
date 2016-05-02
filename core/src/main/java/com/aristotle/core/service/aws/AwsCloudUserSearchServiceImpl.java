package com.aristotle.core.service.aws;

import static com.aristotle.core.service.aws.UserDocumentField.ADDRESS_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.CREATION_TYPE_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.DATE_OF_BIRTH_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.DONATION_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.DONOR_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.EMAIL_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.FACEBOOK_ACCOUNT_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.FATHER_NAME_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.ID_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.INTEREST_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.LIVING_AC_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.LIVING_AC_ID_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.LIVING_DISTRICT_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.LIVING_DISTRICT_ID_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.LIVING_PC_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.LIVING_PC_ID_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.LIVING_STATE_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.LIVING_STATE_ID_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.MEMBER_END_DATE_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.MEMBER_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.MEMBER_START_DATE_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.MOTHER_NAME_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.NAME_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.NRI_COUNTRY_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.NRI_COUNTRY_ID_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.NRI_COUNTRY_REGION_AREA_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.NRI_COUNTRY_REGION_AREA_ID_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.NRI_COUNTRY_REGION_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.NRI_COUNTRY_REGION_ID_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.NRI_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.PHONE_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.PROFILE_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.PROFILE_PIC_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.TWITTER_ACCOUNT_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.VOTER_ID_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.VOTING_AC_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.VOTING_AC_ID_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.VOTING_DISTRICT_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.VOTING_DISTRICT_ID_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.VOTING_PC_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.VOTING_PC_ID_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.VOTING_STATE_FIELD;
import static com.aristotle.core.service.aws.UserDocumentField.VOTING_STATE_ID_FIELD;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.omg.CORBA.portable.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aws.services.cloudsearchv2.AmazonCloudSearchInternalServerException;
import aws.services.cloudsearchv2.AmazonCloudSearchRequestException;
import aws.services.cloudsearchv2.documents.AmazonCloudSearchAddRequest;
import aws.services.cloudsearchv2.search.AmazonCloudSearchQuery;
import aws.services.cloudsearchv2.search.AmazonCloudSearchResult;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Donation;
import com.aristotle.core.persistance.Email;
import com.aristotle.core.persistance.FacebookAccount;
import com.aristotle.core.persistance.Interest;
import com.aristotle.core.persistance.Membership;
import com.aristotle.core.persistance.Phone;
import com.aristotle.core.persistance.TwitterAccount;
import com.aristotle.core.persistance.User;
import com.aristotle.core.persistance.UserLocation;
import com.aristotle.core.persistance.repo.MembershipRepository;
import com.aristotle.core.persistance.repo.UserLocationRepository;
import com.aristotle.core.persistance.repo.UserRepository;
import com.google.gson.JsonObject;

@Service
@Transactional
public class AwsCloudUserSearchServiceImpl extends AwsCloudBaseSearchService implements UserSearchService {


    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserLocationRepository userLocationRepository;

    @Autowired
    private MembershipRepository membershipRepository;
    
    @Autowired
    private QueueService queueService;


    @Autowired
    public AwsCloudUserSearchServiceImpl(@Value("${user_search_end_point}") String searchEndpoint, @Value("${user_document_end_point}") String userDocumentEndpoint,
            @Value("${user_cloud_search_domain_name}") String userDomainName) {
        super(searchEndpoint, userDocumentEndpoint, userDomainName);
    }


    @Override
    protected void ensureFieldIndexes() {

        ensureTextIndex(NAME_FIELD, true, true, false);
        ensureTextIndex(ID_FIELD, true, false, false);
        ensureTextIndex(FATHER_NAME_FIELD, true, false, false);
        ensureTextIndex(MOTHER_NAME_FIELD, true, false, false);
        ensureTextIndex(ADDRESS_FIELD, true, false, false);
        ensureTextIndex(CREATION_TYPE_FIELD, true, false, false);
        ensureDateIndex(DATE_OF_BIRTH_FIELD, true, true, true);
        ensureTextIndex(NRI_FIELD, true, false, false);
        ensureTextIndex(NRI_COUNTRY_FIELD, true, false, false);
        ensureTextIndex(NRI_COUNTRY_ID_FIELD, true, false, false);
        ensureTextIndex(NRI_COUNTRY_REGION_FIELD, true, false, false);
        ensureTextIndex(NRI_COUNTRY_REGION_ID_FIELD, true, false, false);
        ensureTextIndex(NRI_COUNTRY_REGION_AREA_FIELD, true, false, false);
        ensureTextIndex(NRI_COUNTRY_REGION_AREA_ID_FIELD, true, false, false);
        ensureTextIndex(LIVING_STATE_FIELD, true, false, false);
        ensureTextIndex(LIVING_STATE_ID_FIELD, true, false, false);
        ensureTextIndex(LIVING_DISTRICT_FIELD, true, false, false);
        ensureTextIndex(LIVING_DISTRICT_ID_FIELD, true, false, false);
        ensureTextIndex(LIVING_AC_FIELD, true, false, false);
        ensureTextIndex(LIVING_AC_ID_FIELD, true, false, false);
        ensureTextIndex(LIVING_PC_FIELD, true, false, false);
        ensureTextIndex(LIVING_PC_ID_FIELD, true, false, false);
        
        ensureTextIndex(VOTING_STATE_FIELD, true, false, false);
        ensureTextIndex(VOTING_STATE_ID_FIELD, true, false, false);
        ensureTextIndex(VOTING_DISTRICT_FIELD, true, false, false);
        ensureTextIndex(VOTING_DISTRICT_ID_FIELD, true, false, false);
        ensureTextIndex(VOTING_AC_FIELD, true, false, false);
        ensureTextIndex(VOTING_AC_ID_FIELD, true, false, false);
        ensureTextIndex(VOTING_PC_FIELD, true, false, false);
        ensureTextIndex(VOTING_PC_ID_FIELD, true, false, false);
        ensureTextIndex(PROFILE_FIELD, true, false, false);
        ensureTextArrayIndex(FACEBOOK_ACCOUNT_FIELD, true, false);
        ensureTextArrayIndex(TWITTER_ACCOUNT_FIELD, true, false);
        ensureTextArrayIndex(DONATION_FIELD, true, false);
        ensureTextArrayIndex(EMAIL_FIELD, true, false);
        ensureTextArrayIndex(PHONE_FIELD, true, false);
        ensureTextArrayIndex(INTEREST_FIELD, true, false);
        ensureTextIndex(PROFILE_PIC_FIELD, true, false, false);
        ensureTextIndex(MEMBER_FIELD, true, false, false);
        ensureTextIndex(DONOR_FIELD, true, false, false);
        ensureTextIndex(VOTER_ID_FIELD, true, false, false);
        ensureTextIndex(MEMBER_START_DATE_FIELD, true, false, false);
        ensureTextIndex(MEMBER_END_DATE_FIELD, true, false, false);

    }

    @Override
    public void indexUser(Long userId) throws AppException {
        User user = userRepository.findOne(userId);
        if(user == null){
            return;
        }
        List<User> users = new ArrayList<>();
        users.add(user);
        indexUsers(users);
    }

    @Override
    public void indexUsers() throws AppException {
        Pageable pageRequest = new PageRequest(0, 1000);
        Page<User> userPageResult = userRepository.searchUserByReindex(pageRequest);
        List<User> users = userPageResult.getContent();
        indexUsers(users);
    }
    
    private void indexUsers(List<User> users) throws AppException {
        if (users.isEmpty()) {
            return;
        }
        List<AmazonCloudSearchAddRequest> addRequests = new ArrayList<>();
        for(User oneUser : users){
            addRequests.add(convertUserToDocument(oneUser));
            oneUser.setReindex(false);
        }
        indexDocuments(addRequests);

    }

    private AmazonCloudSearchAddRequest convertUserToDocument(User user) {
        AmazonCloudSearchAddRequest amazonCloudSearchAddRequest = new AmazonCloudSearchAddRequest();
        amazonCloudSearchAddRequest.id = user.getId().toString();
        amazonCloudSearchAddRequest.version = 1;
        amazonCloudSearchAddRequest.addField(NAME_FIELD, user.getName());
        amazonCloudSearchAddRequest.addField(ID_FIELD, user.getId().toString());
        amazonCloudSearchAddRequest.addField(FATHER_NAME_FIELD, user.getFatherName());
        amazonCloudSearchAddRequest.addField(MOTHER_NAME_FIELD, user.getMotherName());
        amazonCloudSearchAddRequest.addField(ADDRESS_FIELD, user.getAddress());
        if(user.getCreationType() != null){
            amazonCloudSearchAddRequest.addField(CREATION_TYPE_FIELD, user.getCreationType().toString());    
        }
        if(user.getDateOfBirth() != null){
            amazonCloudSearchAddRequest.addField(DATE_OF_BIRTH_FIELD, dateFormat.format(user.getDateOfBirth()));
        }
        List<UserLocation> userLocations = userLocationRepository.getUserLocationByUserId(user.getId());
        if (user.isNri()) {
            amazonCloudSearchAddRequest.addField(NRI_FIELD, "yes");
        } else {
            amazonCloudSearchAddRequest.addField(NRI_FIELD, "no");
        }
        addUserLocationField(userLocations, amazonCloudSearchAddRequest);
        amazonCloudSearchAddRequest.addField(PROFILE_FIELD, user.getProfile());
        addFacebookAccountField(user.getFacebookAccounts(), amazonCloudSearchAddRequest);
        addTwitterAccountField(user.getTwitterAccounts(), amazonCloudSearchAddRequest);
        
        

        // ensureTextArrayIndex(DONATION_FIELD, true, false);
        addEmailField(user.getEmails(), amazonCloudSearchAddRequest);
        addPhoneField(user.getPhones(), amazonCloudSearchAddRequest);
        addInterestField(user.getInterests(), amazonCloudSearchAddRequest);
        amazonCloudSearchAddRequest.addField(PROFILE_PIC_FIELD, user.getProfilePic());
        addMembershipFields(user, amazonCloudSearchAddRequest);

        addDonationField(user.getDonations(), amazonCloudSearchAddRequest);
        amazonCloudSearchAddRequest.addField(VOTER_ID_FIELD, user.getVoterId());
        
        return amazonCloudSearchAddRequest;
    }

    private void addFacebookAccountField(Collection<FacebookAccount> fbAccounts, AmazonCloudSearchAddRequest amazonCloudSearchAddRequest) {
        if(fbAccounts == null || fbAccounts.isEmpty()){
            return;
        }
        List<String> fbAccnts = new ArrayList<>(fbAccounts.size());
        for(FacebookAccount oneFacebookAccount : fbAccounts){
            fbAccnts.add(oneFacebookAccount.getUserName());
        }
        amazonCloudSearchAddRequest.addField(FACEBOOK_ACCOUNT_FIELD, fbAccnts);
    }

    private void addTwitterAccountField(Collection<TwitterAccount> twitterAccounts, AmazonCloudSearchAddRequest amazonCloudSearchAddRequest) {
        if (twitterAccounts == null || twitterAccounts.isEmpty()) {
            return;
        }
        List<String> twAccnts = new ArrayList<>(twitterAccounts.size());
        for (TwitterAccount oneFacebookAccount : twitterAccounts) {
            twAccnts.add(oneFacebookAccount.getScreenName());
        }
        amazonCloudSearchAddRequest.addField(TWITTER_ACCOUNT_FIELD, twAccnts);
    }

    private void addEmailField(Collection<Email> emails, AmazonCloudSearchAddRequest amazonCloudSearchAddRequest) {
        if (emails == null || emails.isEmpty()) {
            return;
        }
        List<String> emailIds = new ArrayList<>(emails.size());
        for (Email oneEmail : emails) {
            emailIds.add(oneEmail.getEmail());
        }
        amazonCloudSearchAddRequest.addField(EMAIL_FIELD, emailIds);
    }

    private void addPhoneField(Collection<Phone> phones, AmazonCloudSearchAddRequest amazonCloudSearchAddRequest) {
        if (phones == null || phones.isEmpty()) {
            return;
        }
        List<String> allPhoneNumbers = new ArrayList<>(phones.size());
        for (Phone onePhone : phones) {
            allPhoneNumbers.add(onePhone.getPhoneNumber());
        }
        amazonCloudSearchAddRequest.addField(PHONE_FIELD, allPhoneNumbers);
    }

    private void addInterestField(Collection<Interest> interests, AmazonCloudSearchAddRequest amazonCloudSearchAddRequest) {
        if (interests == null || interests.isEmpty()) {
            return;
        }
        List<String> allInterests = new ArrayList<>(interests.size());
        for (Interest oneInterest : interests) {
            allInterests.add(oneInterest.getDescription());
        }
        amazonCloudSearchAddRequest.addField(INTEREST_FIELD, allInterests);
    }
    
    private void addMembershipFields(User user, AmazonCloudSearchAddRequest amazonCloudSearchAddRequest) {
        Membership membership = membershipRepository.getMembershipByUserId(user.getId());
        if (membership == null) {
            amazonCloudSearchAddRequest.addField(MEMBER_FIELD, "no");
        } else {
            amazonCloudSearchAddRequest.addField(MEMBER_FIELD, "yes");
            amazonCloudSearchAddRequest.addField(MEMBER_START_DATE_FIELD, dateFormat.format(membership.getStartDate()));
            amazonCloudSearchAddRequest.addField(MEMBER_END_DATE_FIELD, dateFormat.format(membership.getEndDate()));
        }
    }
    

    private void addDonationField(Collection<Donation> donations, AmazonCloudSearchAddRequest amazonCloudSearchAddRequest) {
        
        if (donations == null || donations.isEmpty()) {
            amazonCloudSearchAddRequest.addField(DONOR_FIELD, "no");
        }else{
            amazonCloudSearchAddRequest.addField(DONOR_FIELD, "yes");
        }
        /*
        List<String> fbAccnts = new ArrayList<>(donations.size());
        for (TwitterAccount oneFacebookAccount : donations) {
            fbAccnts.add(oneFacebookAccount.getScreenName());
        }
        amazonCloudSearchAddRequest.addField(TWITTER_ACCOUNT_FIELD, fbAccnts);
        */
    }

    private void addUserLocationField(List<UserLocation> userLocations, AmazonCloudSearchAddRequest amazonCloudSearchAddRequest) {
        for (UserLocation oneUserLocation : userLocations) {
            if (oneUserLocation.getLocation().getLocationType().getName().equalsIgnoreCase("Country")) {
                if (!oneUserLocation.getLocation().getName().equalsIgnoreCase("India")) {
                    amazonCloudSearchAddRequest.addField(NRI_COUNTRY_ID_FIELD, oneUserLocation.getLocation().getId().toString());
                    amazonCloudSearchAddRequest.addField(NRI_COUNTRY_FIELD, oneUserLocation.getLocation().getName());

                }
            }
            if (oneUserLocation.getLocation().getLocationType().getName().equalsIgnoreCase("CountryRegion")) {
                addLocations(amazonCloudSearchAddRequest, NRI_COUNTRY_REGION_ID_FIELD, NRI_COUNTRY_REGION_AREA_FIELD, oneUserLocation);
            }
            if (oneUserLocation.getLocation().getLocationType().getName().equalsIgnoreCase("CountryRegion")) {
                addLocations(amazonCloudSearchAddRequest, NRI_COUNTRY_REGION_AREA_ID_FIELD, NRI_COUNTRY_REGION_FIELD, oneUserLocation);
            }
            if (oneUserLocation.getLocation().getLocationType().getName().equalsIgnoreCase("state")) {
                if (oneUserLocation.getUserLocationType().equalsIgnoreCase("voting")) {
                    addLocations(amazonCloudSearchAddRequest, VOTING_STATE_ID_FIELD, VOTING_STATE_FIELD, oneUserLocation);
                } else {
                    addLocations(amazonCloudSearchAddRequest, LIVING_STATE_ID_FIELD, LIVING_STATE_FIELD, oneUserLocation);
                }
            }
            if (oneUserLocation.getLocation().getLocationType().getName().equalsIgnoreCase("district")) {
                if (oneUserLocation.getUserLocationType().equalsIgnoreCase("voting")) {
                    addLocations(amazonCloudSearchAddRequest, VOTING_DISTRICT_ID_FIELD, VOTING_DISTRICT_FIELD, oneUserLocation);
                } else {
                    addLocations(amazonCloudSearchAddRequest, LIVING_DISTRICT_ID_FIELD, LIVING_DISTRICT_FIELD, oneUserLocation);
                }
            }
            if (oneUserLocation.getLocation().getLocationType().getName().equalsIgnoreCase("AssemblyConstituency")) {
                if (oneUserLocation.getUserLocationType().equalsIgnoreCase("voting")) {
                    addLocations(amazonCloudSearchAddRequest, VOTING_AC_ID_FIELD, VOTING_AC_FIELD, oneUserLocation);
                } else {
                    addLocations(amazonCloudSearchAddRequest, LIVING_AC_ID_FIELD, LIVING_AC_FIELD, oneUserLocation);
                }
            }
            if (oneUserLocation.getLocation().getLocationType().getName().equalsIgnoreCase("ParliamentConstituency")) {
                if (oneUserLocation.getUserLocationType().equalsIgnoreCase("voting")) {
                    addLocations(amazonCloudSearchAddRequest, VOTING_PC_ID_FIELD, VOTING_PC_FIELD, oneUserLocation);
                } else {
                    addLocations(amazonCloudSearchAddRequest, LIVING_PC_ID_FIELD, LIVING_PC_FIELD, oneUserLocation);
                }
            }
        }
    }

    private void addLocations(AmazonCloudSearchAddRequest amazonCloudSearchAddRequest, String locationIdField, String locationNameField, UserLocation oneUserLocation) {
        amazonCloudSearchAddRequest.addField(locationIdField, oneUserLocation.getLocation().getId().toString());
        amazonCloudSearchAddRequest.addField(locationNameField, oneUserLocation.getLocation().getName());
    }


	@Override
	public void searchMembers(String query) throws AppException {
		AmazonCloudSearchQuery amazonCloudSearchQuery = new AmazonCloudSearchQuery();
		amazonCloudSearchQuery.query = query;
		amazonCloudSearchQuery.start = 0;
		amazonCloudSearchQuery.size = 20;
		//amazonCloudSearchQuery.setFields("sku_no^11", "title^10", "description^9", "features^8", "specification^8", "categories^7");
		try {
			AmazonCloudSearchResult amazonCloudSearchResult = getAmazonCloudSearchClient().search(amazonCloudSearchQuery);
			System.out.println(amazonCloudSearchResult.toString());
		} catch (IllegalStateException | AmazonCloudSearchRequestException
				| AmazonCloudSearchInternalServerException e) {
			throw new AppException(e);
		}
		
	}


	@Override
	public void sendUserForIndexing(String userId) throws AppException {
		if(userId == null){
			Sort sort = new Sort(new Sort.Order(Direction.ASC, "id"));
			Pageable pageable = new PageRequest(0, 100, sort);
			Page<Membership> members;
			while(true){
				members = membershipRepository.findAll(pageable);	
				if(members.getContent().isEmpty()){
					break;
				}
				for(Membership oneMembership : members.getContent()){
					sendMessage(oneMembership.getUserId().toString());
				}
				pageable = pageable.next();
			}
        }else{
        	sendMessage(userId);
        }		
	}
	private void sendMessage(String userId) throws AppException{
		try {
			queueService.sendRefreshUserMessage(buildUserRefreshMessage(userId));
		} catch (ApplicationException e) {
			throw new AppException(e);
		}
	}
	private String buildUserRefreshMessage(String userId){
    	JsonObject jsonObject = new JsonObject();
    	jsonObject.addProperty("userId", userId);
    	return jsonObject.toString();
    }
}
