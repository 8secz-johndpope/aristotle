package com.aristotle.core.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Donation;
import com.aristotle.core.persistance.IvrDonation;
import com.aristotle.core.persistance.repo.DonationRepository;
import com.aristotle.core.persistance.repo.IvrDonationRepository;

@Service
@Transactional
public class DonationServiceImpl implements DonationService {

    @Autowired
    private IvrDonationRepository ivrDonationRepository;
    @Autowired
    private DonationRepository donationRepository;

    @Override
    public Donation saveIvrDonation(String mobile, String name, String amount, String paymentMode, String upid, String adminUpid, String adminMobile, String msg) throws AppException {
        IvrDonation ivrDonation = new IvrDonation();
        ivrDonation.setAmount(Double.parseDouble(amount));
        ivrDonation.setDateCreated(new Date());
        ivrDonation.setDonorMobile(mobile);
        ivrDonation.setDonorName(name);
        ivrDonation.setUpid(upid);
        ivrDonation.setAdminMobileNumber(adminMobile);
        ivrDonation.setAdminUpid(adminUpid);
        ivrDonation.setDonationDate(new Date());
        ivrDonation.setSmsMessage(msg);
        ivrDonation = ivrDonationRepository.save(ivrDonation);
        return ivrDonation;
    }

    @Override
    public List<Donation> getDonations(int pageNumber, int pageSize) throws AppException {
        Pageable pageable = new PageRequest(pageNumber, pageSize);
        return donationRepository.getDonationsOrderByDonationDate(pageable);
    }

}
