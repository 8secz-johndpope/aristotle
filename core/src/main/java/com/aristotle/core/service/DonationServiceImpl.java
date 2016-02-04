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
import com.aristotle.core.persistance.Email;
import com.aristotle.core.persistance.IvrDonation;
import com.aristotle.core.persistance.PaymentGatewayDonation;
import com.aristotle.core.persistance.User;
import com.aristotle.core.persistance.repo.DonationRepository;
import com.aristotle.core.persistance.repo.EmailRepository;
import com.aristotle.core.persistance.repo.IvrDonationRepository;
import com.aristotle.core.persistance.repo.PaymentGatewayDonationRepository;

@Service
@Transactional
public class DonationServiceImpl implements DonationService {

    @Autowired
    private IvrDonationRepository ivrDonationRepository;
    @Autowired
    private DonationRepository donationRepository;
    @Autowired
    private PaymentGatewayDonationRepository paymentGatewayDonationRepository;
    @Autowired
    private EmailRepository EmailRepository;

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

    @Override
    public PaymentGatewayDonation saveOnlineDonationFromInstamojo(boolean success, String paymentId, String status, String buyerName, String buyerPhone, String buyerEmail, String amount, String fees,
            Date donationDate)
            throws AppException {
        User user = null;
        if (buyerEmail != null) {
            Email email = EmailRepository.getEmailByEmailUp(buyerEmail.toUpperCase());
            if (email != null) {
                user = email.getUser();
            }
        }

        PaymentGatewayDonation paymentGatewayDonation = paymentGatewayDonationRepository.findByMerchantReferenceNumber(paymentId);
        if (paymentGatewayDonation == null) {
            paymentGatewayDonation = new PaymentGatewayDonation();
            paymentGatewayDonation.setMerchantReferenceNumber(paymentId);
        }
        paymentGatewayDonation.setAmount(Double.parseDouble(amount));
        paymentGatewayDonation.setDonorName(buyerName);
        paymentGatewayDonation.setPaymentGateway("Instamojo");
        paymentGatewayDonation.setDonationDate(new Date());
        paymentGatewayDonation.setDonorEmail(buyerEmail);
        paymentGatewayDonation.setDonorMobile(buyerPhone);
        paymentGatewayDonation.setUser(user);
        paymentGatewayDonation.setDonationDate(donationDate);

        paymentGatewayDonation = paymentGatewayDonationRepository.save(paymentGatewayDonation);

        return paymentGatewayDonation;
    }

    @Override
    public Donation getDonationByPgTransactionId(String pgTransactionId) throws AppException {
        return paymentGatewayDonationRepository.findByMerchantReferenceNumber(pgTransactionId);
    }

}
