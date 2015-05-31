package com.aristotle.core.service;

import java.util.List;
import java.util.Map;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.DataPlugin;
import com.aristotle.core.persistance.DomainPageTemplate;
import com.aristotle.core.persistance.DomainTemplate;
import com.aristotle.core.persistance.DomainTemplateFile;
import com.aristotle.core.persistance.UrlMapping;

public interface DataPluginService {

    List<UrlMapping> getAllUrlMappings() throws AppException;

    UrlMapping saveUrlMapping(UrlMapping urlMapping) throws AppException;

    void addDataPluginForUrlMapping(Long urlMappingId, List<DataPlugin> dataPlugins) throws AppException;

    Map<DataPlugin, String> getDataPluginsForUrlMapping(Long urlMappingId) throws AppException;

    List<DataPlugin> getDataPluginsByUrlMappingId(Long urlMappingId) throws AppException;

    List<DataPlugin> getAllDataPlugins() throws AppException;

    List<DataPlugin> getAllGlobalDataPlugins() throws AppException;
    
    List<DataPlugin> getAllNonGlobalDataPlugins() throws AppException;

    List<DomainTemplate> getAllDomainTemplates(Long locationId) throws AppException;

    DomainTemplate saveDomainTemplate(DomainTemplate domainTemplate) throws AppException;

    DomainPageTemplate saveDomainPageTemplate(DomainPageTemplate domainPageTemplate) throws AppException;

    DomainTemplateFile saveDomainTemplateFile(Long domainTemplateId, String filePathAndName, long fileSize) throws AppException;

    void createAllCustomDataPlugins(List<String> classNames) throws AppException;
}
