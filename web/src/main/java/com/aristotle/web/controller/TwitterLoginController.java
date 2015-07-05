package com.aristotle.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.oauth1.OAuth1Operations;
import org.springframework.social.oauth1.OAuth1Parameters;
import org.springframework.social.oauth1.OAuthToken;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.TwitterApp;
import com.aristotle.core.persistance.TwitterTeam;
import com.aristotle.core.service.TwitterService;
import com.google.gdata.util.common.base.StringUtil;

@Controller
public class TwitterLoginController {

    @Autowired
    private TwitterService twitterService;

    String twitterRedirectUrl = "/twitter/team/success";
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler({ Exception.class })
    public String handleException(Exception ex) {
        ex.printStackTrace();
        return ex.getMessage();
    }

    @RequestMapping(value = { "/twitter/team/{teamUrl}" })
    public ModelAndView sendToTwitterTeamLogin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView, @PathVariable String teamUrl) {
        try {
            TwitterTeam twitterTeam = twitterService.getTwitterTeamByUrl(teamUrl);
            TwitterApp twitterApp = twitterTeam.getTwitterApp();
            TwitterConnectionFactory twitterConnectionFactory = new TwitterConnectionFactory(twitterApp.getConsumerKey(), twitterApp.getConsumerSecret());

            OAuth1Operations oauthOperations = twitterConnectionFactory.getOAuthOperations();

            OAuthToken requestToken = oauthOperations.fetchRequestToken(twitterRedirectUrl, null);
            String authorizeUrl = oauthOperations.buildAuthenticateUrl(requestToken.getValue(), OAuth1Parameters.NONE);

            setRedirectUrlInSessiom(httpServletRequest, getRedirectUrlForRedirectionAfterLogin(httpServletRequest));

            RedirectView rv = new RedirectView(authorizeUrl);
            logger.info("url= {}", authorizeUrl);
            modelAndView.setView(rv);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modelAndView;
    }

    @RequestMapping(value = "/twitterfail", method = RequestMethod.GET)
    @ResponseBody
    public String loginFailed(HttpServletRequest httpServletRequest, ModelAndView mv) {
        return "Please login to Twitter and give permission";
    }

    @RequestMapping(value = { "/twitter/team/success" })
    public ModelAndView success(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView, @PathVariable String teamUrl) {
        try {
            TwitterTeam twitterTeam = twitterService.getTwitterTeamByUrl(teamUrl);
            TwitterApp twitterApp = twitterTeam.getTwitterApp();
            TwitterConnectionFactory twitterConnectionFactory = new TwitterConnectionFactory(twitterApp.getConsumerKey(), twitterApp.getConsumerSecret());

            OAuth1Operations oauthOperations = twitterConnectionFactory.getOAuthOperations();
            OAuthToken requestToken = oauthOperations.fetchRequestToken(twitterRedirectUrl, null);
            String authorizeUrl = oauthOperations.buildAuthenticateUrl(requestToken.getValue(), OAuth1Parameters.NONE);

            setRedirectUrlInSessiom(httpServletRequest, getRedirectUrlForRedirectionAfterLogin(httpServletRequest));

            RedirectView rv = new RedirectView(authorizeUrl);
            // logger.info("url= {}", authorizeUrl);
            modelAndView.setView(rv);
        } catch (AppException e) {
            e.printStackTrace();
        }
        return modelAndView;
    }

    protected String getRedirectUrlForRedirectionAfterLogin(HttpServletRequest httpServletRequest) {
        String redirectUrlAfterLogin = getRedirectUrl(httpServletRequest);
        logger.info("redirectUrlAfterLogin from param = " + redirectUrlAfterLogin);
        if (StringUtil.isEmpty(redirectUrlAfterLogin)) {
            redirectUrlAfterLogin = httpServletRequest.getContextPath() + "/signin";
            logger.info("redirectUrlAfterLogin default = " + redirectUrlAfterLogin);
        }
        return redirectUrlAfterLogin;
    }

    protected String getRedirectUrl(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getParameter("redirect");
    }

    protected void setRedirectUrlInSessiom(HttpServletRequest httpServletRequest, String redirectUrl) {
        httpServletRequest.getSession(true).setAttribute("redirect", redirectUrl);
    }

}
