package com.aristotle.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.aristotle.core.persistance.User;
import com.aristotle.core.service.AwsFileManager;
import com.aristotle.core.service.UserService;

@Controller
public class FileUploadController {
    
    @Autowired
    private AwsFileManager awsFileManager;
    @Autowired
    private UserService userService;

    @Value("${aws_access_key}")
    private String awsKey;
    @Value("${aws_access_secret}")
    private String awsSecret;

    @Value("${static_data_env:dev}")
    private String staticDataEnv;

    @RequestMapping(value = "/editprofile/upload", method = RequestMethod.POST)
    public @ResponseBody String handleFileUpload(HttpServletRequest httpServletRequest, ModelAndView mv, @RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                User user = (User) httpServletRequest.getSession().getAttribute("loggedInUser");
                System.out.println("Uploading File");
                String subdDirectory = "";
                System.out.println("subdDirectory = " + subdDirectory);
                String remoteFileName = "profile/" + staticDataEnv + "/" + user.getId() + "/" + file.getName();
                String bucketName = "static.swarajabhiyan.org";

                awsFileManager.uploadFileToS3(awsKey, awsSecret, bucketName, remoteFileName, file.getInputStream(), "image/jpeg");
                userService.updateUserProfilePic(user.getId(), remoteFileName);
                RedirectView rv = new RedirectView("/user/editprofile");
                mv.setView(rv);
                return "You successfully uploaded " + remoteFileName + "!";
            } catch (Exception e) {
                return "Failed to upload photo " + file.getName() + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + file.getName() + " because the file was empty.";
        }
    }
}
