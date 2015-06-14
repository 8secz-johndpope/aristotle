package com.aristotle.admin.service;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.StorageClass;

@Component
public class AwsFileManagerImpl implements AwsFileManager {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //
    @PostConstruct
    public void init() {
        System.out.println("Created AwsImageUtil");
    }

    // @Value("${aws_access_key}")
    private String accessKey;

    // @Value("${aws_access_secret}")
    private String accessSecret;

    // @Value("${aws_region}")
    private String awsRegion;

    /* (non-Javadoc)
     * @see com.aristotle.admin.service.AwsFileManager#uploadFileToS3(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void uploadFileToS3(String awsKey, String awsSecret, String bucketName, String remoteFileNameAndPath, String localFilePathToUpload) throws FileNotFoundException {

        AmazonS3 s3client = new AmazonS3Client(new BasicAWSCredentials(awsKey, awsSecret));

        logger.info("Uploading a new object to S3 from " + localFilePathToUpload + " to remote file " + bucketName + "/" + remoteFileNameAndPath);
        FileInputStream file = new FileInputStream(localFilePathToUpload);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setCacheControl("max-age=2592000");
        objectMetadata.addUserMetadata("x-amz-storage-class", "RRS");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 2);
        objectMetadata.setExpirationTime(calendar.getTime());
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, remoteFileNameAndPath, file, objectMetadata);
        putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
        PutObjectResult putObjectResult = s3client.putObject(putObjectRequest);
        logger.info("File Uploaded");
    }

    /* (non-Javadoc)
     * @see com.aristotle.admin.service.AwsFileManager#uploadFileToS3(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.io.InputStream, java.lang.String)
     */
    @Override
    public void uploadFileToS3(String awsKey, String awsSecret, String bucketName, String remoteFileNameAndPath, InputStream fileToUpload, String contentType) throws FileNotFoundException {

        AmazonS3 s3client = new AmazonS3Client(new BasicAWSCredentials(awsKey, awsSecret));

        logger.info("Uploading a new object to S3 from input Stream to remote file " + bucketName + "/" + remoteFileNameAndPath);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setCacheControl("max-age=2592000");
        objectMetadata.setContentType(contentType);
        objectMetadata.addUserMetadata("x-amz-storage-class", "RRS");
        remoteFileNameAndPath = remoteFileNameAndPath.toLowerCase();
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, remoteFileNameAndPath, fileToUpload, objectMetadata);
        putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
        putObjectRequest.setStorageClass(StorageClass.Standard);
        s3client.putObject(putObjectRequest);
        logger.info("File Uploaded");
    }

    private String getContentType(String fileName) {
        if (fileName.endsWith(".css")) {
            return "text/css";
        }
        if (fileName.endsWith(".js")) {
            return "application/javascript";
        }
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        }
        if (fileName.endsWith(".png")) {
            return "image/png";
        }
        if (fileName.endsWith(".gif")) {
            return "image/gif";
        }
        if (fileName.endsWith(".pdf")) {
            return "application/pdf";
        }
        if (fileName.endsWith(".doc") || fileName.endsWith(".docx")) {
            return "application/msword";
        }

        return null;
    }

    @Override
    public void uploadFileToS3(String awsKey, String awsSecret, String bucketName, String remoteFileNameAndPath, InputStream fileToUpload) throws FileNotFoundException {
        String contentType = getContentType(remoteFileNameAndPath);
        uploadFileToS3(awsKey, awsSecret, bucketName, remoteFileNameAndPath, fileToUpload, contentType);
    }

    public static BufferedImage resize(InputStream is, int newW, int newH) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(is);
        return resize(bufferedImage, newW, newH);
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage dimg = new BufferedImage(newW, newH, img.getType());
        Graphics2D g = dimg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
        g.dispose();
        return dimg;
    }

}
