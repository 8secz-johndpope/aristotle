package com.aristotle.core.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.CustomDataPlugin;
import com.aristotle.core.persistance.DataPlugin;
import com.aristotle.core.persistance.Domain;
import com.aristotle.core.persistance.DomainPageTemplate;
import com.aristotle.core.persistance.DomainTemplate;
import com.aristotle.core.persistance.DomainTemplateFile;
import com.aristotle.core.persistance.UrlMapping;
import com.aristotle.core.persistance.UrlMappingPlugin;
import com.aristotle.core.persistance.repo.CustomDataPluginRepository;
import com.aristotle.core.persistance.repo.DataPluginRepository;
import com.aristotle.core.persistance.repo.DomainPageTemplateRepository;
import com.aristotle.core.persistance.repo.DomainRepository;
import com.aristotle.core.persistance.repo.DomainTemplateFileRepository;
import com.aristotle.core.persistance.repo.DomainTemplateRepository;
import com.aristotle.core.persistance.repo.UrlMappingPluginRepository;
import com.aristotle.core.persistance.repo.UrlMappingRepository;

@Service
@Transactional(rollbackFor={Throwable.class})
public class DataPluginServiceImpl implements DataPluginService {

    @Autowired
    private UrlMappingRepository urlMappingRepository;
    @Autowired
    private UrlMappingPluginRepository urlMappingPluginRepository;
    @Autowired
    private DomainRepository domainRepository;
    @Autowired
    private DomainTemplateRepository domainTemplateRepository;
    @Autowired
    private DomainPageTemplateRepository domainPageTemplateRepository;
    @Autowired
    private DomainTemplateFileRepository domainTemplateFileRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private DataPluginRepository dataPluginRepository;
    @Autowired
    private CustomDataPluginRepository customDataPluginRepository;

    @Override
    public List<UrlMapping> getAllUrlMappings() throws AppException {
        return urlMappingRepository.findAll();
    }

    @Override
    public Map<DataPlugin, String> getDataPluginsForUrlMapping(Long urlMappingId) throws AppException {
        return null;
    }

    @Override
    public List<DomainTemplate> getAllDomainTemplates(Long locationId) throws AppException {
        if (locationId == null) {
            return domainTemplateRepository.getGlobalDomainTemplates();
        }
        return domainTemplateRepository.getLocationDomainTemplates(locationId);
    }

    @Override
    public DomainTemplate saveDomainTemplate(DomainTemplate domainTemplate) throws AppException {
        entityManager.merge(domainTemplate);
        Domain domain = domainRepository.findOne(domainTemplate.getDomainId());
        domainTemplate.setDomain(domain);
        domainTemplate = domainTemplateRepository.save(domainTemplate);
        return domainTemplate;
    }

    @Override
    public DomainPageTemplate saveDomainPageTemplate(DomainPageTemplate domainPageTemplate) throws AppException {
        domainPageTemplate = entityManager.merge(domainPageTemplate);
        UrlMapping urlMapping = entityManager.merge(domainPageTemplate.getUrlMapping());
        domainPageTemplate.setUrlMapping(urlMapping);
        DomainTemplate domainTemplate = domainTemplateRepository.findOne(domainPageTemplate.getDomainTemplateId());
        domainPageTemplate.setDomainTemplate(domainTemplate);

        domainPageTemplate = domainPageTemplateRepository.save(domainPageTemplate);
        return domainPageTemplate;
    }

    @Override
    public DomainTemplateFile saveDomainTemplateFile(Long domainTemplateId, String filePathAndName, long fileSize) throws AppException {
    	System.out.println("Getting Domain Template for id : "+ domainTemplateId);
        DomainTemplate domainTemplate = domainTemplateRepository.findOne(domainTemplateId);
    	System.out.println("Domain Template : "+ domainTemplate);

        filePathAndName = filePathAndName.toLowerCase();
    	System.out.println("Getting Domain Template file for filePathAndName : "+ filePathAndName);
        DomainTemplateFile domainTemplateFile = domainTemplateFileRepository.getDomainTemplateFileByFileName(filePathAndName);
    	System.out.println("Domain Template File : "+ domainTemplateFile);

        if (domainTemplateFile == null) {
            domainTemplateFile = new DomainTemplateFile();
            domainTemplateFile.setFileName(filePathAndName);
        }
        domainTemplateFile.setSize(fileSize);
        domainTemplateFile.setDomainTemplate(domainTemplate);
    	System.out.println("Saving Domain Template file for filePathAndName : "+ domainTemplateFile);

        domainTemplateFile = domainTemplateFileRepository.save(domainTemplateFile);
        return domainTemplateFile;
    }

    @Override
    public UrlMapping saveUrlMapping(UrlMapping urlMapping) throws AppException {
        return urlMappingRepository.save(urlMapping);
    }

    @Override
    public List<DataPlugin> getDataPluginsByUrlMappingId(Long urlMappingId) throws AppException {
        return dataPluginRepository.getDataPluginOfUrlMapping(urlMappingId);
    }

    @Override
    public List<DataPlugin> getAllDataPlugins() throws AppException {
        return dataPluginRepository.findAll();
    }

    @Override
    public void addDataPluginForUrlMapping(Long urlMappingId, List<DataPlugin> dataPlugins) throws AppException {
        UrlMapping urlMapping = urlMappingRepository.findOne(urlMappingId);
        if (urlMapping.getUrlMappingPlugins() == null) {
            urlMapping.setUrlMappingPlugins(new ArrayList<UrlMappingPlugin>());
        }

        boolean existing;
        for (DataPlugin oneDataPlugin : dataPlugins) {
            existing = false;
            for (UrlMappingPlugin oneUrlMappingPlugin : urlMapping.getUrlMappingPlugins()) {
                if (oneUrlMappingPlugin.getDataPlugin().getId().equals(oneDataPlugin.getId())) {
                    existing = true;
                }
            }
            if (existing) {
                continue;
            }
            oneDataPlugin = entityManager.merge(oneDataPlugin);
            UrlMappingPlugin oneUrlMappingPlugin = new UrlMappingPlugin();
            oneUrlMappingPlugin.setUrlMapping(urlMapping);
            oneUrlMappingPlugin.setDataPlugin(oneDataPlugin);
            oneUrlMappingPlugin = urlMappingPluginRepository.save(oneUrlMappingPlugin);
        }

        for (UrlMappingPlugin oneUrlMappingPlugin : urlMapping.getUrlMappingPlugins()) {
            existing = false;
            for (DataPlugin oneDataPlugin : dataPlugins) {
                if (oneUrlMappingPlugin.getDataPlugin().getId().equals(oneDataPlugin.getId())) {
                    existing = true;
                }
            }
            if (existing) {
                continue;
            }
            urlMappingPluginRepository.delete(oneUrlMappingPlugin);
        }

    }

    @Override
    public void createAllCustomDataPlugins(List<String> classNames) throws AppException {
        List<CustomDataPlugin> allCustomDataPlugins = customDataPluginRepository.findAll();
        Set<String> allExistingPluginClassNames = new HashSet<String>();
        for (CustomDataPlugin oneCustomDataPlugin : allCustomDataPlugins) {
            allExistingPluginClassNames.add(oneCustomDataPlugin.getFullClassName());
        }
        for(String oneClass : classNames){
            if (allExistingPluginClassNames.contains(oneClass)) {
                continue;
            }
            CustomDataPlugin customDataPlugin = new CustomDataPlugin();
            customDataPlugin.setFullClassName(oneClass);
            customDataPlugin.setDisabled(false);
            customDataPlugin.setPluginName(getClassName(oneClass));
            customDataPlugin = customDataPluginRepository.save(customDataPlugin);
        }
        
    }

    private String getClassName(String fullClassName) {
        return fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
    }

    @Override
    public List<DataPlugin> getAllGlobalDataPlugins() throws AppException {
        return dataPluginRepository.getAllGlobalDataPlugins();
    }

    @Override
    public List<DataPlugin> getAllNonGlobalDataPlugins() throws AppException {
        return dataPluginRepository.getAllNonGlobalDataPlugins();
    }

	@Override
	public DomainPageTemplate getDomainPageTemplateByUrlAndDomainTemplate(String url, Long domainTemplateId) throws AppException {
		domainPageTemplateRepository.getDomainPageTemplateByUrlAndDomainTemplate(url, domainTemplateId);
		return null;
	}
}
