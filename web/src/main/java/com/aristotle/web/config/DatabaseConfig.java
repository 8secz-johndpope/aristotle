package com.aristotle.web.config;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = { "com.aristotle.core.persistance.repo" })
@EntityScan(basePackages = { "com.aristotle.core.persistance" })
public class DatabaseConfig {

}
