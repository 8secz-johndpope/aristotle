package com.aristotle.core.service.dto;

import com.aristotle.core.persistance.Email;
import com.aristotle.core.persistance.Phone;
import com.aristotle.core.persistance.User;

public class UserSearchResultForEdting {

    private User user;
    private Email email;
    private Phone phone;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }
}
