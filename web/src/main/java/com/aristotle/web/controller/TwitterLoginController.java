package com.aristotle.web.controller;

import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.util.ParameterMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.oauth1.AuthorizedRequestToken;
import org.springframework.social.oauth1.OAuth1Operations;
import org.springframework.social.oauth1.OAuth1Parameters;
import org.springframework.social.oauth1.OAuthToken;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.aristotle.core.persistance.TwitterAccount;
import com.aristotle.core.persistance.TwitterApp;
import com.aristotle.core.persistance.TwitterTeam;
import com.aristotle.core.persistance.User;
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
            httpServletRequest.getSession().setAttribute("consumerKey", twitterApp.getConsumerKey());
            httpServletRequest.getSession().setAttribute("consumerSecret", twitterApp.getConsumerSecret());
            httpServletRequest.getSession().setAttribute("twitterApp", twitterApp);
            httpServletRequest.getSession().setAttribute("twitterTeam", twitterTeam);

            OAuth1Operations oauthOperations = twitterConnectionFactory.getOAuthOperations();

            String finalRedirecturl = getFinalRedirecturl(httpServletRequest);

            OAuthToken requestToken = oauthOperations.fetchRequestToken(finalRedirecturl, null);
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
    public ModelAndView loginSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv) {
        try {
        	String requestTokenValue = httpServletRequest.getParameter("oauth_token");
            String oauthVerifier = httpServletRequest.getParameter("oauth_verifier");
            System.out.println("requestTokenValue = " + requestTokenValue);
            System.out.println("oauthVerifier = " + oauthVerifier);

            String consumerKey = (String) httpServletRequest.getSession().getAttribute("consumerKey");
            httpServletRequest.getSession().removeAttribute("consumerKey");
            String consumerSecret = (String) httpServletRequest.getSession().getAttribute("consumerSecret");
            httpServletRequest.getSession().removeAttribute("consumerSecret");
            TwitterApp twitterApp = (TwitterApp) httpServletRequest.getSession().getAttribute("twitterApp");
            httpServletRequest.getSession().removeAttribute("twitterApp");
            TwitterTeam twitterTeam = (TwitterTeam) httpServletRequest.getSession().getAttribute("twitterTeam");
            httpServletRequest.getSession().removeAttribute("twitterTeam");
            // upon receiving the callback from the provider:
            TwitterConnectionFactory twitterConnectionFactory = new TwitterConnectionFactory(consumerKey, consumerSecret);
            OAuth1Operations oauthOperations = twitterConnectionFactory.getOAuthOperations();
            
            OAuthToken requestToken = new OAuthToken(requestTokenValue, consumerSecret);
            System.out.println("requestToken = " + requestToken);
            Enumeration<String> names = httpServletRequest.getParameterNames();
            while(names.hasMoreElements()){
            	String param = (String) names.nextElement();
                System.out.println(param + " = " + httpServletRequest.getParameter(param));

        	}
            OAuthToken accessToken = oauthOperations.exchangeForAccessToken(new AuthorizedRequestToken(requestToken, oauthVerifier), new OAuth1Parameters());
            Connection<Twitter> twitterConnection = twitterConnectionFactory.createConnection(accessToken);

            User user = (User) httpServletRequest.getSession().getAttribute("loggedInUser");

            TwitterAccount twitterAccount = twitterService.saveTwitterAccount(twitterConnection, twitterApp.getId(), twitterTeam.getId(), user);
            setCookie(httpServletResponse, "ti", twitterAccount.getId());
            String redirectUrl = getAndRemoveRedirectUrlFromSession(httpServletRequest);
            if (StringUtil.isEmpty(redirectUrl)) {
                redirectUrl = httpServletRequest.getContextPath() + "/register";
            }
            RedirectView rv = new RedirectView(redirectUrl);
            logger.info("url= {}", redirectUrl);
            mv.setView(rv);

            return mv;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return mv;
    }
    
    private void setCookie(HttpServletResponse httpServletResponse, String cookieName, Long twitterAccountId){
    	Cookie cookie = new Cookie(cookieName, String.valueOf(twitterAccountId));
    	cookie.setMaxAge(604800);
    	cookie.setPath("/");
    	httpServletResponse.addCookie(cookie);
    }

    private String getFinalRedirecturl(HttpServletRequest httpServletRequest) {
        String finalRedirecturl = httpServletRequest.getScheme() + "://" + httpServletRequest.getServerName();
        if (httpServletRequest.getServerPort() != 80) {
            finalRedirecturl = finalRedirecturl + ":" + httpServletRequest.getServerPort();
        }
        finalRedirecturl = finalRedirecturl + twitterRedirectUrl;
        return finalRedirecturl;
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

    protected String getAndRemoveRedirectUrlFromSession(HttpServletRequest httpServletRequest) {
        String redirectUrl = (String) httpServletRequest.getSession().getAttribute("redirect");
        httpServletRequest.getSession().removeAttribute("redirect");
        return redirectUrl;
    }

}
