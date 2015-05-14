package com.aristotle.core.persistance.repo;

import com.next.aap.core.persistance.Volunteer;

public interface VolunteerDao {

	public abstract Volunteer saveVolunteer(Volunteer volunteer);

	public abstract Volunteer getVolunteerById(Long id);

	public abstract Volunteer getVolunteersByUserId(Long userId);

}