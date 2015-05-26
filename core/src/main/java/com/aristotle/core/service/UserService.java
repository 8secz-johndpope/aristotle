package com.aristotle.core.service;

import java.util.List;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.service.dto.SearchUser;
import com.aristotle.core.service.dto.UserContactBean;
import com.aristotle.core.service.dto.UserRegisterBean;
import com.aristotle.core.service.dto.UserSearchResult;

public interface UserService {

    void registerUserQuick(UserContactBean userContactBean) throws AppException;

    void registerUser(UserRegisterBean userRegisterBean) throws AppException;

    List<UserSearchResult> searchUsers(SearchUser searchUser) throws AppException;
}
