package com.aristotle.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.DataPlugin;
import com.aristotle.core.persistance.UrlMapping;
import com.aristotle.core.persistance.repo.UrlMappingPluginRepository;
import com.aristotle.core.persistance.repo.UrlMappingRepository;

@Service
public class DataPluginServiceImpl implements DataPluginService {

    @Autowired
    private UrlMappingRepository urlMappingRepository;
    @Autowired
    private UrlMappingPluginRepository urlMappingPluginRepository;

    @Override
    public List<UrlMapping> getAllUrlMappings() throws AppException {
        return urlMappingRepository.findAll();
    }

    @Override
    public Map<DataPlugin, String> getDataPluginsForUrlMapping(Long urlMappingId) throws AppException {
        return null;
    }

}
