package com.aristotle.member;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.aristotle.core.config.DatabaseConfig;
import com.vaadin.spring.navigator.SpringViewProvider;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class }, scanBasePackages = { "com.aristotle" })
@ComponentScan(basePackages = { "com.aristotle.member", "com.aristotle.core.service", "com.aristotle.core.service.temp" })
public class Application extends SpringBootServletInitializer {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(new Object[] { Application.class, DatabaseConfig.class }, args);

		System.out.println("Let's inspect the beans provided by Spring Boot:");
		String[] beanNames = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for (String beanName : beanNames) {
			// System.out.println(beanName + " , " + ctx.getBean(beanName));
			System.out.println(beanName);
		}
	}

	// BeanFieldGroup.bindFieldsUnbuffered(domain, this);
	@Override protected SpringApplicationBuilder configure(SpringApplicationBuilder 
			application) { 
		//This function generate relevant information for Tomcat or other containers 
		return application.sources(Application.class, DatabaseConfig.class); 
	}

	public static void mainw(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}
	
}
