package com.aristotle.core.service;

import com.aristotle.core.exception.AppException;

public interface EmailManager {

    void sendEmail(String toEmail, String fromName, String fromEmail, String subject, String body, String htmlContent) throws AppException;
}
