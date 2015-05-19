package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.DomainPageTemplate;

public interface PageTemplateRepository extends JpaRepository<DomainPageTemplate, Long> {

}
