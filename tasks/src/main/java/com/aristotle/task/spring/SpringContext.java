package com.aristotle.task.spring;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.aristotle.core.config.DatabaseConfig;

@SpringBootApplication()
@ComponentScan(basePackages = { "com.aristotle.core.service", "com.aristotle.core.service.aws" })
public class SpringContext implements CommandLineRunner
{
    private static volatile ConfigurableApplicationContext context;

    public static void initContext()
    {
        if (context == null) {
            synchronized (SpringContext.class) {
                if (context == null) {
                    System.out.println("Creating Context");
                    context = new SpringApplicationBuilder(SpringContext.class, DatabaseConfig.class).web(false).run(new String[0]);
                    // context = SpringApplication.run(SpringContext.class);
                }
            }
        }
    }

    public static ConfigurableApplicationContext getContext() {
        initContext();
        return context;
    }

    public static <T> T getBean(Class<T> clazz) {

        return getContext().getBean(clazz);
    }

    @Override
    public void run(String... strings) throws Exception {
    }
}
