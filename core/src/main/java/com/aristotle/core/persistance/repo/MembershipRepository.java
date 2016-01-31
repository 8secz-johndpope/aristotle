package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aristotle.core.persistance.Membership;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

    Membership getMembershipByUserId(Long userId);

    @Query("select membership from Membership membership where membership.userId = ?1 and (CURRENT_DATE between membership.startDate and membership.endDate)")
    Membership getCurrentMembershipByUserId(Long userId);
}
