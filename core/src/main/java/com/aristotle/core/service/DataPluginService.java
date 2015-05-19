package com.aristotle.core.service;

import java.util.List;
import java.util.Map;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.DataPlugin;
import com.aristotle.core.persistance.UrlMapping;

public interface DataPluginService {

    List<UrlMapping> getAllUrlMappings() throws AppException;

    Map<DataPlugin, String> getDataPluginsForUrlMapping(Long urlMappingId) throws AppException;
}
