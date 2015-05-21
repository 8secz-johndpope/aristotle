package com.aristotle.admin;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { "com.aristotle.core.service", "com.aristotle.core.service.temp" })
public class ServiceConfig {

}
