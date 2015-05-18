package com.aristotle.core.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = { "com.aristotle.core.persistance.repo" })
@EntityScan(basePackages = { "com.aristotle.core.persistance" })
@SpringBootApplication
public class DatabaseConfig {

}
