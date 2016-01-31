package com.aristotle.core.service.aws;

import com.aristotle.core.exception.AppException;

public interface DonationSearchService {

    void indexDonation(Long donationId) throws AppException;

    void indexDonations(int pageNumber, int pageSize) throws AppException;

}
