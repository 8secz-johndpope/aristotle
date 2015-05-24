package com.aristotle.web.service;

import com.aristotle.core.exception.AppException;
import com.aristotle.web.controller.beans.UserContactBean;
import com.aristotle.web.controller.beans.UserRegisterBean;

public interface UserService {


    void registerUserQuick(UserContactBean userContactBean) throws AppException;

    void registerUser(UserRegisterBean userRegisterBean) throws AppException;

}
