package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.FacebookApp;


public interface FacebookAppRepository extends JpaRepository<FacebookApp, Long> {

    public FacebookApp getFacebookAppByAppId(String appId);

}