package com.aristotle.core.service;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.TwitterAccount;
import com.aristotle.core.persistance.repo.TwitterAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by sharmar2 on 02/07/2015.
 */
@Service
@Transactional
public class TwitterServiceImpl implements TwitterService {

    @Autowired
    private TwitterAccountRepository twitterAccountRepository;

    @Override
    public List<TwitterAccount> getAllSourceTwitterAccounts() throws AppException {
        return null;
    }
}
