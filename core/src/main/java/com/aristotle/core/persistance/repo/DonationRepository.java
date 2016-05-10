package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aristotle.core.persistance.Donation;

public interface DonationRepository extends JpaRepository<Donation, Long> {

    @Query("select donation from Donation donation order by donationDate desc")
    public abstract List<Donation> getDonationsOrderByDonationDate(Pageable pageable);


    /*
	public abstract List<Donation> getDonationsByUserId(Long userId);
	
	public abstract Donation getDonationByDonorId(String donorId);

	public Donation getDonationByTransactionId(String transactionId);
	
	public abstract Double getTotalDonationAmountByLcid(String lcid);
	
	public abstract Integer getTotalDonationCountByLcid(String lcid);
*/

}