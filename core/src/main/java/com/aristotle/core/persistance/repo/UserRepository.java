package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.User;


public interface UserRepository extends JpaRepository<User, Long> {

	/**
	 * Search user by Email
	 * @param userEmail
	 * @return user or null if not found
	 */
    // public abstract User getUserByEmail(String userEmail);
	
	/**
	 * Search user by Membership Number
	 * @param userMembershipNumber
	 * @return user or null if not found
	 */
	public abstract User getUserByMembershipNumber(String membershipNumber);


	/**
	 * Search user by Passport Number
	 * @param userPassportNumber
	 * @return user or null if not found
	 */
	public abstract User getUserByPassportNumber(String passportNumber);

	/*
	List<User> searchUserOfAssemblyConstituency(String name,Long livingAcId,Long votingAcId);
	
	
	List<Long> getAllAdminUserForGlobalTreasur();
	
	List<Long> getAllAdminUserForStateTreasure(long stateId);
	
	List<Long> getAllAdminUserForDistrictTreasure(long districtId);
	
	List<Long> getAllAdminUserForAcTreasure(long acId);
	
	List<Long> getAllAdminUserForPcTreasure(long pcId);
	
	List<Long> getAdminUserForGlobalTreasur();
	
	List<Long> getAdminUserForStateTreasure(long stateId);
	
	List<Long> getAdminUserForDistrictTreasure(long districtId);
	
	List<Long> getAdminUserForAcTreasure(long acId);
	
	List<Long> getAdminUserForPcTreasure(long pcId);
	
	LegacyMembership getLegacyMembershipByEmail(String email);
	
	LegacyMembership getLegacyMembershipByMobile(String mobile);

	LegacyMembership getLegacyMembershipsByMembershipNumbers(Long membershipNumber);
	*/
}
