package com.aristotle.core.persistance;

import com.aristotle.core.enums.PlannedPostStatus;
import com.aristotle.core.enums.PostLocationType;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tweeter_team")
public class TwitterTeam extends BaseEntity{

	@Column(name = "name")
	private String name;
	
	@Column(name = "description")
	private String description;

    @Column(name = "global")
    private String global;

	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name = "twitter_team_tweet_source",
			joinColumns = {
					@JoinColumn(name="twitter_team_id")
			},
			inverseJoinColumns = {
					@JoinColumn(name="twitter_account_id")
			})
	private List<TwitterAccount> tweetSource;//Twitter accounts which are source of this Team

	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name = "tweeter_team_tweet_twitter",
			joinColumns = {
					@JoinColumn(name="twitter_team_id")
			},
			inverseJoinColumns = {
					@JoinColumn(name="twitter_account_id")
			})
	private List<TwitterAccount> tweetTweeters;//Twitter accounts which which will retweet tweets from tweetSource accounts

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TwitterAccount> getTweetTweeters() {
        return tweetTweeters;
    }

    public void setTweetTweeters(List<TwitterAccount> tweetTweeters) {
        this.tweetTweeters = tweetTweeters;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGlobal() {
        return global;
    }

    public void setGlobal(String global) {
        this.global = global;
    }

    public List<TwitterAccount> getTweetSource() {
        return tweetSource;
    }

    public void setTweetSource(List<TwitterAccount> tweetSource) {
        this.tweetSource = tweetSource;
    }
}
