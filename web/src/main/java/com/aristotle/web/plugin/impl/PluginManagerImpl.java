package com.aristotle.web.plugin.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.CustomDataPlugin;
import com.aristotle.core.persistance.DataPlugin;
import com.aristotle.core.persistance.StaticDataPlugin;
import com.aristotle.core.persistance.UrlMapping;
import com.aristotle.core.persistance.UrlMappingPlugin;
import com.aristotle.core.service.DataPluginService;
import com.aristotle.web.parameters.HttpParameters;
import com.aristotle.web.plugin.PatternUrlMapping;
import com.aristotle.web.plugin.PluginManager;
import com.aristotle.web.plugin.WebDataPlugin;
import com.google.gson.JsonParser;

@Service
@Transactional
public class PluginManagerImpl implements PluginManager {

    @Autowired
    private DataPluginService dataPluginService;
    @Autowired
    private BeanFactory springBeanFactory;

    @Autowired
    private ApplicationContext applicationContext;

    private List<PatternUrlMapping> urlPatterns;
    private List<WebDataPlugin> globalWebDataPlugins;
    private volatile boolean isInitialized = false;

    @Override
    public void refresh() {
        isInitialized = false;
        init();
    }

    public void init() {
        if (isInitialized) {
            return;
        }
        synchronized (this) {
            if(isInitialized){
                return;
            }
            try {

                JsonParser jsonParser = new JsonParser();
                loadGlobalDataPlugins(jsonParser);
                List<UrlMapping> urlMappings = dataPluginService.getAllUrlMappings();
                urlPatterns = new ArrayList<PatternUrlMapping>();
                List<WebDataPlugin> dataPlugins;
                WebDataPlugin oneWebDataPlugin;
                for (UrlMapping oneUrlMapping : urlMappings) {
                    dataPlugins = new ArrayList<WebDataPlugin>();
                    for (UrlMappingPlugin oneUrlMappingPlugin : oneUrlMapping.getUrlMappingPlugins()) {
                        if (oneUrlMappingPlugin.getDataPlugin().isDisabled()) {
                            continue;
                        }
                        if (oneUrlMappingPlugin.getDataPlugin().isGlobal()) {
                            continue;
                        }
                        oneWebDataPlugin = createDataPlugin(oneUrlMappingPlugin.getDataPlugin(), jsonParser);
                        if (oneWebDataPlugin != null) {
                            dataPlugins.add(oneWebDataPlugin);
                        }
                        String setting = oneUrlMappingPlugin.getSetting();
                        oneWebDataPlugin.setSettings(setting);
                    }
                    PatternUrlMapping onePatternUrlMapping = new PatternUrlMapping(oneUrlMapping, dataPlugins);
                    urlPatterns.add(onePatternUrlMapping);
                    isInitialized = true;
                }
            } catch (AppException e) {
                e.printStackTrace();
            }

        }

    }

    private void loadGlobalDataPlugins(JsonParser jsonParser) throws AppException {
        List<DataPlugin> globalDataPlugins = dataPluginService.getAllGlobalDataPlugins();
        
        for(DataPlugin oneDataPlugin : globalDataPlugins){
            WebDataPlugin oneWebDataPlugin = createDataPlugin(oneDataPlugin, jsonParser);
            oneWebDataPlugin.setSettings("{}");// just empty valid Json
            globalWebDataPlugins.add(oneWebDataPlugin);

        }
    }

    private WebDataPlugin createDataPlugin(CustomDataPlugin customDataPlugin, JsonParser jsonParser) {
        Class<?> clz;
        try {
            clz = Class.forName(customDataPlugin.getFullClassName());
            WebDataPlugin dataPlugin = (WebDataPlugin) springBeanFactory.getBean(clz, customDataPlugin.getPluginName());
            return dataPlugin;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private WebDataPlugin createDataPlugin(StaticDataPlugin staticDataPlugin, JsonParser jsonParser) {
        WebStaticDataPlugin webStaticDataPlugin = new WebStaticDataPlugin(jsonParser.parse(staticDataPlugin.getContent()).getAsJsonObject(), staticDataPlugin.getPluginName());
        return webStaticDataPlugin;

    }

    private WebDataPlugin createDataPlugin(DataPlugin dataPlugin, JsonParser jsonParser) {
        if (dataPlugin instanceof CustomDataPlugin) {
            return createDataPlugin((CustomDataPlugin) dataPlugin, jsonParser);
        }
        if (dataPlugin instanceof StaticDataPlugin) {
            return createDataPlugin((StaticDataPlugin) dataPlugin, jsonParser);
        }
        throw new RuntimeException("Should never come here");
    }

    @Override
    public void applyAllPluginsForUrl(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView, boolean addData) {
        init();
        String requestedUrl = httpServletRequest.getRequestURI();
        System.out.println("Handling Url = " + requestedUrl);
        List<WebDataPlugin> plugins = findDataPlugins(httpServletRequest, requestedUrl);
        if (plugins == null || plugins.isEmpty()) {
            return;
        }
        if(addData){
            // First apply all Global Data Plugins
            for (WebDataPlugin oneWebDataPlugin : globalWebDataPlugins) {
                oneWebDataPlugin.applyPlugin(httpServletRequest, httpServletResponse, modelAndView);
            }
            for (WebDataPlugin oneWebDataPlugin : plugins) {
                oneWebDataPlugin.applyPlugin(httpServletRequest, httpServletResponse, modelAndView);
            }

        }
        
    }

    private List<WebDataPlugin> findDataPlugins(HttpServletRequest httpServletRequest, String url) {
        String apiUrl = url;
        if (url.startsWith("/api")) {
            apiUrl = url.replaceAll("/api", "");
        }
        for (PatternUrlMapping onePatternUrlMapping : urlPatterns) {
            Pattern r = onePatternUrlMapping.getPattern();
            if (r == null) {
                if (url.equalsIgnoreCase(onePatternUrlMapping.getUrlMapping().getUrlPattern()) || apiUrl.equalsIgnoreCase(onePatternUrlMapping.getUrlMapping().getUrlPattern())) {
                    httpServletRequest.setAttribute(HttpParameters.PATH_PARAMETER_PARAM, Collections.emptyMap());
                    httpServletRequest.setAttribute(HttpParameters.URL_MAPPING, onePatternUrlMapping.getUrlMapping());
                    return onePatternUrlMapping.getDataPlugins();
                }
                for (String oneUrl : onePatternUrlMapping.getAliases()) {
                    if (url.equalsIgnoreCase(oneUrl)) {
                        httpServletRequest.setAttribute(HttpParameters.PATH_PARAMETER_PARAM, Collections.emptyMap());
                        httpServletRequest.setAttribute(HttpParameters.URL_MAPPING, onePatternUrlMapping.getUrlMapping());
                        return onePatternUrlMapping.getDataPlugins();
                    }
                }
            } else {
                Matcher m = r.matcher(url);
                if (m.find()) {
                    Map<String, String> pathParameters = new LinkedHashMap<String, String>();
                    int count = 1;
                    for (String oneParam : onePatternUrlMapping.getParameters()) {
                        System.out.println("Found " + oneParam + ": " + m.group(count));
                        pathParameters.put(oneParam, m.group(count));
                        count++;
                    }

                    httpServletRequest.setAttribute(HttpParameters.PATH_PARAMETER_PARAM, pathParameters);
                    httpServletRequest.setAttribute(HttpParameters.URL_MAPPING, onePatternUrlMapping.getUrlMapping());
                    return onePatternUrlMapping.getDataPlugins();
                }
            }

        }
        return null;
    }

    @Override
    public void updateDbWithAllPlugins() throws AppException {
        Map<String, WebDataPlugin> allWebDataPlugins = applicationContext.getBeansOfType(WebDataPlugin.class);
        List<String> allPluginImplementations = new ArrayList<String>();
        for (Entry<String, WebDataPlugin> oneEntry : allWebDataPlugins.entrySet()) {
            if (oneEntry.getValue() instanceof WebStaticDataPlugin) {
                continue;
            }
            allPluginImplementations.add(oneEntry.getValue().getClass().getName());
            System.out.println(oneEntry.getValue().getClass().getName());
        }
        dataPluginService.createAllCustomDataPlugins(allPluginImplementations);
    }


}
