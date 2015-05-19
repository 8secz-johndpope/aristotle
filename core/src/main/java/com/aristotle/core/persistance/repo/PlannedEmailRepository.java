package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.enums.PostLocationType;
import com.aristotle.core.persistance.PlannedEmail;


public interface PlannedEmailRepository extends JpaRepository<PlannedEmail, Long> {

	public abstract List<PlannedEmail> getPlannedEmailByLocationTypeAndLocationId(PostLocationType postLocationType, Long locationId);
}