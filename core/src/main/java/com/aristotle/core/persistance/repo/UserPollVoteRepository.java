package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.UserPollVote;

public interface UserPollVoteRepository extends JpaRepository<UserPollVote, Long> {

    public abstract UserPollVote getUserPollVoteByUserIdAndPollQuestionId(Long userId, Long questionId);
	
}