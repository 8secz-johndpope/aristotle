package com.aristotle.admin.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aristotle.admin.util.SessionUtil;
import com.aristotle.core.persistance.User;

@Component("springLoginFilter")
public class SpringLoginFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SessionUtil sessionUtil;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub
        logger.info("*****Creating springLoginFilter");
        if (sessionUtil == null) {
            sessionUtil = new SessionUtil();
        }

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        // logger.info("Requested URL " + httpServletRequest.getRequestURL().toString());
        if (httpServletRequest.getRequestURL().toString().contains("login") || httpServletRequest.getRequestURL().toString().contains("javax.faces.resource")) {
            chain.doFilter(request, response);
            return;
        }

        User user = sessionUtil.getLoggedInUserFromSession(httpServletRequest);
        logger.info("Logged In User = " + user);
        if (user == null) {
            // No user logegd In
            String redirectUrl = "/admin/login?redirect_url=" + httpServletRequest.getRequestURI();
            logger.info("User Not logged In, SO Redirecting to {}", redirectUrl);
            ((HttpServletResponse) response).sendRedirect(redirectUrl);
            return;
        }
        chain.doFilter(request, response);
        /*
        String redirectUrl = "/admin/notAllowed.html";
        logger.info("User Not Allowed So Redirecting to {}", redirectUrl);
        ((HttpServletResponse) response).sendRedirect(redirectUrl);
        */
        return;

    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

}
