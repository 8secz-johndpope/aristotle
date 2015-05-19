package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.Domain;

public interface DomainRepository extends JpaRepository<Domain, Long> {

}
