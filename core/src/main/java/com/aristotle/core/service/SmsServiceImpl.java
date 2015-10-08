package com.aristotle.core.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.GroupPlannedSms;
import com.aristotle.core.persistance.LocationPlannedSms;
import com.aristotle.core.persistance.MobileGroup;
import com.aristotle.core.persistance.PlannedSms;
import com.aristotle.core.persistance.TeamPlannedSms;
import com.aristotle.core.persistance.repo.GroupPlannedSmsRepository;
import com.aristotle.core.persistance.repo.LocationPlannedSmsRepository;
import com.aristotle.core.persistance.repo.MobileGroupRepository;
import com.aristotle.core.persistance.repo.PlannedSmsRepository;
import com.aristotle.core.persistance.repo.TeamPlannedSmsRepository;

@Service
@Transactional
public class SmsServiceImpl implements SmsService {

    @Autowired
    private GroupPlannedSmsRepository groupPlannedSmsRepository;
    @Autowired
    private TeamPlannedSmsRepository teamPlannedSmsRepository;
    @Autowired
    private LocationPlannedSmsRepository locationPlannedSmsRepository;
    @Autowired
    private PlannedSmsRepository plannedSmsRepository;
    @Autowired
    private MobileGroupRepository mobileGroupRepository;

    @Override
    public GroupPlannedSms saveGroupPlannedSms(GroupPlannedSms groupPlannedSms) throws AppException {
        groupPlannedSms = groupPlannedSmsRepository.save(groupPlannedSms);
        return groupPlannedSms;
    }

    @Override
    public TeamPlannedSms saveTeamPlannedSms(TeamPlannedSms teamPlannedSms) throws AppException {
        teamPlannedSms = teamPlannedSmsRepository.save(teamPlannedSms);
        return teamPlannedSms;
    }

    @Override
    public LocationPlannedSms saveLocationPlannedSms(LocationPlannedSms locationPlannedSms) throws AppException {
        locationPlannedSms = locationPlannedSmsRepository.save(locationPlannedSms);
        return locationPlannedSms;
    }

    @Override
    public List<PlannedSms> getAllPlannedSms() throws AppException {
        return plannedSmsRepository.getAllPlannedSms();
    }

    @Override
    public List<MobileGroup> getAllMobileGroups() throws AppException {
        return mobileGroupRepository.getMobileGroups();
    }

}
