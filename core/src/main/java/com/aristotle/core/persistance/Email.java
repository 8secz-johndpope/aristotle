package com.aristotle.core.persistance;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="email")
public class Email extends BaseEntity {

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "email_up", nullable = false)
	private String emailUp;
	
	@Column(name = "confirmed")
	private boolean confirmed;

    @Column(name = "confirmation_date")
    protected Date confirmationDate;

    @Column(name = "news_letter", columnDefinition = "BIT(1) DEFAULT 1")
    private boolean newsLetter;

    @Column(name = "confirmation_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private ConfirmationType confirmationType;

	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="user_id")
    private User user;
	@Column(name="user_id", insertable=false,updatable=false)
	private Long userId;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "phone_id")
    private Phone phone;
    @Column(name = "phone_id", insertable = false, updatable = false)
    private Long phoneId;

    public enum ConfirmationType {
		CONFIRMED_FACEBOOK_ACCOUNT,
		CONFIRMED_GOOGLE_ACCOUNT,
		RECEIVED_EMAIL_FROM_ACCOUNT,
		UN_CONFIRNED,
		ADMIN_ENTERED,
		DONOR_ENTERED,
		VIA_EMAIL_VERIFICATION_FLOW
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailUp() {
		return emailUp;
	}

	public void setEmailUp(String emailUp) {
		this.emailUp = emailUp;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	public ConfirmationType getConfirmationType() {
		return confirmationType;
	}

	public void setConfirmationType(ConfirmationType confirmationType) {
		this.confirmationType = confirmationType;
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

    public boolean isNewsLetter() {
        return newsLetter;
    }

    public void setNewsLetter(boolean newsLetter) {
        this.newsLetter = newsLetter;
    }

    public Date getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(Date confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public Long getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(Long phoneId) {
        this.phoneId = phoneId;
    }

}
