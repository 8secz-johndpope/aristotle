package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.EmailConfirmationRequest;

public interface EmailConfirmationRequestRepository extends JpaRepository<EmailConfirmationRequest, Long> {

    EmailConfirmationRequest getPasswordResetRequestByToken(String token);

    EmailConfirmationRequest getPasswordResetRequestByEmail(String email);
}
