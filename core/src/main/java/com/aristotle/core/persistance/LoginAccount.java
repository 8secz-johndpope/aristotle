package com.aristotle.core.persistance;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "login_account")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE,region="Account", include="all")
public class LoginAccount extends BaseEntity {

	@Column(name = "user_name", nullable = false, length=256)
	private String userName;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "email", nullable = true)
	private String email;
	
	@Column(name = "phone", nullable = true)
	private String phone;

	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="user_id")
    private User user;
	@Column(name="user_id", insertable=false,updatable=false)
	private Long userId;
	
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	@Override
	public String toString() {
		return "LoginAccount [userName=" + userName + ", email=" + email + ", userId=" + userId + ", toString()=" + super.toString() + "]";
	}
	
	
	
}
