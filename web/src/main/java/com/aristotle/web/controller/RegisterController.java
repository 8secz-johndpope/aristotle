package com.aristotle.web.controller;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.exception.FieldsAppException;
import com.aristotle.core.persistance.Donation;
import com.aristotle.core.persistance.User;
import com.aristotle.core.persistance.UserLocation;
import com.aristotle.core.service.DonationService;
import com.aristotle.core.service.UserService;
import com.aristotle.core.service.dto.ResetPasswordBean;
import com.aristotle.core.service.dto.UserChangePasswordBean;
import com.aristotle.core.service.dto.UserContactBean;
import com.aristotle.core.service.dto.UserLoginBean;
import com.aristotle.core.service.dto.UserPersonalDetailBean;
import com.aristotle.core.service.dto.UserRegisterBean;
import com.aristotle.core.service.dto.UserVolunteerBean;
import com.google.gson.JsonObject;

@Controller
public class RegisterController {

    @Autowired
    private UserService userService;
    @Autowired
    private HttpSessionUtil httpSessionUtil;
    @Autowired
    private DonationService donationService;

    @RequestMapping(value = "/registerquick", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> registeruserQuickly(HttpServletRequest httpServletRequest, @RequestBody UserContactBean userContactBean) {
        System.out.println("userContactBean=" + userContactBean);
        JsonObject jsonObject = new JsonObject();
        ResponseEntity<String> returnDt;
        HttpStatus httpStatus;
        try {
            userService.registerUserQuick(userContactBean);
            jsonObject.addProperty("message", "user Registered Succesfully");
            httpStatus = HttpStatus.OK;

        } catch (FieldsAppException e) {
            e.printStackTrace();
            jsonObject.addProperty("message", "Unable to Register User");
            JsonObject fieldErrors = new JsonObject();
            for (Entry<String, String> oneEntry : e.getFieldErrors().entrySet()) {
                fieldErrors.addProperty(oneEntry.getKey(), oneEntry.getValue());
            }
            jsonObject.add("fieldErrors", fieldErrors);
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        } catch (AppException e) {
            jsonObject.addProperty("message", "Unable to Register User : " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        returnDt = new ResponseEntity<String>(jsonObject.toString(), httpStatus);
        return returnDt;
    }

    private void addError(Map<String, String> errors, String fieldname, String error) {
        errors.put(fieldname, error);
    }

    boolean ignore = false;
    @RequestMapping(value = "/registeruser", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> saveUserProfile(HttpServletRequest httpServletRequest, @RequestBody UserRegisterBean user) {
        JsonObject jsonObject = new JsonObject();
        HttpStatus httpStatus;
        System.out.println("UserRegisterBean = " + user);
        if (ignore) {
            httpStatus = HttpStatus.OK;
            jsonObject.addProperty("message", "User Not saved, Server running in test mode");
        } else {
            Map<String, String> errors = new LinkedHashMap<String, String>();

            if (!user.isNri()) {
                if (user.getStateLivingId() == null || user.getStateLivingId() == 0) {
                    addError(errors, "stateLivingId", "Please select State where you are living currently");
                }
                if (user.getDistrictLivingId() == null || user.getDistrictLivingId() == 0) {
                    addError(errors, "districtLivingId", "Please select District where you are living currently");
                }
                /*
                 * if (user.getAssemblyConstituencyLivingId() == null || user.getAssemblyConstituencyLivingId() == 0) { addError(errors, "assemblyConstituencyLivingId",
                 * "Please select Assembly Constituency where you are living currently"); }
                 */
                if (user.getParliamentConstituencyLivingId() == null || user.getParliamentConstituencyLivingId() == 0) {
                    addError(errors, "parliamentConstituencyLivingId", "Please select Parliament Constituency where you are living currently");
                }
            }

            if (user.getStateVotingId() == null || user.getStateVotingId() == 0) {
                addError(errors, "stateVotingId", "Please select State where you are registered as Voter");
            }
            if (user.getDistrictVotingId() == null || user.getDistrictVotingId() == 0) {
                addError(errors, "districtVotingId", "Please select District where you are registered as Voter");
            }
            /*
             * if (user.getAssemblyConstituencyVotingId() == null || user.getAssemblyConstituencyVotingId() == 0) { addError(errors, "assemblyConstituencyVotingId",
             * "Please select Assembly Constituency where you registered as Voter"); }
             */
            if (user.getParliamentConstituencyVotingId() == null || user.getParliamentConstituencyVotingId() == 0) {
                addError(errors, "parliamentConstituencyVotingId", "Please select Parliament Constituency where you registered as Voter");
            }
            if (user.isNri()) {
                if ((user.getNriCountryId() == null || user.getNriCountryId() == 0)) {
                    addError(errors, "nriCountryId", "Please select Country where you Live");
                }
                if (user.isMember() && StringUtils.isEmpty(user.getPassportNumber())) {
                    addError(errors, "passportNumber", "Please enter passport number. Its Required for NRIs to become member.");
                }
            }
            if (user.getDateOfBirth() == null) {
                addError(errors, "dateOfBirth", "Please enter your Date of Birth");
            }
            if (StringUtils.isEmpty(user.getName())) {
                addError(errors, "name", "Please enter your full name");
            }
            if (errors.isEmpty()) {
                try {
                    System.out.println("saving User " + user);
                    userService.registerUser(user);
                    httpStatus = HttpStatus.OK;
                    jsonObject.addProperty("message", "User Registered Succesfully");
                } catch (FieldsAppException ex) {
                    ex.printStackTrace();
                    httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                    jsonObject.addProperty("message", "Unable to Register User");
                    JsonObject fieldErrors = new JsonObject();
                    for (Entry<String, String> oneEntry : ex.getFieldErrors().entrySet()) {
                        fieldErrors.addProperty(oneEntry.getKey(), oneEntry.getValue());
                    }
                    jsonObject.add("fieldErrors", fieldErrors);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                    jsonObject.addProperty("message", "Unable to Register User : " + ex.getMessage());

                }
            } else {
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                jsonObject.addProperty("message", "Unable to Register User");
                JsonObject fieldErrors = new JsonObject();
                for (Entry<String, String> oneEntry : errors.entrySet()) {
                    fieldErrors.addProperty(oneEntry.getKey(), oneEntry.getValue());
                }
                jsonObject.add("fieldErrors", fieldErrors);

            }
        }

        ResponseEntity<String> returnDt = new ResponseEntity<String>(jsonObject.toString(), httpStatus);
        return returnDt;
    }

    @RequestMapping(value = "/js/login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> loginUser(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @RequestBody UserLoginBean userLoginBean) {
        JsonObject jsonObject = new JsonObject();
        ResponseEntity<String> returnDt;
        HttpStatus httpStatus;
        System.out.println("Login for "+userLoginBean.getUserName());
        try {
            User user = userService.login(userLoginBean.getUserName(), userLoginBean.getPassword());
            System.out.println("user "+user);
            if (user != null) {
                jsonObject.addProperty("message", "user logged in Succesfully");
                httpStatus = HttpStatus.OK;

                List<UserLocation> userLocations = userService.getUserLocations(user.getId());
                httpSessionUtil.setLoggedInUser(httpServletRequest, user);
                Set<Long> userLocationIds = new HashSet<Long>();
                for (UserLocation oneUserLocation : userLocations) {
                    userLocationIds.add(oneUserLocation.getId());
                }
                httpSessionUtil.setLoggedInUserLocations(httpServletRequest, httpServletResponse, userLocationIds);
            } else {
                jsonObject.addProperty("message", "unable to login user");
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

            }
        } catch (AppException e) {
            e.printStackTrace();
            jsonObject.addProperty("message", "Unable to login : " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.addProperty("message", "Unable to login : " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        returnDt = new ResponseEntity<String>(jsonObject.toString(), httpStatus);
        return returnDt;
    }

    @RequestMapping(value = "/js/logout", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        ResponseEntity<String> returnDt = new ResponseEntity<String>("{\"message\":\"Success\"}", HttpStatus.OK);
        httpServletRequest.getSession().invalidate();
        return returnDt;

    }

    @RequestMapping(value = "/js/changepassword", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> changePassword(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @RequestBody UserChangePasswordBean userChangePasswordBean) {
        HttpStatus httpStatus;
        JsonObject body = new JsonObject();
        try {
            User user = (User) httpServletRequest.getSession().getAttribute("loggedInUser");
            userService.changePassword(user.getId(), userChangePasswordBean.getOldPassword(), userChangePasswordBean.getNewPassword());
            httpStatus = HttpStatus.OK;
            body.addProperty("message", "Password Changed Succesfully");
        } catch (AppException e) {
            body.addProperty("message", "Unable to change password : " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            e.printStackTrace();
        } catch (Exception e) {
            body.addProperty("message", "Unable to change password : ");
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            e.printStackTrace();
        }
        ResponseEntity<String> returnDt = new ResponseEntity<String>(body.toString(), httpStatus);
        return returnDt;

    }

    @RequestMapping(value = "/js/personaldetail", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> updatePersonalDetail(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @RequestBody UserPersonalDetailBean userPersonalDetailBean) {
        System.out.println("Saving : " + userPersonalDetailBean);
        HttpStatus httpStatus;
        JsonObject body = new JsonObject();
        try {
            User user = (User) httpServletRequest.getSession().getAttribute("loggedInUser");
            userService.updatePersonalDetails(user.getId(), userPersonalDetailBean);
            httpStatus = HttpStatus.OK;
            body.addProperty("message", "Personal Details updated Succesfully");
        } catch (AppException e) {
            body.addProperty("message", "Unable to save : " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            e.printStackTrace();
        } catch (Exception e) {
            body.addProperty("message", "Unable to save");
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            e.printStackTrace();
        }
        ResponseEntity<String> returnDt = new ResponseEntity<String>(body.toString(), httpStatus);
        return returnDt;

    }

    @RequestMapping(value = "/js/volunteerdetail", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> updateVolunteerDetail(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @RequestBody UserVolunteerBean userVolunteerBean) {
        System.out.println("Saving : " + userVolunteerBean);
        HttpStatus httpStatus = HttpStatus.OK;
        JsonObject body = new JsonObject();
        try {
            User user = (User) httpServletRequest.getSession().getAttribute("loggedInUser");
            userService.updateVolunteerDetails(user.getId(), userVolunteerBean);
            body.addProperty("message", "Personal Details updated Succesfully");
        } catch (AppException e) {
            body.addProperty("message", "Unable to save : " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            e.printStackTrace();
        } catch (Exception e) {
            body.addProperty("message", "Unable to save");
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            e.printStackTrace();
        }
        ResponseEntity<String> returnDt = new ResponseEntity<String>(body.toString(), httpStatus);
        return returnDt;

    }

    @RequestMapping(value = "/js/passwordreset", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> resetPassword(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @RequestBody ResetPasswordBean resetPasswordBean) {
        System.out.println("resetPasswordBean : " + resetPasswordBean);
        HttpStatus httpStatus = HttpStatus.OK;
        JsonObject body = new JsonObject();
        try {
            userService.sendPasswordResetEmail(resetPasswordBean.getEmail());
            body.addProperty("message", "Email sent to " + resetPasswordBean.getEmail() + ", please check your email box including spam folder");
        } catch (AppException e) {
            body.addProperty("message", e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            body.addProperty("message", "Unable to reset password : " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        ResponseEntity<String> returnDt = new ResponseEntity<String>(body.toString(), httpStatus);
        return returnDt;

    }

    @RequestMapping(value = "/js/passwordupdate", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> updatePassword(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @RequestBody ResetPasswordBean resetPasswordBean) {
        System.out.println("resetPasswordBean : " + resetPasswordBean);
        HttpStatus httpStatus = HttpStatus.OK;
        JsonObject body = new JsonObject();
        try {
            userService.updatePassword(resetPasswordBean.getEmail(), resetPasswordBean.getPassword(), resetPasswordBean.getToken());
            body.addProperty("message", "Email sent to " + resetPasswordBean.getEmail() + ", please check your email box including spam folder");
        } catch (AppException e) {
            body.addProperty("message", "Unable to save : " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            e.printStackTrace();
        } catch (Exception e) {
            body.addProperty("message", "Unable to save ");
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            e.printStackTrace();
        }
        ResponseEntity<String> returnDt = new ResponseEntity<String>(body.toString(), httpStatus);
        return returnDt;

    }

    @RequestMapping(value = "/ivr/register", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> saveIvrUser(HttpServletRequest httpServletRequest) {
        JsonObject jsonObject = new JsonObject();
        HttpStatus httpStatus;
        try {
            String mobileNumber = getParam(httpServletRequest, "mobile", null);
            String name = getParam(httpServletRequest, "name", null);
            String gender = getParam(httpServletRequest, "gender", null);
            String amount = getParam(httpServletRequest, "amount", null);
            String paymentMode = getParam(httpServletRequest, "pm", null);
            String state = getParam(httpServletRequest, "state", null);
            String district = getParam(httpServletRequest, "district", null);
            String msg = getParam(httpServletRequest, "msg", null);
            User savedUser = userService.registerIvrMember(mobileNumber, name, gender, amount, paymentMode, state, district, msg);
            jsonObject.addProperty("mobile", mobileNumber);
            jsonObject.addProperty("name", name);
            jsonObject.addProperty("gender", gender);
            jsonObject.addProperty("amount", amount);
            jsonObject.addProperty("pm", paymentMode);
            jsonObject.addProperty("sa_id", savedUser.getId());
            httpStatus = HttpStatus.OK;
        } catch (Exception e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            jsonObject.addProperty("error", e.getMessage());
            e.printStackTrace();
        }
        ResponseEntity<String> returnDt = new ResponseEntity<String>(jsonObject.toString(), httpStatus);
        return returnDt;
    }

    @RequestMapping(value = "/ivr/donation", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> saveIvrDonation(HttpServletRequest httpServletRequest) {
        JsonObject jsonObject = new JsonObject();
        HttpStatus httpStatus;
        try {
            String mobile = getParam(httpServletRequest, "mobile", null);
            String name = getParam(httpServletRequest, "name", null);
            String amount = getParam(httpServletRequest, "amount", null);
            String paymentMode = getParam(httpServletRequest, "pm", null);
            String msg = getParam(httpServletRequest, "msg", null);
            String upid = getParam(httpServletRequest, "upid", null);
            String adminUpid = getParam(httpServletRequest, "aupid", null);
            String adminMobile = getParam(httpServletRequest, "amobile", null);
            Donation donation = donationService.saveIvrDonation(mobile, name, amount, paymentMode, upid, adminUpid, adminMobile, msg);
            jsonObject.addProperty("mobile", mobile);
            jsonObject.addProperty("name", name);
            jsonObject.addProperty("amount", amount);
            jsonObject.addProperty("pm", paymentMode);
            jsonObject.addProperty("upid", upid);
            jsonObject.addProperty("aupid", adminUpid);
            jsonObject.addProperty("amobile", adminMobile);
            jsonObject.addProperty("sa_donation_id", donation.getId());
            httpStatus = HttpStatus.OK;
        } catch (Exception e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            jsonObject.addProperty("error", e.getMessage());
            e.printStackTrace();
        }
        ResponseEntity<String> returnDt = new ResponseEntity<String>(jsonObject.toString(), httpStatus);
        return returnDt;
    }

    private String getParam(HttpServletRequest httpServletRequest, String paramName, String defaultValue) {
        String paramValue = httpServletRequest.getParameter(paramName);
        if (paramValue == null) {
            paramValue = defaultValue;
        }
        return paramValue;
    }

}
