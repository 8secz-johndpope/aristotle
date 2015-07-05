package com.aristotle.core.service.aws;

import org.omg.CORBA.portable.ApplicationException;

import com.amazonaws.services.sqs.model.Message;

public interface QueueService {

    Message receivePlannedTweetMessage() throws ApplicationException;

    void sendPlannedTweetMessage(String message) throws ApplicationException;

    void deletePlannedTweetMessage(String messageId) throws ApplicationException;

    void deletePlannedTweetMessage(Message message) throws ApplicationException;

}
