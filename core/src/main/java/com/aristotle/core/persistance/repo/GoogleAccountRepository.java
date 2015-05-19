package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.GoogleAccount;

public interface GoogleAccountRepository extends JpaRepository<GoogleAccount, Long> {

    public GoogleAccount getGoogleAccountByUserId(Long userId);

    public GoogleAccount getGoogleAccountByGoogleUserId(String googleId);

    // public List<GoogleAccount> getGoogleAccountsAfterId(Long lastId, int pageSize);

}