package com.aristotle.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Domain;
import com.aristotle.core.persistance.DomainPageTemplate;
import com.aristotle.core.persistance.DomainTemplatePart;
import com.aristotle.core.persistance.repo.DomainPageTemplateRepository;
import com.aristotle.core.persistance.repo.DomainRepository;
import com.aristotle.core.persistance.repo.DomainTemplatePartRepository;

@Service
public class UiTemplateServiceImpl implements UiTemplateService {

    @Autowired
    private DomainRepository domainRepository;
    @Autowired
    private DomainPageTemplateRepository domainPageTemplateRepository;
    @Autowired
    private DomainTemplatePartRepository domainTemplatePartRepository;

    @Override
    public List<Domain> getAllDomains() throws AppException {
        return domainRepository.findAll();
    }

    @Override
    public List<DomainPageTemplate> getCurrentDomainPageTemplate(Long domainId) throws AppException {
        return domainPageTemplateRepository.getDomainPageTemplateOfActiveTemplate(domainId);
    }

    @Override
    public List<DomainTemplatePart> getDomainTemplatePartsByDomainTemplateId(Long domainTemplateId) throws AppException {
        return domainTemplatePartRepository.getDomainTemplatePartsByDomainTemplateId(domainTemplateId);
    }

}
