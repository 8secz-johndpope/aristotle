package com.aristotle.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.webapp.FacesServlet;

import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.aristotle.admin.filter.SpringLoginFilter;
import com.aristotle.admin.scope.ViewScope;

@Configuration
@ComponentScan(basePackages = { "com.aristotle.admin.util", "com.aristotle.admin.jsf.bean" })
@EnableAutoConfiguration(exclude = { WebMvcAutoConfiguration.class, DispatcherServletAutoConfiguration.class })
public class Main2 extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Main2.class, args);
    }
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(new Class[] { Main2.class, Initializer.class });
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        FacesServlet servlet = new FacesServlet();  
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(servlet, "*.jsf", "*.xhtml");
		return servletRegistrationBean;
    }

    @Bean
    public CustomScopeConfigurer customScopeConfigurer() {
        CustomScopeConfigurer customScopeConfigurer = new CustomScopeConfigurer();
        Map<String, Object> scopes = new HashMap<String, Object>();
        scopes.put("view", new ViewScope());
        customScopeConfigurer.setScopes(scopes);
        return customScopeConfigurer;
    }


    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        SpringLoginFilter securityFilter = new SpringLoginFilter();
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("*.xhtml");
        registrationBean.setUrlPatterns(urlPatterns);
        registrationBean.setFilter(securityFilter);
        return registrationBean;
    }

}
