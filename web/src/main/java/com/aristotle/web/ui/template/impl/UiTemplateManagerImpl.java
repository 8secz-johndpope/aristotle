package com.aristotle.web.ui.template.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aristotle.web.controller.HandleBarManager;
import com.github.jknack.handlebars.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private Map<String, Map<Long, Template>> domainUiCompileTemplateMap;

    private Map<String, Long> domainLocationMap;
    private Template errorTemplate;
    private Template exceptionTemplate;
    
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HandleBarManager handleBarManager;

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
                errorTemplate = handleBarManager.getHandlebars().compileInline("No Template Defined");
                exceptionTemplate = handleBarManager.getHandlebars().compileInline("InternalServer Error");

                domainUiTemplateMap = new HashMap<String, Map<Long, DomainPageTemplate>>();
                domainUiCompileTemplateMap = new HashMap<>();
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
                    Map<Long, Template> pageCompiledTemplates = new HashMap<Long, Template>();
                    Template compiledTemplate;
                    for (DomainPageTemplate oneDomainPageTemplate : domainPageTemplates) {
                        detachedDomainPageTemplate = new DomainPageTemplate();
                        BeanUtils.copyProperties(oneDomainPageTemplate, detachedDomainPageTemplate);
                        applySubTemplates(detachedDomainPageTemplate, subTemplates);
                        pageTemplates.put(detachedDomainPageTemplate.getUrlMappingId(), detachedDomainPageTemplate);
                        compiledTemplate = handleBarManager.getHandlebars().compileInline(detachedDomainPageTemplate.getHtmlContent());
                        pageCompiledTemplates.put(detachedDomainPageTemplate.getUrlMappingId(), compiledTemplate);
                    }
                    domainUiCompileTemplateMap.put(oneDomain.getName().toLowerCase(), pageCompiledTemplates);
                    domainUiTemplateMap.put(oneDomain.getName().toLowerCase(), pageTemplates);
                    if (oneDomain.getLocationId() == null) {
                        domainUiTemplateMap.put("default", pageTemplates);
                        domainUiCompileTemplateMap.put("default", pageCompiledTemplates);
                    }
                    if (otherDomainNames != null) {
                        for (String oneOtherDomainName : otherDomainNames) {
                            domainUiTemplateMap.put(oneOtherDomainName.toLowerCase(), pageTemplates);
                            domainUiCompileTemplateMap.put(oneOtherDomainName.toLowerCase(), pageCompiledTemplates);
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
    public Integer getCacheTime(HttpServletRequest httpServletRequest) {
        init();
        UrlMapping urlMapping = (UrlMapping) httpServletRequest.getAttribute(HttpParameters.URL_MAPPING);
        if (urlMapping == null) {
            return 300;
        }
        return urlMapping.getCacheTimeSeconds();
    }

    @Override
    public String getTemplate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        boolean requestForDraft = isRequestForDraft(httpServletRequest, httpServletResponse);
        if (requestForDraft) {
            refresh();// refresh if draft is requested
        }
        init();
        String domainName = httpServletRequest.getServerName();
        UrlMapping urlMapping = (UrlMapping) httpServletRequest.getAttribute(HttpParameters.URL_MAPPING);
        if (urlMapping == null) {
        	logger.info("No URL Mapping Found");
            return "No Template Defined";
        }
        DomainPageTemplate domainPageTemplate = getDomainPageTemplate(domainName, urlMapping.getId());
        if(domainPageTemplate == null){
        	logger.info("No Domain page Template Found");
            return "No Template Defined";
        }
        if (requestForDraft) {
            return domainPageTemplate.getHtmlContentDraft();
        }
        return domainPageTemplate.getHtmlContent();

    }

    @Override
    public Template getCompiledTemplate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        boolean requestForDraft = isRequestForDraft(httpServletRequest, httpServletResponse);
        if (requestForDraft) {
            refresh();// refresh if draft is requested
        }
        init();
        String domainName = httpServletRequest.getServerName();
        UrlMapping urlMapping = (UrlMapping) httpServletRequest.getAttribute(HttpParameters.URL_MAPPING);
        if (urlMapping == null) {
            logger.info("No URL Mapping Found");
            return errorTemplate;
        }
        DomainPageTemplate domainPageTemplate = getDomainPageTemplate(domainName, urlMapping.getId());
        if(domainPageTemplate == null){
            logger.info("No Domain page Template Found");
            return errorTemplate;
        }
        if (requestForDraft) {
            try {
                return handleBarManager.getHandlebars().compile(domainPageTemplate.getHtmlContentDraft());
            } catch (IOException e) {
                logger.error("Exception occured : ",e);
                return exceptionTemplate;
            }
        }

        Template domainPageCompiledTemplate = getDomainPageCompiledTemplate(domainName, urlMapping.getId());

        return domainPageCompiledTemplate;

    }

    private boolean isRequestForDraft(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String draftParamValue = httpServletRequest.getParameter("draft");
        if (draftParamValue == null) {
            Cookie[] cookies = httpServletRequest.getCookies();
            if (cookies != null && cookies.length > 0) {
                for (Cookie oneCookie : cookies) {
                    if (oneCookie.getName().equals("draft")) {
                        draftParamValue = oneCookie.getValue();
                    }
                }
            }
        } else {
            Cookie cookie = new Cookie("draft", draftParamValue);
            cookie.setPath("/");
            httpServletResponse.addCookie(cookie);
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
        	logger.info("Not found");
            return null;
        }
        return domainPageTemplateMap.get(urlMappingId);
    }
    private Template getDomainPageCompiledTemplate(String domain, Long urlMappingId) {
        Map<Long, Template> domainPageTemplateMap = domainUiCompileTemplateMap.get(domain.toLowerCase());
        if (domainPageTemplateMap == null) {
            domainPageTemplateMap = domainUiCompileTemplateMap.get("default");
        }
        if (domainPageTemplateMap == null) {
            logger.info("Not found");
            return null;
        }
        return domainPageTemplateMap.get(urlMappingId);
    }

    @Override
    public Long getDomainLocation(HttpServletRequest httpServletRequest) {
        init();
        String domain = httpServletRequest.getServerName().toLowerCase();
        return domainLocationMap.get(domain);
    }

}
