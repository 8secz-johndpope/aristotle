package com.aristotle.web.ui.template.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.aristotle.core.persistance.Domain;
import com.aristotle.core.persistance.DomainPageTemplate;
import com.aristotle.core.persistance.DomainTemplatePart;
import com.aristotle.core.persistance.UrlMapping;
import com.aristotle.core.service.UiTemplateService;
import com.aristotle.web.parameters.HttpParameters;
import com.aristotle.web.ui.template.UiTemplateManager;

@Service
public class UiTemplateManagerImpl implements UiTemplateManager {

    private Map<String, Map<Long, DomainPageTemplate>> domainUiTemplateMap;

    private Map<String, Long> domainLocationMap;

    @Autowired
    private UiTemplateService uiTemplateService;

    private volatile boolean isInitialised = false;

    @Override
    public void refresh() {
        isInitialised = false;
        init();
    }

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
                domainLocationMap = new HashMap<String, Long>();
                List<Domain> domains = uiTemplateService.getAllDomains();
                DomainPageTemplate detachedDomainPageTemplate;
                for (Domain oneDomain : domains) {
                    List<DomainPageTemplate> domainPageTemplates = uiTemplateService.getCurrentDomainPageTemplate(oneDomain.getId());
                    domainLocationMap.put(oneDomain.getName().toLowerCase(), oneDomain.getLocationId());
                    String[] otherDomainNames = null;
                    if (!StringUtils.isEmpty(oneDomain.getNameAliases())) {
                        otherDomainNames = StringUtils.commaDelimitedListToStringArray(oneDomain.getNameAliases());
                        for (String oneOtherDomainName : otherDomainNames) {
                            domainLocationMap.put(oneOtherDomainName.toLowerCase(), oneDomain.getLocationId());
                        }
                    }
                    if (domainPageTemplates == null || domainPageTemplates.isEmpty()) {
                        continue;
                    }
                    Long domainTemplateId = domainPageTemplates.get(0).getDomainTemplateId();
                    List<DomainTemplatePart> subTemplates = uiTemplateService.getDomainTemplatePartsByDomainTemplateId(domainTemplateId);
                    Map<Long, DomainPageTemplate> pageTemplates = new HashMap<Long, DomainPageTemplate>();
                    for (DomainPageTemplate oneDomainPageTemplate : domainPageTemplates) {
                        detachedDomainPageTemplate = new DomainPageTemplate();
                        BeanUtils.copyProperties(oneDomainPageTemplate, detachedDomainPageTemplate);
                        applySubTemplates(detachedDomainPageTemplate, subTemplates);
                        pageTemplates.put(detachedDomainPageTemplate.getUrlMappingId(), detachedDomainPageTemplate);
                    }

                    domainUiTemplateMap.put(oneDomain.getName().toLowerCase(), pageTemplates);
                    if (oneDomain.getLocationId() == null) {
                        domainUiTemplateMap.put("default", pageTemplates);
                    }
                    if (otherDomainNames != null) {
                        for (String oneOtherDomainName : otherDomainNames) {
                            domainUiTemplateMap.put(oneOtherDomainName.toLowerCase(), pageTemplates);
                        }
                    }
                }
                isInitialised = true;

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }


    }

    private void applySubTemplates(DomainPageTemplate oneDomainPageTemplate, List<DomainTemplatePart> subTemplates) {
        for (DomainTemplatePart oneDomainTemplatePart : subTemplates) {
            String templateKey = "[[" + oneDomainTemplatePart.getName() + "]]";
            String htmlContent = StringUtils.replace(oneDomainPageTemplate.getHtmlContent(), templateKey, oneDomainTemplatePart.getHtmlContent());
            oneDomainPageTemplate.setHtmlContent(htmlContent);

            String htmlContentDraft = StringUtils.replace(oneDomainPageTemplate.getHtmlContentDraft(), templateKey, oneDomainTemplatePart.getHtmlContentDraft());
            oneDomainPageTemplate.setHtmlContentDraft(htmlContentDraft);
        }
    }

    @Override
    public String getTemplate(HttpServletRequest httpServletRequest) {
        boolean requestForDraft = isRequestForDraft(httpServletRequest);
        if (requestForDraft) {
            refresh();// refresh if draft is requested
        }
        init();
        String domainName = httpServletRequest.getServerName();
        UrlMapping urlMapping = (UrlMapping) httpServletRequest.getAttribute(HttpParameters.URL_MAPPING);
        if (urlMapping == null) {
            System.out.println("No URL Mapping Found");
            return "No Template Defined";
        }
        DomainPageTemplate domainPageTemplate = getDomainPageTemplate(domainName, urlMapping.getId());
        if(domainPageTemplate == null){
            System.out.println("No Domain page Template Found");
            return "No Template Defined";
        }
        if (requestForDraft) {
            return domainPageTemplate.getHtmlContentDraft();
        }
        return domainPageTemplate.getHtmlContent();

    }

    private boolean isRequestForDraft(HttpServletRequest httpServletRequest) {
        String draftParamValue = httpServletRequest.getParameter("draft");
        if (draftParamValue == null) {
            draftParamValue = (String) httpServletRequest.getSession().getAttribute("draft");
        } else {
            httpServletRequest.getSession().setAttribute("draft", draftParamValue);
        }
        if (draftParamValue == null || !draftParamValue.equals("1")) {
            return false;
        }
        return true;
    }

    private DomainPageTemplate getDomainPageTemplate(String domain, Long urlMappingId) {
        Map<Long, DomainPageTemplate> domainPageTemplateMap = domainUiTemplateMap.get(domain.toLowerCase());
        if (domainPageTemplateMap == null) {
            domainPageTemplateMap = domainUiTemplateMap.get("default");
        }
        if (domainPageTemplateMap == null) {
            System.out.println("Not found");
            return null;
        }
        return domainPageTemplateMap.get(urlMappingId);
    }

    @Override
    public Long getDomainLocation(HttpServletRequest httpServletRequest) {
        init();
        String domain = httpServletRequest.getServerName().toLowerCase();
        System.out.println("Getting Domain location for " + domain);
        return domainLocationMap.get(domain);
    }

}
