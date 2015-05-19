package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.enums.PostLocationType;
import com.aristotle.core.persistance.PlannedSms;


public interface PlannedSmsRepository extends JpaRepository<PlannedSms, Long> {

	public abstract List<PlannedSms> getPlannedSmsByLocationTypeAndLocationId(PostLocationType postLocationType, Long locationId);
}