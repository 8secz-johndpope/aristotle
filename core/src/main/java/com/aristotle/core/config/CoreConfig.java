package com.aristotle.core.config;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.aristotle.core.audit.SpringSecurityAuditorAware;

@Configuration
@EnableJpaRepositories(basePackages = { "com.aristotle.core.persistance.repo" })
@EntityScan(basePackages = { "com.aristotle.core.persistance" })
@EnableJpaAuditing
@ComponentScan("com.aristotle.core")
public class CoreConfig {

    @Bean
    public AuditorAware<Long> auditorProvider() {
        return new SpringSecurityAuditorAware();
    }
}
