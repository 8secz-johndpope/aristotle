package com.aristotle.core.service.dto;

public class UserContactBean {

    private String mobile;
    private String countryCode;
    private String email;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserContactBean [mobile=" + mobile + ", countryCode=" + countryCode + ", email=" + email + "]";
    }

}
