package com.aristotle.core.service;

import java.util.List;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Donation;

public interface DonationService {

    Donation saveIvrDonation(String mobile, String name, String amount, String paymentMode, String upid, String adminUpid, String adminMobile, String msg) throws AppException;

    List<Donation> getDonations(int pageNumber, int pageSize) throws AppException;
}
