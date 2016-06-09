package com.aristotle.core.service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Interest;
import com.aristotle.core.persistance.InterestGroup;
import com.aristotle.core.persistance.Location;
import com.aristotle.core.persistance.Office;
import com.aristotle.core.persistance.Volunteer;
import com.aristotle.core.persistance.repo.InterestGroupRepository;
import com.aristotle.core.persistance.repo.LocationRepository;
import com.aristotle.core.persistance.repo.OfficeRepository;
import com.aristotle.core.persistance.repo.VolunteerRepository;

@Service
@Transactional(rollbackFor=Exception.class)
public class AppServiceImpl implements AppService {

    @Autowired
    private InterestGroupRepository interestGroupRepository;
    @Autowired
    private VolunteerRepository volunteerRepository;
    @Autowired
    private OfficeRepository officeRepository;
    @Autowired
    private LocationRepository locationRepository;

    @Override
    public List<InterestGroup> getAllInterests() {
        return interestGroupRepository.findAll();
    }

    @Override
    public Volunteer getVolunteerDataForUser(Long userId) throws AppException {
        Volunteer volunteer = volunteerRepository.getVolunteersByUserId(userId);
        if (volunteer == null) {
            return null;
        }
        if (volunteer.getInterests() == null) {
            Set<Interest> interestSet = Collections.emptySet();
            volunteer.setInterests(interestSet);
        } else {
            for (Interest oneInterest : volunteer.getInterests()) {
                oneInterest.getDescription();
            }

        }

        return volunteer;
    }

    @Override
    public List<Office> getLocationOffices(Long locationId) {
        if (locationId == null) {
            return officeRepository.getNationalOffices();
        }
        return officeRepository.getOfficeByLocationId(locationId);
    }

    @Override
    public Office saveOffice(Office office, Long locationId) throws AppException {
        if (locationId == null) {
            office.setNational(true);
        } else {
            Location location = locationRepository.findOne(locationId);
            office.setLocation(location);
        }
        office = officeRepository.save(office);
        return office;
    }

}
