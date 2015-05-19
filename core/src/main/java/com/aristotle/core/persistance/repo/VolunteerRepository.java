package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.Volunteer;

public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {

	public abstract Volunteer getVolunteersByUserId(Long userId);

}