package com.aristotle.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.webapp.FacesServlet;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.aristotle.admin.filter.SpringLoginFilter;
import com.aristotle.admin.util.SessionUtil;

@Configuration
@ComponentScan(basePackages = { "com.aristotle.admin.util", "com.aristotle.admin.jsf.bean" })
@EnableAutoConfiguration(exclude = { WebMvcAutoConfiguration.class, DispatcherServletAutoConfiguration.class })
@EnableWebMvc
public class Main3 extends SpringBootServletInitializer {

    public static void mainasa(String[] args) {
        SpringApplication.run(Main3.class, args);
    }
    

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(new Class[] { Main3.class, Initializer.class });
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        FacesServlet servlet = new FacesServlet();  
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(servlet, "*.jsf", "*.xhtml");
		return servletRegistrationBean;
    }
    
    @Bean
    public DispatcherServlet dispatcherServlet() {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        return dispatcherServlet;
    }

    /**
     * Register dispatcherServlet programmatically
     * 
     * @return ServletRegistrationBean
     */
    @Bean
    public ServletRegistrationBean dispatcherServletRegistration() {

        ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet(), "/");

        registration.setName(DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME);


        return registration;
    }


    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        SpringLoginFilter securityFilter = new SpringLoginFilter();
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("*.xhtml");
        registrationBean.setUrlPatterns(urlPatterns);
        registrationBean.setFilter(securityFilter);
        return registrationBean;
    }

    // Put Servlets and Filters in their own nested class so they don't force early
    // instantiation of ManagementServerProperties.
    @Configuration
    protected static class ApplicationContextFilterConfiguration {

        @Bean
        public Filter applicationContextIdFilter(ApplicationContext context) {
            return new ApplicationContextHeaderFilter(context);
        }

    }

    /**
     * {@link OncePerRequestFilter} to add the {@literal X-Application-Context} if required.
     */
    private static class ApplicationContextHeaderFilter extends OncePerRequestFilter {

        private final ApplicationContext applicationContext;

        SessionUtil sessionUtil;

        public ApplicationContextHeaderFilter(ApplicationContext applicationContext) {
            this.applicationContext = applicationContext;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            // System.out.println("Doing Internal Filtering " + request.getPathInfo() + "," + request.getRequestURL().toString() + ", sessionUtil=" + sessionUtil);
            filterChain.doFilter(request, response);
        }

    }
    


}
