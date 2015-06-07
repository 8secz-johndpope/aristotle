package com.aristotle.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Interest;
import com.aristotle.core.persistance.InterestGroup;
import com.aristotle.core.persistance.Volunteer;
import com.aristotle.core.persistance.repo.InterestGroupRepository;
import com.aristotle.core.persistance.repo.VolunteerRepository;

@Service
@Transactional
public class AppServiceImpl implements AppService {

    @Autowired
    private InterestGroupRepository interestGroupRepository;
    @Autowired
    private VolunteerRepository volunteerRepository;
    @Override
    public List<InterestGroup> getAllInterests() {
        return interestGroupRepository.findAll();
    }

    @Override
    public Volunteer getVolunteerDataForUser(Long userId) throws AppException {
        Sort sort;
        Volunteer volunteer = volunteerRepository.getVolunteersByUserId(userId);
        if (volunteer == null) {
            return null;
        }
        if (volunteer.getInterests() != null) {
            for (Interest oneInterest : volunteer.getInterests()) {
                oneInterest.getDescription();
            }
        }

        return volunteer;
    }

}
