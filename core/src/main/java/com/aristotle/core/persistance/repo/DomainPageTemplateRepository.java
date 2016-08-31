package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aristotle.core.persistance.DomainPageTemplate;

public interface DomainPageTemplateRepository extends JpaRepository<DomainPageTemplate, Long> {

    @Query("select dpt from DomainPageTemplate dpt where dpt.domainTemplate.active = true and dpt.domainTemplate.domainId = ?1")
    List<DomainPageTemplate> getDomainPageTemplateOfActiveTemplate(Long domainId);
    
    
    @Query("select dpt from DomainPageTemplate dpt where dpt.domainTemplateId=?2 and dpt.urlMapping.urlPattern = ?1")
    DomainPageTemplate getDomainPageTemplateByUrlAndDomainTemplate(String url, Long domainTemplateId);
}
