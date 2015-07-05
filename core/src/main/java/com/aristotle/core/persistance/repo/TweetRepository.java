package com.aristotle.core.persistance.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aristotle.core.persistance.Tweet;

public interface TweetRepository extends JpaRepository<Tweet, Long> {

    public Tweet getTweetByPlannedTweetIdAndTwitterAccountId(Long plannedTweetId, Long twitterAccountId);

    @Query("select tweet from Tweet tweet where tweet.status = 'Pending' and (tweet.twitterAccount.lastTweetSentTime <= ?1 or tweet.twitterAccount.lastTweetSentTime is null)")
    public List<Tweet> getNextPendingTweet(Date nextMinimumTweetSentDateTime, Pageable pageRequest);
	
    // public abstract List<Tweet> getTweetByUserId(Long userId);

}