package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aristotle.core.persistance.TwitterTeam;

public interface TwitterTeamRepository extends JpaRepository<TwitterTeam, Long> {

    @Query("select twitterTeam from TwitterTeam twitterTeam join twitterTeam.tweetSource tweetSources where tweetSources.id = ?1")
    List<TwitterTeam> getTwitterTeamsOfSourceTwitterAccount(Long twitterAccountId);

    TwitterTeam getTwitterTeamByUrl(String url);
}
