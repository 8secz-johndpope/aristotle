package com.aristotle.core.service;

import java.util.List;
import java.util.Set;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Team;
import com.aristotle.core.persistance.TeamMember;

public interface TeamService {

    public Team saveTeam(Team team, Long locationId) throws AppException;

    public TeamMember saveTeamMember(TeamMember teamMember) throws AppException;

    public Team getTeamByUrl(String url) throws AppException;

    public List<TeamMember> getTeamMembersByTeamId(Long teamId) throws AppException;

    public List<TeamMember> getTeamMembersByUserId(Long userId) throws AppException;

    public List<Team> getAllGlobalTeams() throws AppException;

    public List<Team> getLocationTeams(Set<Long> locationIds) throws AppException;
}
