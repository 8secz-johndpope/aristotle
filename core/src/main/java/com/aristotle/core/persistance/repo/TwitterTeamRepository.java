package com.aristotle.core.persistance.repo;

import com.aristotle.core.persistance.Candidate;
import com.aristotle.core.persistance.TwitterTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TwitterTeamRepository extends JpaRepository<TwitterTeam, Long> {

}
