package com.aristotle.web;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import com.aristotle.web.config.DatabaseConfig;
import com.google.common.cache.CacheBuilder;

@SpringBootApplication
@ComponentScan(basePackages = { "com.aristotle.core.service", "com.aristotle.web.controller", "com.aristotle.web.plugin.impl", "com.aristotle.web.plugin.impl", "com.aristotle.web.ui.template.impl",
        "com.aristotle.web.service", "com.aristotle.web.ui" })
@EnableAutoConfiguration
@EnableCaching
@Import({DatabaseConfig.class})
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

    @Bean
    public CacheManager cacheManager() {
        CacheBuilder cacheBuilder = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES);
        GuavaCacheManager guavaCacheManager = new GuavaCacheManager("events", "videos", "news", "blogs");
        guavaCacheManager.setCacheBuilder(cacheBuilder);
        return guavaCacheManager;
    }

}
