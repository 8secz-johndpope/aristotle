package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.aristotle.core.persistance.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    List<Customer> findByLastName(String lastName);
}