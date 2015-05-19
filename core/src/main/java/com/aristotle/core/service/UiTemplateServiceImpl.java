package com.aristotle.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Domain;
import com.aristotle.core.persistance.DomainPageTemplate;
import com.aristotle.core.persistance.repo.DomainPageTemplateRepository;
import com.aristotle.core.persistance.repo.DomainRepository;

@Service
public class UiTemplateServiceImpl implements UiTemplateService {

    @Autowired
    private DomainRepository domainRepository;
    @Autowired
    private DomainPageTemplateRepository domainPageTemplateRepository;

    @Override
    public List<Domain> getAllDomains() throws AppException {
        return domainRepository.findAll();
    }

    @Override
    public List<DomainPageTemplate> getCurrentDomainPageTemplate(Long domainId) throws AppException {
        return domainPageTemplateRepository.getDomainPageTemplateOfActiveTemplate(domainId);
    }

}
