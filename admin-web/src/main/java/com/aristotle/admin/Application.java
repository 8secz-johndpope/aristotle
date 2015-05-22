package com.aristotle.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.webapp.FacesServlet;
import javax.servlet.DispatcherType;

import org.apache.naming.factory.BeanFactory;
import org.primefaces.webapp.filter.FileUploadFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.aristotle.admin.filter.SpringLoginFilter;
import com.aristotle.admin.scope.ViewScope;
import com.ocpsoft.pretty.PrettyFilter;

/**
 * Created by Ravi Sharma on 17/05/15.
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.aristotle.admin.jsf.bean", "com.aristotle.admin.service", "com.aristotle.admin.util", "com.aristotle.admin.filter" })
public class Application extends SpringBootServletInitializer {

    @Autowired
    private BeanFactory springLoginFilter;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class, DatabaseConfig.class, ServiceConfig.class, Initializer.class);
        app.run(args);
    }

    @Bean
    public ServletRegistrationBean facesServletRegistraiton() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new FacesServlet(), new String[]{"*.xhtml"});
        registration.setName("Faces Servlet");
        registration.setLoadOnStartup(1);
        return registration;
    }


    // @Bean
    public FilterRegistrationBean facesUploadFilterRegistration() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new FileUploadFilter(), facesServletRegistraiton());
        registrationBean.setName("PrimeFaces FileUpload Filter");
        List<String> serveltNames = new ArrayList<String>();
        serveltNames.add("Faces Servlet");
        registrationBean.setServletNames(serveltNames);
        registrationBean.setDispatcherTypes(DispatcherType.FORWARD, DispatcherType.REQUEST);
        return registrationBean;
    }

    @Bean
    public CustomScopeConfigurer customScopeConfigurer() {
        CustomScopeConfigurer customScopeConfigurer = new CustomScopeConfigurer();
        Map<String, Object> scopes = new HashMap<String, Object>();
        scopes.put("view", new ViewScope());
        customScopeConfigurer.setScopes(scopes);
        return customScopeConfigurer;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        SpringLoginFilter securityFilter = new SpringLoginFilter();
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("*.xhtml");
        registrationBean.setUrlPatterns(urlPatterns);
        System.out.println("groovy.swing.factory.BeanFactory  = " + springLoginFilter);
        registrationBean.setFilter(securityFilter);

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean prettyFilterBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        PrettyFilter securityFilter = new PrettyFilter();
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/*");
        registrationBean.setUrlPatterns(urlPatterns);
        // registrationBean.setDispatcherTypes(DispatcherType.FORWARD, DispatcherType.REQUEST, DispatcherType.ERROR);
        registrationBean.setFilter(securityFilter);

        return registrationBean;
    }


    // @Bean
    public ServletContextInitializer servletContextInitializer() {
        return servletContext -> {
            servletContext.setInitParameter("com.sun.faces.forceLoadConfiguration", Boolean.TRUE.toString());
            servletContext.setInitParameter("primefaces.THEME", "bootstrap");
            servletContext.setInitParameter("primefaces.CLIENT_SIDE_VALIDATION", Boolean.TRUE.toString());
            servletContext.setInitParameter("javax.faces.FACELETS_SKIP_COMMENTS", Boolean.TRUE.toString());
            servletContext.setInitParameter("primefaces.FONT_AWESOME", Boolean.TRUE.toString());
            // servletContext.setInitParameter("primefaces.UPLOADER", "commons");
        };
    }
}
