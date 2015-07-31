package com.aristotle.core.persistance.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aristotle.core.persistance.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {

    public Team getTeamByUrl(String url);

    @Query("select team from News team where team.global=true order by dateCreated desc")
    public List<Team> getGlobalTeams();

    @Query("select team from Team team join team.locations locations where locations.id in ?1 order by team.dateCreated desc")
    public List<Team> getLocationTeams(Set<Long> locationIds);

}
