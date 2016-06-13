package com.aristotle.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.aristotle.core.persistance.Customer;
import com.aristotle.core.persistance.repo.CustomerRepository;

@SpringBootApplication
public class App implements CommandLineRunner
{
    @Autowired
    CustomerRepository repository;

    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        SpringApplication.run(App.class);
    }

    @Override
    public void run(String... strings) throws Exception {
    	
    }
}
