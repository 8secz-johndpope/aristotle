package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.Email;

public interface EmailRepository extends JpaRepository<Email, Long> {

    public abstract Email getEmailByEmailUp(String email);

	public abstract List<Email> getEmailsByUserId(Long userId);

}