package com.aristotle.core.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Email;
import com.aristotle.core.persistance.Location;
import com.aristotle.core.persistance.Phone;
import com.aristotle.core.persistance.Team;
import com.aristotle.core.persistance.TeamMember;
import com.aristotle.core.persistance.User;
import com.aristotle.core.persistance.repo.EmailRepository;
import com.aristotle.core.persistance.repo.LocationRepository;
import com.aristotle.core.persistance.repo.PhoneRepository;
import com.aristotle.core.persistance.repo.TeamMemberRepository;
import com.aristotle.core.persistance.repo.TeamRepository;
import com.aristotle.core.persistance.repo.UserRepository;

@Service
@Transactional
public class TeamServiceImpl implements TeamService {

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TeamMemberRepository teamMemberRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private PhoneRepository phoneRepository;

    @Override
    public Team saveTeam(Team team, Long locationId) throws AppException {
        team = teamRepository.save(team);
        addLocationToTeam(team, locationId);
        return team;
    }

    @Override
    public TeamMember saveTeamMember(String emailIdOrPhoneNumber, String post, Long teamId) throws AppException {
        Email email = emailRepository.getEmailByEmailUp(emailIdOrPhoneNumber.toUpperCase());
        User user = null;
        if(email == null){
            Phone phone = phoneRepository.getPhoneByPhoneNumber(emailIdOrPhoneNumber);
            if (phone != null) {
                user = phone.getUser();
            }
        } else {
            user = email.getUser();
        }

        if (user == null) {
            throw new AppException("No Registered user found with email/mobile [" + emailIdOrPhoneNumber + "]");
        }


        Team team = teamRepository.findOne(teamId);
        TeamMember teamMember = new TeamMember();
        teamMember.setPost(post);
        teamMember.setTeam(team);
        teamMember.setUser(user);
        teamMember = teamMemberRepository.save(teamMember);
        return teamMember;
    }

    @Override
    public Team getTeamByUrl(String url) throws AppException {
        return teamRepository.getTeamByUrl(url);
    }

    @Override
    public List<TeamMember> getTeamMembersByTeamId(Long teamId) throws AppException {
        return teamMemberRepository.getTeamMembersByTeamId(teamId);
    }

    @Override
    public List<TeamMember> getTeamMembersByUserId(Long userId) throws AppException {
        return teamMemberRepository.getTeamMembersByUserId(userId);
    }

    private void addLocationToTeam(Team team, Long locationId) {
        if (locationId == null || locationId <= 0) {
            return;
        }
        Location location = locationRepository.findOne(locationId);
        if (location == null) {
            return;
        }
        if (team.getLocations() == null) {
            team.setLocations(new HashSet<Location>());
        }
        team.getLocations().add(location);
    }

    @Override
    public List<Team> getAllGlobalTeams() throws AppException {
        return teamRepository.getGlobalTeams();
    }

    @Override
    public List<Team> getLocationTeams(Set<Long> locationIds) throws AppException {
        return teamRepository.getLocationTeams(locationIds);
    }

    @Override
    public void deleteTeamMember(Long teamMemberId) throws AppException {
        teamMemberRepository.delete(teamMemberId);

    }

}
