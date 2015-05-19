package com.aristotle.web.ui.template.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristotle.core.persistance.Domain;
import com.aristotle.core.persistance.DomainPageTemplate;
import com.aristotle.core.persistance.UrlMapping;
import com.aristotle.core.service.UiTemplateService;
import com.aristotle.web.parameters.HttpParameters;
import com.aristotle.web.ui.template.UiTemplateManager;

@Service
public class UiTemplateManagerImpl implements UiTemplateManager {

    private Map<String, Map<Long, DomainPageTemplate>> domainUiTemplateMap;
    @Autowired
    private UiTemplateService uiTemplateService;

    private volatile boolean isInitialised = false;
    private void init() {
        if (isInitialised) {
            return;
        }
        synchronized (this) {
            if (isInitialised) {
                return;
            }
            try {
                domainUiTemplateMap = new HashMap<String, Map<Long, DomainPageTemplate>>();
                List<Domain> domains = uiTemplateService.getAllDomains();
                for (Domain oneDomain : domains) {
                    List<DomainPageTemplate> domainPageTemplates = uiTemplateService.getCurrentDomainPageTemplate(oneDomain.getId());
                    Map<Long, DomainPageTemplate> pageTemplates = new HashMap<Long, DomainPageTemplate>();
                    for (DomainPageTemplate oneDomainPageTemplate : domainPageTemplates) {
                        pageTemplates.put(oneDomainPageTemplate.getUrlMappingId(), oneDomainPageTemplate);
                    }
                    domainUiTemplateMap.put(oneDomain.getName().toLowerCase(), pageTemplates);
                }
                isInitialised = true;

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }


    }
    @Override
    public String getTemplate(HttpServletRequest httpServletRequest) {
        init();
        String domainName = httpServletRequest.getServerName();
        UrlMapping urlMapping = (UrlMapping) httpServletRequest.getAttribute(HttpParameters.URL_MAPPING);

        DomainPageTemplate domainPageTemplate = getDomainPageTemplate(domainName, urlMapping.getId());
        if(domainPageTemplate == null){
            return "No Template Defined";
        }
        return domainPageTemplate.getHtmlContent();
    }

    private DomainPageTemplate getDomainPageTemplate(String domain, Long urlMappingId) {
        Map<Long, DomainPageTemplate> domainPageTemplateMap = domainUiTemplateMap.get(domain.toLowerCase());
        if (domainPageTemplateMap == null) {
            return null;
        }
        return domainPageTemplateMap.get(urlMappingId);
    }

}
