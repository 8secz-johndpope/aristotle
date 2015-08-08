package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.Donation;

public interface DonationRepository extends JpaRepository<Donation, Long> {

    /*
	public abstract List<Donation> getDonationsByUserId(Long userId);
	
	public abstract Donation getDonationByDonorId(String donorId);

	public Donation getDonationByTransactionId(String transactionId);
	
	public abstract Double getTotalDonationAmountByLcid(String lcid);
	
	public abstract Integer getTotalDonationCountByLcid(String lcid);
*/

}