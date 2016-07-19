package com.aristotle.member.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.User;
import com.aristotle.core.service.AwsFileManager;

@Component
public class AwsProfileImageUtil {

	@Autowired
	private AwsFileManager awsFileManager;
	@Autowired
	private MemberService memberService;

	@Value("${aws_access_key}")
	private String awsKey;
	@Value("${aws_access_secret}")
	private String awsSecret;

	@Value("${static_data_env:dev}")
	private String staticDataEnv;

	public User uploadImage(File file, User user) throws FileNotFoundException, AppException {
		if (file != null) {
			String bucketName = "static.swarajabhiyan.org";
			if (!StringUtils.isEmpty(user.getProfilePic())) {
				try {
					awsFileManager.deleteFileFromS3(awsKey, awsSecret, bucketName, user.getProfilePic());
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			String currentUserProfileImage = user.getProfilePic();
			final String userDirectory = "profile/" + staticDataEnv + "/" + user.getId() + "/";
			String remoteFileName = userDirectory + System.currentTimeMillis() + ".jpg";
			System.out.println("Uploading remoteFileName = " + remoteFileName);

			awsFileManager.uploadFileToS3(awsKey, awsSecret, bucketName, remoteFileName, new FileInputStream(file), "image/jpeg");
			user = memberService.updateUserProfilePic(user.getId(), remoteFileName);
			user.setProfilePic(remoteFileName);
			if (currentUserProfileImage != null && currentUserProfileImage.contains(userDirectory)) {
				// delete old image
				awsFileManager.deleteFileFromS3(awsKey, awsSecret, bucketName, currentUserProfileImage);
			}
		}
		return user;
	}
}
