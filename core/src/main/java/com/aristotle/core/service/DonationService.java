package com.aristotle.core.service;

import java.util.Date;
import java.util.List;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Donation;
import com.aristotle.core.persistance.PaymentGatewayDonation;

public interface DonationService {

    Donation saveIvrDonation(String mobile, String name, String amount, String paymentMode, String upid, String adminUpid, String adminMobile, String msg) throws AppException;

    PaymentGatewayDonation saveOnlineDonationFromInstamojo(boolean success, String paymentId, String status, String buyerName, String buyerPhone, String buyerEmail, String amount, String fees,
            Date donationDate)
            throws AppException;

    List<Donation> getDonations(int pageNumber, int pageSize) throws AppException;

    Donation getDonationByPgTransactionId(String pdTransactionId) throws AppException;

    Long getTotalDonation() throws AppException;
}
