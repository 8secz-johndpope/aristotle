package com.aristotle.core.service;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.PlannedTweet;
import com.aristotle.core.persistance.TwitterAccount;

import java.util.List;

/**
 * Created by sharmar2 on 02/07/2015.
 */
public interface TwitterService {

    List<TwitterAccount> getAllSourceTwitterAccounts() throws AppException;
}
