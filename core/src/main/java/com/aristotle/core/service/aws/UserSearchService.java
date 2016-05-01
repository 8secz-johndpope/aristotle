package com.aristotle.core.service.aws;

import com.aristotle.core.exception.AppException;

public interface UserSearchService {

    void indexUser(Long userId) throws AppException;

    void indexUsers() throws AppException;
    
    void searchMembers(String query) throws AppException;

}
