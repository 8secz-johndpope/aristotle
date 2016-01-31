package com.aristotle.core.service.aws;

import org.omg.CORBA.portable.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.model.Message;

@Service
public class AwsQueueServiceImpl implements QueueService {

    @Autowired
    private AwsQueueManager awsQueueManager;
    @Value("${planned_tweet_queue}")
    private String plannedTweetQueue;
    @Value("${refresh_donation_queue}")
    private String refreshDonationQueue;
    @Value("${refresh_user_queue}")
    private String refreshUserQueue;

    private void sendMessage(String queueName, String message) {
        System.out.println("Sending message :" + message + " , to queue [" + queueName + "]");
        awsQueueManager.sendMessage(plannedTweetQueue, message);
    }

    private void deleteMessage(String messageId, String queueName) throws ApplicationException {
        awsQueueManager.deleteMessage(messageId, queueName);
    }

    private void deleteMessage(Message messageId, String queueName) throws ApplicationException {
        awsQueueManager.deleteMessage(messageId, queueName);
    }
    @Override
    public Message receivePlannedTweetMessage() throws ApplicationException {
        return awsQueueManager.receiveMessage(plannedTweetQueue);
    }

    @Override
    public void sendPlannedTweetMessage(String message) throws ApplicationException {
        sendMessage(plannedTweetQueue, message);
    }

    @Override
    public void deletePlannedTweetMessage(String messageId) throws ApplicationException {
        awsQueueManager.deleteMessage(messageId, plannedTweetQueue);
    }

    @Override
    public void deletePlannedTweetMessage(Message message) throws ApplicationException {
        awsQueueManager.deleteMessage(message, plannedTweetQueue);
    }

    @Override
    public void sendRefreshDonationMessage(String message) throws ApplicationException {
        sendMessage(refreshDonationQueue, message);

    }

    @Override
    public void sendRefreshUserMessage(String message) throws ApplicationException {
        sendMessage(refreshUserQueue, message);
    }

    @Override
    public Message receiveRefreshDonationMessage() throws ApplicationException {
        return awsQueueManager.receiveMessage(refreshDonationQueue);
    }

    @Override
    public Message receiveRefreshUserMessage() throws ApplicationException {
        return awsQueueManager.receiveMessage(refreshUserQueue);
    }

}
