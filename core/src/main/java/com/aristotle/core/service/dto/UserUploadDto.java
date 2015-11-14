package com.aristotle.core.service.dto;

public class UserUploadDto {

    private String email;
    private String phone;
    private String name;
    private boolean emailAlreadyExists;
    private boolean phoneAlreadyExists;
    private Long userIdForEmail;
    private Long userIdForPhone;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEmailAlreadyExists() {
        return emailAlreadyExists;
    }

    public void setEmailAlreadyExists(boolean emailAlreadyExists) {
        this.emailAlreadyExists = emailAlreadyExists;
    }

    public boolean isPhoneAlreadyExists() {
        return phoneAlreadyExists;
    }

    public void setPhoneAlreadyExists(boolean phoneAlreadyExists) {
        this.phoneAlreadyExists = phoneAlreadyExists;
    }

    public Long getUserIdForEmail() {
        return userIdForEmail;
    }

    public void setUserIdForEmail(Long userIdForEmail) {
        this.userIdForEmail = userIdForEmail;
    }

    public Long getUserIdForPhone() {
        return userIdForPhone;
    }

    public void setUserIdForPhone(Long userIdForPhone) {
        this.userIdForPhone = userIdForPhone;
    }


}
