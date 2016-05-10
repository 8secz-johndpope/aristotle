package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.TeamMember;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    public List<TeamMember> getTeamMembersByTeamId(Long teamId);

    public List<TeamMember> getTeamMembersByUserId(Long userId);

}
