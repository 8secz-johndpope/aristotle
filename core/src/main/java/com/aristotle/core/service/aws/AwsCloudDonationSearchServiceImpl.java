package com.aristotle.core.service.aws;

import static com.aristotle.core.service.aws.DonationDocumentField.ADMIN_MOBILE_NUMBER_FIELD;
import static com.aristotle.core.service.aws.DonationDocumentField.ADMIN_UPID_FIELD;
import static com.aristotle.core.service.aws.DonationDocumentField.AMOUNT_FIELD;
import static com.aristotle.core.service.aws.DonationDocumentField.DONATION_DATE_FIELD;
import static com.aristotle.core.service.aws.DonationDocumentField.DONATION_TYPE_FIELD;
import static com.aristotle.core.service.aws.DonationDocumentField.EMAIL_FIELD;
import static com.aristotle.core.service.aws.DonationDocumentField.ID_FIELD;
import static com.aristotle.core.service.aws.DonationDocumentField.NAME_FIELD;
import static com.aristotle.core.service.aws.DonationDocumentField.PHONE_FIELD;
import static com.aristotle.core.service.aws.DonationDocumentField.UPID_FIELD;
import static com.aristotle.core.service.aws.DonationDocumentField.USER_ID_FIELD;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aws.services.cloudsearchv2.documents.AmazonCloudSearchAddRequest;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Donation;
import com.aristotle.core.persistance.IvrDonation;
import com.aristotle.core.persistance.repo.DonationRepository;
import com.aristotle.core.persistance.repo.MembershipRepository;
import com.aristotle.core.persistance.repo.UserLocationRepository;

@Service
@Transactional
public class AwsCloudDonationSearchServiceImpl extends AwsCloudBaseSearchService implements DonationSearchService {


    @Autowired
    private DonationRepository donationRepository;
    
    @Autowired
    private UserLocationRepository userLocationRepository;

    @Autowired
    private MembershipRepository membershipRepository;


    private DecimalFormat amountFormat = new DecimalFormat("##.##");

    @Autowired
    public AwsCloudDonationSearchServiceImpl(@Value("${donation_search_end_point}") String searchEndpoint, @Value("${donation_document_end_point}") String userDocumentEndpoint,
            @Value("${donation_cloud_search_domain_name}") String userDomainName) {
        super(searchEndpoint, userDocumentEndpoint, userDomainName);
    }


    @Override
    protected void ensureFieldIndexes() {

        ensureTextIndex(NAME_FIELD, true, true, false);
        ensureTextIndex(ID_FIELD, true, false, false);
        ensureTextIndex(USER_ID_FIELD, true, false, false);
        ensureTextIndex(ADMIN_MOBILE_NUMBER_FIELD, true, false, false);
        ensureTextIndex(ADMIN_UPID_FIELD, true, false, false);
        ensureTextIndex(UPID_FIELD, true, false, false);
        ensureDateIndex(DONATION_DATE_FIELD, true, true, true);
        ensureTextIndex(DONATION_TYPE_FIELD, true, false, false);
        ensureDoubleIndex(AMOUNT_FIELD, true, false, false);
        
        ensureTextArrayIndex(EMAIL_FIELD, true, false);
        ensureTextArrayIndex(PHONE_FIELD, true, false);
        
        


    }

    @Override
    public void indexDonation(Long userId) throws AppException {
        Donation user = donationRepository.findOne(userId);
        if(user == null){
            return;
        }
        List<Donation> users = new ArrayList<>();
        users.add(user);
        indexDonations(users);
    }

    @Override
    public void indexDonations(int pageNumber, int pageSize) throws AppException {
        Pageable pageRequest = new PageRequest(pageNumber, pageSize);
        Page<Donation> userPageResult = donationRepository.findAll(pageRequest);
        List<Donation> users = userPageResult.getContent();
        indexDonations(users);
    }
    
    private void indexDonations(List<Donation> donations) throws AppException {
        if (donations.isEmpty()) {
            return;
        }
        List<AmazonCloudSearchAddRequest> addRequests = new ArrayList<>();
        for (Donation oneUser : donations) {
            addRequests.add(convertDonationToDocument(oneUser));
        }
        indexDocuments(addRequests);
    }


    private AmazonCloudSearchAddRequest convertDonationToDocument(Donation donation) {
        AmazonCloudSearchAddRequest amazonCloudSearchAddRequest = new AmazonCloudSearchAddRequest();
        amazonCloudSearchAddRequest.id = donation.getId().toString();
        amazonCloudSearchAddRequest.version = 1;
        amazonCloudSearchAddRequest.addField(NAME_FIELD, donation.getDonorName());
        amazonCloudSearchAddRequest.addField(ID_FIELD, donation.getId().toString());
        if (donation.getDonorEmail() != null) {
            List<String> emailIds = new ArrayList<>(1);
            emailIds.add(donation.getDonorEmail());
            amazonCloudSearchAddRequest.addField(EMAIL_FIELD, emailIds);
        }

        if (donation.getDonorMobile() != null) {
            List<String> phones = new ArrayList<>(1);
            phones.add(donation.getDonorMobile());
            amazonCloudSearchAddRequest.addField(PHONE_FIELD, phones);
        }

        amazonCloudSearchAddRequest.addField(DONATION_TYPE_FIELD, donation.getDonationype());
        amazonCloudSearchAddRequest.addField(DONATION_DATE_FIELD, dateFormat.format(donation.getDonationDate()));
        amazonCloudSearchAddRequest.addField(AMOUNT_FIELD, amountFormat.format(donation.getAmount()));

        addIvrAdminFieldsField(donation, amazonCloudSearchAddRequest);
        
        return amazonCloudSearchAddRequest;
    }

    private void addIvrAdminFieldsField(Donation donation, AmazonCloudSearchAddRequest amazonCloudSearchAddRequest) {
        if (!(donation instanceof IvrDonation)) {
            return;
        }
        IvrDonation ivrDonation = (IvrDonation) donation;
        amazonCloudSearchAddRequest.addField(ADMIN_MOBILE_NUMBER_FIELD, ivrDonation.getAdminMobileNumber());
        amazonCloudSearchAddRequest.addField(ADMIN_UPID_FIELD, ivrDonation.getAdminUpid());
        amazonCloudSearchAddRequest.addField(UPID_FIELD, ivrDonation.getUpid());
    }

}
