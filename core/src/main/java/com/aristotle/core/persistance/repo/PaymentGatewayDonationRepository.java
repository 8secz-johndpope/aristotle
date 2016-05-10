package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.PaymentGatewayDonation;

public interface PaymentGatewayDonationRepository extends JpaRepository<PaymentGatewayDonation, Long> {

    public PaymentGatewayDonation findByMerchantReferenceNumber(String merchantReferenceNumber);
    /*
	public abstract List<Donation> getDonationsByUserId(Long userId);
	
	public abstract Donation getDonationByDonorId(String donorId);

	public Donation getDonationByTransactionId(String transactionId);
	
	public abstract Double getTotalDonationAmountByLcid(String lcid);
	
	public abstract Integer getTotalDonationCountByLcid(String lcid);
*/

}