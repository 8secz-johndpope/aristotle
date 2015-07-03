package com.aristotle.core.persistance;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.aristotle.core.enums.PlannedPostStatus;
import com.aristotle.core.enums.PostLocationType;

@Entity
@Table(name = "planned_tweets")
public class PlannedTweet extends BaseEntity{

	@Column(name = "tweet_type")
	private String tweetType;
	
	@Column(name = "tweet_id")
	private Long tweetId;

    @Column(name = "from_twitter_user_id")
    private Long fromTwitterUserId;

	@Column(name = "picture")
	private String picture;

	@Column(name = "message", length = 140)
	private String message;

    @Column(name = "total_required")
    private Integer totalRequired;

    @Column(name = "posting_time")
	private Date postingTime;

	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private PlannedPostStatus status;

	@Column(name = "location_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private PostLocationType locationType;

	@Column(name = "location_id")
	private Long locationId;

	@Column(name = "error_message")
	private String errorMessage;

	@Column(name = "total_success_tweets")
	private int totalSuccessTweets;
	@Column(name = "total_failed_tweets")
	private int totalFailedTweets;

	@Override
    public Long getId() {
		return id;
	}

	@Override
    public void setId(Long id) {
		this.id = id;
	}

	@Override
    public int getVer() {
		return ver;
	}

	@Override
    public void setVer(int ver) {
		this.ver = ver;
	}

	@Override
    public Date getDateCreated() {
		return dateCreated;
	}

	@Override
    public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Override
    public Date getDateModified() {
		return dateModified;
	}

	@Override
    public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	@Override
    public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	@Override
    public Long getModifierId() {
		return modifierId;
	}

	public void setModifierId(Long modifierId) {
		this.modifierId = modifierId;
	}

	public String getTweetType() {
		return tweetType;
	}

	public void setTweetType(String tweetType) {
		this.tweetType = tweetType;
	}

	public Long getTweetId() {
		return tweetId;
	}

	public void setTweetId(Long tweetId) {
		this.tweetId = tweetId;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getPostingTime() {
		return postingTime;
	}

	public void setPostingTime(Date postingTime) {
		this.postingTime = postingTime;
	}

	public PlannedPostStatus getStatus() {
		return status;
	}

	public void setStatus(PlannedPostStatus status) {
		this.status = status;
	}

	public PostLocationType getLocationType() {
		return locationType;
	}

	public void setLocationType(PostLocationType locationType) {
		this.locationType = locationType;
	}

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public int getTotalSuccessTweets() {
		return totalSuccessTweets;
	}

	public void setTotalSuccessTweets(int totalSuccessTweets) {
		this.totalSuccessTweets = totalSuccessTweets;
	}

	public int getTotalFailedTweets() {
		return totalFailedTweets;
	}

	public void setTotalFailedTweets(int totalFailedTweets) {
		this.totalFailedTweets = totalFailedTweets;
	}

    public Integer getTotalRequired() {
        return totalRequired;
    }

    public void setTotalRequired(Integer totalRequired) {
        this.totalRequired = totalRequired;
    }

    public Long getFromTwitterUserId() {
        return fromTwitterUserId;
    }

    public void setFromTwitterUserId(Long fromTwitterUserId) {
        this.fromTwitterUserId = fromTwitterUserId;
    }

    @Override
    public String toString() {
        return "PlannedTweet [id=" + id + ", ver=" + ver + ", dateCreated=" + dateCreated + ", dateModified=" + dateModified + ", creatorId=" + creatorId + ", modifierId=" + modifierId
                + ", tweetType=" + tweetType + ", tweetId=" + tweetId + ", picture=" + picture + ", message=" + message + ", totalRequired=" + totalRequired + ", postingTime=" + postingTime
                + ", status=" + status + ", locationType=" + locationType + ", locationId=" + locationId + ", errorMessage=" + errorMessage + ", totalSuccessTweets=" + totalSuccessTweets
                + ", totalFailedTweets=" + totalFailedTweets + "]";
    }

}
