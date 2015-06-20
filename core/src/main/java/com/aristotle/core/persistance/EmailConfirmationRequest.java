package com.aristotle.core.persistance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "email_confirmation_request")
public class EmailConfirmationRequest extends BaseEntity {

    @Column(name = "email", nullable = false, length = 256)
    private String email;

    @Column(name = "token", nullable = false)
    private String token;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
