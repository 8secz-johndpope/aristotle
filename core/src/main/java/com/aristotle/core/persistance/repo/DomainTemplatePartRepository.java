package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.DomainTemplatePart;

public interface DomainTemplatePartRepository extends JpaRepository<DomainTemplatePart, Long> {

    List<DomainTemplatePart> getDomainTemplatePartsByDomainTemplateId(Long domainTemplateId);
}
