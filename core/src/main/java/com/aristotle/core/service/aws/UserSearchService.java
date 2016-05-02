package com.aristotle.core.service.aws;

import java.util.List;

import com.aristotle.core.exception.AppException;

import aws.services.cloudsearchv2.search.Hit;

public interface UserSearchService {

    void indexUser(Long userId) throws AppException;

    void indexUsers() throws AppException;
    
    String searchMembers(String query) throws AppException;
    
    void sendUserForIndexing(String userId) throws AppException;

}
