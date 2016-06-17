package com.aristotle.member;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//@Configuration
//@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
//@EnableWebSecurity
public class ApplicationSecurity {//extends WebSecurityConfigurerAdapter {

	//@Override
    protected void configure( HttpSecurity http )
        throws Exception
    {
		 http.csrf().disable().authorizeRequests().anyRequest().permitAll();
    }
}