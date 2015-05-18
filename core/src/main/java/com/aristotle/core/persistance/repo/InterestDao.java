package com.aristotle.core.persistance.repo;

import java.util.List;

import com.aristotle.core.persistance.Interest;

public interface InterestDao {

	public abstract Interest saveInterest(Interest interest);

	public abstract Interest getInterestById(Long id);
	
	public abstract List<Interest> getAllInterests();

}