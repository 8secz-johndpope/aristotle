package com.aristotle.core.persistance.repo;

import com.aristotle.core.persistance.Tweet;
import com.aristotle.core.persistance.TwitterApp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TwitterAppRepository extends JpaRepository<TwitterApp, Long> {

}