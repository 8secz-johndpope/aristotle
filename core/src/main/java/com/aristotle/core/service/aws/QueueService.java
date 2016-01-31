package com.aristotle.core.service.aws;

import org.omg.CORBA.portable.ApplicationException;

import com.amazonaws.services.sqs.model.Message;

public interface QueueService {

    Message receivePlannedTweetMessage() throws ApplicationException;

    void sendPlannedTweetMessage(String message) throws ApplicationException;

    void deletePlannedTweetMessage(String messageId) throws ApplicationException;

    void deletePlannedTweetMessage(Message message) throws ApplicationException;

    void sendRefreshDonationMessage(String message) throws ApplicationException;

    Message receiveRefreshDonationMessage() throws ApplicationException;

    void sendRefreshUserMessage(String message) throws ApplicationException;

    Message receiveRefreshUserMessage() throws ApplicationException;

}
