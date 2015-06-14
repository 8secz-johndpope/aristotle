package com.aristotle.core.service;

import org.springframework.beans.factory.annotation.Value;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.aristotle.core.exception.AppException;

public class EmailManagerImpl implements EmailManager {

    @Value("${aws_access_key}")
    private String awsKey;
    @Value("${aws_access_secret}")
    private String awsSecret;

    @Override
    public void sendEmail(String toEmail, String fromName, String fromEmail, String subjectStr, String bodyStr, String htmlContent) throws AppException {
        // Construct an object to contain the recipient address.
        Destination destination = new Destination().withToAddresses(new String[] { toEmail });

        // Create the subject and body of the message.
        Content subject = new Content().withData(subjectStr);
        Content textBody = new Content().withData(bodyStr);


        Body body = new Body().withText(textBody);
        if (htmlContent != null) {
            Content htmlBody = new Content().withData(htmlContent);
            body.withHtml(htmlBody);
        }

        // Create a message with the specified subject and body.
        Message message = new Message().withSubject(subject).withBody(body);

        // Assemble the email.
        String completeFromAddress = "\"" + fromName + "\" <" + fromEmail + ">";
        SendEmailRequest request = new SendEmailRequest().withSource(completeFromAddress).withDestination(destination).withMessage(message);


        try {
            System.out.println("Attempting to send an email through Amazon SES by using the AWS SDK for Java...");

            AWSCredentials awsCredentials = new BasicAWSCredentials(awsKey, awsSecret);
            AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient(awsCredentials);

            Region REGION = Region.getRegion(Regions.US_WEST_2);
            client.setRegion(REGION);

            // Send the email.
            client.sendEmail(request);
            System.out.println("Email sent!");
        } catch (Exception ex) {
            System.out.println("The email was not sent.");
            System.out.println("Error message: " + ex.getMessage());
        }

    }

}
