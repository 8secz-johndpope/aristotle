package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.ContentTweet;

public interface ContentTweetRepository extends JpaRepository<ContentTweet, Long> {

    public abstract List<ContentTweet> getAllContentTweets();

}