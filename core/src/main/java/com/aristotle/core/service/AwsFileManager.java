package com.aristotle.core.service;

import java.io.FileNotFoundException;
import java.io.InputStream;

public interface AwsFileManager {

    public abstract void uploadFileToS3(String awsKey, String awsSecret, String bucketName, String remoteFileNameAndPath, String localFilePathToUpload) throws FileNotFoundException;

    public abstract void uploadFileToS3(String awsKey, String awsSecret, String bucketName, String remoteFileNameAndPath, InputStream fileToUpload, String contentType) throws FileNotFoundException;

    public abstract void deleteFileFromS3(String awsKey, String awsSecret, String bucketName, String remoteFileNameAndPath) throws FileNotFoundException;

    public abstract void uploadFileToS3(String awsKey, String awsSecret, String bucketName, String remoteFileNameAndPath, InputStream fileToUpload) throws FileNotFoundException;

    public abstract void uploadFileToS3(String awsKey, String awsSecret, String bucketName, String remoteFileNameAndPath, InputStream fileToUpload, String contentType, int expiry)
            throws FileNotFoundException;

}