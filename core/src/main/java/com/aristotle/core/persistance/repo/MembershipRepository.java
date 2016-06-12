package com.aristotle.core.persistance.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aristotle.core.persistance.Membership;
import com.aristotle.core.persistance.User;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

    Membership getMembershipByUserId(Long userId);
    
    Membership getMembershipByMembershipId(String membershipId);

    @Query("select distinct membership from Membership membership, UserLocation ul where ul.locationId=?1 and ul.userId=membership.userId")
    Page<Membership> searchLocationMember(Long locationId, Pageable pageable);
    

}

