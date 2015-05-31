package com.aristotle.web;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.aristotle.web.config.DatabaseConfig;

@SpringBootApplication
@ComponentScan(basePackages = { "com.aristotle.core.service", "com.aristotle.web.controller", "com.aristotle.web.plugin.impl", "com.aristotle.web.plugin.impl", "com.aristotle.web.ui.template.impl",
        "com.aristotle.web.service" })
@EnableAutoConfiguration
public class App extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(App.class);
    }
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(new Object[] { App.class, DatabaseConfig.class }, args);

        System.out.println("Let's inspect the beans provided by Spring Boot:");
        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            // System.out.println(beanName + " , " + ctx.getBean(beanName));
            System.out.println(beanName);
        }
    }

}