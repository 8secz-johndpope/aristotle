package com.aristotle.core.service;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Donation;

public interface DonationService {

    Donation saveIvrDonation(String mobile, String name, String amount, String paymentMode, String upid, String adminUpid, String adminMobile, String msg) throws AppException;
}
