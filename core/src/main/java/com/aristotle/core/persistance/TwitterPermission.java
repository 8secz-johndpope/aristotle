package com.aristotle.core.persistance;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "twitter_permission")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE,region="Account", include="all")
public class TwitterPermission extends BaseEntity{

	@Column(name = "token", nullable = false, length=256)
	private String token;

	@Column(name = "token_secret", nullable = false, length=256)
	private String tokenSecret;

	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.LAZY )
    @JoinColumn(name="twitter_account_id")
    private TwitterAccount twitterAccount;
	@Column(name="twitter_account_id", insertable=false,updatable=false)
	private Long twitterAccountId;

	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.LAZY )
	@JoinColumn(name="twitter_app_id")
	private TwitterApp twitterApp;
	@Column(name="twitter_app_id", insertable=false,updatable=false)
	private Long twitterAppId;

	public Long getTwitterAppId() {
		return twitterAppId;
	}

	public void setTwitterAppId(Long twitterAppId) {
		this.twitterAppId = twitterAppId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTokenSecret() {
		return tokenSecret;
	}

	public void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
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

	public TwitterApp getTwitterApp() {
		return twitterApp;
	}

	public void setTwitterApp(TwitterApp twitterApp) {
		this.twitterApp = twitterApp;
	}



}
