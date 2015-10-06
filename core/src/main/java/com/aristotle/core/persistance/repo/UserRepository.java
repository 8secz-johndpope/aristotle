package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.aristotle.core.persistance.User;


public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

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

    @Query("select distinct user from User user, Volunteer vl join vl.interests interests where  interests.id in ?1 and vl.userId=user.id and user.nri=true")
    List<User> searchNriUserForVolunteerIntrest(List<Long> interestIds);

    @Query("select user from User user where user.nri=true")
    List<User> searchNriOnly();

    @Query("select distinct user from User user, Volunteer vl join vl.interests interests where  interests.id in ?1 and vl.userId=user.id")
    List<User> searchGlobalUserForVolunteerIntrest(List<Long> interestIds);

    @Query("select distinct user from User user, UserLocation ul, Volunteer vl join vl.interests interests where interests.id in ?2 and vl.userId=user.id and ul.userId=user.id and ul.locationId=?1")
    List<User> searchLocationUserForVolunteerIntrest(Long locationId, List<Long> interestIds);

    @Query("select distinct user from User user, UserLocation ul where ul.locationId=?1 and ul.userId=user.id")
    List<User> searchLocationUser(Long locationId);
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
