package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.Membership;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

    Membership getMembershipByUserId(Long userId);
}
