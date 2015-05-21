package com.aristotle.core.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;

import com.aristotle.core.exception.AppException;

@Component
public class PasswordUtil {

    public String encryptPassword(String password) throws AppException {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] passBytes = password.getBytes();
            byte[] digested = messageDigest.digest(passBytes);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < digested.length; i++) {
                sb.append(Integer.toHexString(0xff & digested[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new AppException(e);
        }
    }

    public boolean checkPassword(String textPassword, String encryptedPassword) throws AppException {
        String convertredEncryptPassword = encryptPassword(textPassword);
        return convertredEncryptPassword.equals(encryptedPassword);
    }
}
