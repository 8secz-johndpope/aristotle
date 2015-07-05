package com.aristotle.core.persistance;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "twitter_team")
public class TwitterTeam extends BaseEntity{

	@Column(name = "name")
	private String name;
	
    @Column(name = "url")
    private String url;

    @Column(name = "description")
	private String description;

    @Column(name = "global")
    private boolean global;

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
    private Set<TwitterAccount> tweetTweeters;// Twitter accounts which which will retweet tweets from tweetSource accounts

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinColumn(name = "twitter_app_id")
    private TwitterApp twitterApp;
    @Column(name = "twitter_app_id", insertable = false, updatable = false)
    private Long twitterAppId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<TwitterAccount> getTweetTweeters() {
        return tweetTweeters;
    }

    public void setTweetTweeters(Set<TwitterAccount> tweetTweeters) {
        this.tweetTweeters = tweetTweeters;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    public List<TwitterAccount> getTweetSource() {
        return tweetSource;
    }

    public void setTweetSource(List<TwitterAccount> tweetSource) {
        this.tweetSource = tweetSource;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public TwitterApp getTwitterApp() {
        return twitterApp;
    }

    public void setTwitterApp(TwitterApp twitterApp) {
        this.twitterApp = twitterApp;
    }

    public Long getTwitterAppId() {
        return twitterAppId;
    }

    public void setTwitterAppId(Long twitterAppId) {
        this.twitterAppId = twitterAppId;
    }
}
