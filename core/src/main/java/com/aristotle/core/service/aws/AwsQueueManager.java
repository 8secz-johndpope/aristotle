package com.aristotle.core.service.aws;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;

@Component
@Lazy
public class AwsQueueManager implements Serializable {

    private static final long serialVersionUID = 1L;
    private AmazonSQS sqs;
    private int defaultWaitTime = 20;

	protected Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    public AwsQueueManager(@Value("${aws_region}") String regions, @Value("${aws_access_key}") String accessKey, @Value("${aws_access_secret}") String secretKey) {
        this(Regions.valueOf(regions), accessKey, secretKey);
	}

    private AwsQueueManager(Regions regions, String accessKey, String secretKey) {
		AWSCredentials awsCredentials;
		awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
		sqs = new AmazonSQSClient(awsCredentials);
		Region usWest2 = Region.getRegion(regions);
		sqs.setRegion(usWest2);

	}

    public void sendMessage(String queueName, String messageBody) {
        SendMessageRequest sendMessageRequest = new SendMessageRequest(queueName, messageBody);
        sqs.sendMessage(sendMessageRequest);
    }

    public List<Message> receiveMessage(String queueName, int numberOfMessages) {
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueName);
        receiveMessageRequest.setWaitTimeSeconds(defaultWaitTime);
        // Read only one message ata time
        receiveMessageRequest.setMaxNumberOfMessages(1);

        List<Message> messagesRecieved = new ArrayList<>();
        try {
            logger.debug("Reading Message from Queue {}", queueName);
            ReceiveMessageResult rmResult = sqs.receiveMessage(receiveMessageRequest);
            if (rmResult.getMessages().size() > 0) {
                // Messages have been received
                for (Message message : rmResult.getMessages()) {
                    messagesRecieved.add(message);
                    deleteMessage(message, queueName);
                }
            } else {
                logger.debug("No messages available");
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return messagesRecieved;
    }

    public Message receiveMessage(String queueName) {
        List<Message> messagesReceived = receiveMessage(queueName, 1);
        if (messagesReceived.isEmpty()) {
            return null;
        }
        return messagesReceived.get(0);

    }

    public void deleteMessage(Message message, String queueName) {
        // Doesnt matter what happened, remove it
        // from AWS, its App's responsbility to make
        // sure that persist this message
        // in case of failure/exception
        try {
            sqs.deleteMessage(new DeleteMessageRequest(queueName, message.getReceiptHandle()));
        } catch (Exception ex) {
            // In case some error occurs
        }
    }

    public void deleteMessage(String messageReceiptHandle, String queueName) {
        // Doesnt matter what happened, remove it
        // from AWS, its App's responsbility to make
        // sure that persist this message
        // in case of failure/exception
        try {
            sqs.deleteMessage(new DeleteMessageRequest(queueName, messageReceiptHandle));
        } catch (Exception ex) {
            // In case some error occurs
        }
    }


}
