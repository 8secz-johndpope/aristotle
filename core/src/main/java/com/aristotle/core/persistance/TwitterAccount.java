package com.aristotle.core.persistance;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="twitter_account")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE,region="Account", include="all")
public class TwitterAccount extends BaseEntity{

	@Column(name = "image_url", nullable = false)
	private String imageUrl;

	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="user_id")
    private User user;
	@Column(name="user_id", insertable=false,updatable=false)
	private Long userId;
	
	@Column(name="twitter_id")
	private String twitterId;
	@Column(name="screen_name")
	private String screenName;
	@Column(name="screen_name_cap")
	private String screenNameCap;
    @Column(name = "retweetable", columnDefinition = "BIT(1) DEFAULT 0")
    private boolean retweetable;
    @Column(name = "last_tweet_sent_time")
    protected Date lastTweetSentTime;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "tweeter_team_tweet_twitter", joinColumns = { @JoinColumn(name = "twitter_account_id") }, inverseJoinColumns = { @JoinColumn(name = "twitter_team_id") })
    private List<TwitterTeam> twitterTeams;

	public String getScreenNameCap() {
		return screenNameCap;
	}

	public void setScreenNameCap(String screenNameCap) {
		this.screenNameCap = screenNameCap;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getTwitterId() {
		return twitterId;
	}

	public void setTwitterId(String twitterId) {
		this.twitterId = twitterId;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

    public boolean getRetweetable() {
        return retweetable;
    }

    public void setRetweetable(boolean retweetable) {
        this.retweetable = retweetable;
    }

    public List<TwitterTeam> getTwitterTeams() {
        return twitterTeams;
    }

    public void setTwitterTeams(List<TwitterTeam> twitterTeams) {
        this.twitterTeams = twitterTeams;
    }

    public Date getLastTweetSentTime() {
        return lastTweetSentTime;
    }

    public void setLastTweetSentTime(Date lastTweetSentTime) {
        this.lastTweetSentTime = lastTweetSentTime;
    }

}
