package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aristotle.core.enums.PostLocationType;
import com.aristotle.core.persistance.PlannedSms;


public interface PlannedSmsRepository extends JpaRepository<PlannedSms, Long> {

	public abstract List<PlannedSms> getPlannedSmsByLocationTypeAndLocationId(PostLocationType postLocationType, Long locationId);

    @Query("select plannedSms from PlannedSms plannedSms order by postingTime desc")
    public abstract List<PlannedSms> getAllPlannedSms();

    @Query("select plannedSms from PlannedSms plannedSms where current_date > postingTime and status='PENDING' order by postingTime desc")
    public abstract List<PlannedSms> getAllPendingPlannedSms();

}