package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aristotle.core.persistance.Membership;
import com.aristotle.core.persistance.MembershipTransaction;

public interface MembershipTransactionRepository extends JpaRepository<MembershipTransaction, Long> {

    MembershipTransaction getMembershipTransactionBySourceTransactionId(String sourceTransactionId);

    List<MembershipTransaction> getMembershipTransactionByMembershipId(Long membershipId);
}
