package com.aristotle.core.service;

import java.util.List;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.InterestGroup;
import com.aristotle.core.persistance.Office;
import com.aristotle.core.persistance.Volunteer;

public interface AppService {

    List<InterestGroup> getAllInterests();

    Volunteer getVolunteerDataForUser(Long userId) throws AppException;

    List<Office> getLocationOffices(Long locationId) throws AppException;

    Office saveOffice(Office office, Long locationId) throws AppException;
}
