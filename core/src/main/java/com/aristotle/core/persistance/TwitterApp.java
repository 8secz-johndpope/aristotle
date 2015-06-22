package com.aristotle.core.persistance;

import javax.persistence.*;

@Entity
@Table(name="twitter_account")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE,region="Account", include="all")
public class TwitterApp extends BaseEntity{

	@Column(name = "consumer_key", nullable = false, length=256)
	private String consumerKey;
	
	@Column(name = "consumer_secret", nullable = false, length=256)
	private String consumerSecret;

	@Column(name = "private_app")
	private String privateApp;

	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="user_id")
    private User user;
	@Column(name="user_id", insertable=false,updatable=false)
	private Long userId;

	public String getConsumerKey() {
		return consumerKey;
	}

	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}

	public String getConsumerSecret() {
		return consumerSecret;
	}

	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
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

	public String getPrivateApp() {
		return privateApp;
	}

	public void setPrivateApp(String privateApp) {
		this.privateApp = privateApp;
	}



}
