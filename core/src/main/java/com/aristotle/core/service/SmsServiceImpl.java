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
import com.aristotle.core.persistance.Team;
import com.aristotle.core.persistance.TeamPlannedSms;
import com.aristotle.core.persistance.repo.GroupPlannedSmsRepository;
import com.aristotle.core.persistance.repo.LocationPlannedSmsRepository;
import com.aristotle.core.persistance.repo.MobileGroupRepository;
import com.aristotle.core.persistance.repo.PlannedSmsRepository;
import com.aristotle.core.persistance.repo.TeamPlannedSmsRepository;
import com.aristotle.core.persistance.repo.TeamRepository;

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
    @Autowired
    private TeamRepository teamRepository;


    @Override
    public GroupPlannedSms saveGroupPlannedSms(GroupPlannedSms groupPlannedSms) throws AppException {
        if (groupPlannedSms.getMobileGroup() == null) {
            throw new AppException("Please select a Group");
        }
        MobileGroup mobileGroup = mobileGroupRepository.findOne(groupPlannedSms.getMobileGroup().getId());
        if (mobileGroup == null) {
            throw new AppException("Please select a Group");
        }
        groupPlannedSms.setMobileGroup(mobileGroup);
        groupPlannedSms = groupPlannedSmsRepository.save(groupPlannedSms);
        return groupPlannedSms;
    }

    @Override
    public TeamPlannedSms saveTeamPlannedSms(TeamPlannedSms teamPlannedSms) throws AppException {
        if (teamPlannedSms.getTeam() == null) {
            throw new AppException("Please select a team");
        }
        Team team = teamRepository.findOne(teamPlannedSms.getTeam().getId());
        if (team == null) {
            throw new AppException("Please select a team");
        }
        teamPlannedSms.setTeam(team);
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
