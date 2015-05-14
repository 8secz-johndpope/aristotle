package com.aristotle.web.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import com.aristotle.web.controller.HelloController;

@Configuration
@EnableJpaRepositories(basePackages = { "com.aristotle.core.persistance.repo" })
@EntityScan(basePackages = { "com.aristotle.core.persistance" })
public class DatabaseConfig {

    @Autowired
    HelloController helloController;

    @Bean
    public SimpleUrlHandlerMapping customeSimpleUrlHandlerMapping() {
        SimpleUrlHandlerMapping simple = new SimpleUrlHandlerMapping();
        Map<String, Object> urlMap = new HashMap<String, Object>();
        urlMap.put("/test2", helloController);
        simple.setUrlMap(urlMap);
        return simple;
    }
}
