package com.aristotle.core.service;

import java.util.List;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Domain;
import com.aristotle.core.persistance.DomainPageTemplate;
import com.aristotle.core.persistance.DomainTemplatePart;

public interface UiTemplateService {

    List<Domain> getAllDomains() throws AppException;

    List<DomainPageTemplate> getCurrentDomainPageTemplate(Long domainId) throws AppException;

    List<DomainTemplatePart> getDomainTemplatePartsByDomainTemplateId(Long domainTemplateId) throws AppException;

}
