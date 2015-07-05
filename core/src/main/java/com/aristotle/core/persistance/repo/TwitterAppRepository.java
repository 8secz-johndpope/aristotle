package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.TwitterApp;

public interface TwitterAppRepository extends JpaRepository<TwitterApp, Long> {

}