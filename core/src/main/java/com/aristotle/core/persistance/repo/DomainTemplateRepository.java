package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.DomainTemplate;

public interface DomainTemplateRepository extends JpaRepository<DomainTemplate, Long> {

}
