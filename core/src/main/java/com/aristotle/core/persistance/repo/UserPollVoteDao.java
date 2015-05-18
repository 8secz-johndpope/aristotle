package com.aristotle.core.persistance.repo;

import com.aristotle.core.persistance.UserPollVote;

public interface UserPollVoteDao {

	public abstract UserPollVote saveUserPollVote(UserPollVote userPollVote);

	public abstract UserPollVote getUserPollVoteById(Long id);

	public abstract UserPollVote getUserPollVote(Long userId, Long questionId);
	
}