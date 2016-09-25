package com.aristotle.core.persistance;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="tweet")
public class Tweet extends BaseEntity {

	@Column(name = "tweet_external_id")
	private Long tweetExternalId;//content of tweet
	@Column(name = "tweet_content", length=256)
	private String tweetContent;//content of tweet
    @Column(name = "error_message", length = 256)
    private String errorMessage;
    @Column(name = "status")
    private String status;
    @Column(name = "auto_retweeted")
    private boolean autoRetweeted = true;
	
	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.LAZY )
    @JoinColumn(name="twitter_account_id")
    private TwitterAccount twitterAccount;
	@Column(name="twitter_account_id", insertable=false,updatable=false)
	private Long twitterAccountId;

	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.LAZY )
    @JoinColumn(name="planned_tweet_id")
    private PlannedTweet plannedTweet;
	@Column(name="planned_tweet_id", insertable=false,updatable=false)
	private Long plannedTweetId;

	public Long getTweetExternalId() {
		return tweetExternalId;
	}
	public void setTweetExternalId(Long tweetExternalId) {
		this.tweetExternalId = tweetExternalId;
	}
	public String getTweetContent() {
		return tweetContent;
	}
	public void setTweetContent(String tweetContent) {
		this.tweetContent = tweetContent;
	}
	public TwitterAccount getTwitterAccount() {
		return twitterAccount;
	}
	public void setTwitterAccount(TwitterAccount twitterAccount) {
		this.twitterAccount = twitterAccount;
	}
	public Long getTwitterAccountId() {
		return twitterAccountId;
	}
	public void setTwitterAccountId(Long twitterAccountId) {
		this.twitterAccountId = twitterAccountId;
	}
	public PlannedTweet getPlannedTweet() {
		return plannedTweet;
	}
	public void setPlannedTweet(PlannedTweet plannedTweet) {
		this.plannedTweet = plannedTweet;
	}
	public Long getPlannedTweetId() {
		return plannedTweetId;
	}
	public void setPlannedTweetId(Long plannedTweetId) {
		this.plannedTweetId = plannedTweetId;
	}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isAutoRetweeted() {
        return autoRetweeted;
    }

    public void setAutoRetweeted(boolean autoRetweeted) {
        this.autoRetweeted = autoRetweeted;
    }
}
