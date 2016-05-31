package com.aristotle.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.LoginAccount;
import com.aristotle.core.persistance.User;
import com.aristotle.core.persistance.repo.LoginAccountRepository;
import com.aristotle.core.service.PasswordUtil;

@Service
public class MemberServiceImpl implements MemberService{

	@Autowired
	private LoginAccountRepository loginAccountRepository;
	@Autowired
	private PasswordUtil passwordUtil;
	
	@Override
	public User login(String userName, String password) throws AppException {
        LoginAccount loginAccount = loginAccountRepository.getLoginAccountByUserName(userName.toLowerCase());
        if (loginAccount == null) {
            throw new AppException("Invalid user name/password");
        }
        if (!passwordUtil.checkPassword(password, loginAccount.getPassword())) {
            throw new AppException("Invalid user name/password");
        }
        return loginAccount.getUser();
    }

}
