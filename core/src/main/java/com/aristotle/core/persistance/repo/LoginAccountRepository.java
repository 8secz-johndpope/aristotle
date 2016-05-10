package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.LoginAccount;

public interface LoginAccountRepository extends JpaRepository<LoginAccount, Long> {

    LoginAccount getLoginAccountByUserName(String userName);

    LoginAccount getLoginAccountByUserId(Long userId);
}
