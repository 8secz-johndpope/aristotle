package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.FacebookPost;

public interface FacebookPostRepository extends JpaRepository<FacebookPost, Long> {

    public FacebookPost getFacebookPostByPlannedFacebookPostIdAndFacebookAccountId(Long plannedFacebookPostId, Long facebookAccountId);
	
    public List<FacebookPost> getFacebookPostByFacebookAccountId(Long facebookAccountId);

}