package com.aristotle.core.persistance;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "password_reset_request")
public class PasswordResetRequest extends BaseEntity {

	@Column(name = "user_name", nullable = false, length=256)
	private String userName;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "login_account_id")
    private Long loginAccountId;

    @Column(name = "valid_tile")
    private Date validTill;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getLoginAccountId() {
        return loginAccountId;
    }

    public void setLoginAccountId(Long loginAccountId) {
        this.loginAccountId = loginAccountId;
    }

    public Date getValidTill() {
        return validTill;
    }

    public void setValidTill(Date validTill) {
        this.validTill = validTill;
    }
	
	
	
	
}
