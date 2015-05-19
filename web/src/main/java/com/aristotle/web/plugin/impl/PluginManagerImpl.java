package com.aristotle.web.plugin.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

@Service
@Transactional
public class PluginManagerImpl implements PluginManager {

    @Autowired
    private DataPluginService dataPluginService;
    @Autowired
    private BeanFactory springBeanFactory;

    private List<PatternUrlMapping> urlPatterns;
    private volatile boolean isInitialized = false;

    public void init() {
        if (isInitialized) {
            return;
        }
        synchronized (this) {
            if(isInitialized){
                return;
            }
            try {
                List<UrlMapping> urlMappings = dataPluginService.getAllUrlMappings();
                urlPatterns = new ArrayList<PatternUrlMapping>();
                List<WebDataPlugin> dataPlugins;
                WebDataPlugin oneWebDataPlugin;
                for (UrlMapping oneUrlMapping : urlMappings) {
                    dataPlugins = new ArrayList<WebDataPlugin>();
                    for (UrlMappingPlugin oneUrlMappingPlugin : oneUrlMapping.getUrlMappingPlugins()) {
                        oneWebDataPlugin = createDataPlugin(oneUrlMappingPlugin.getDataPlugin());
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

    private WebDataPlugin createDataPlugin(CustomDataPlugin customDataPlugin) {
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

    private WebDataPlugin createDataPlugin(StaticDataPlugin staticDataPlugin) {
        WebStaticDataPlugin webStaticDataPlugin = new WebStaticDataPlugin(staticDataPlugin.getContent(), staticDataPlugin.getPluginName());
        return webStaticDataPlugin;

    }

    private WebDataPlugin createDataPlugin(DataPlugin dataPlugin) {
        if (dataPlugin instanceof CustomDataPlugin) {
            return createDataPlugin((CustomDataPlugin) dataPlugin);
        }
        if (dataPlugin instanceof StaticDataPlugin) {
            return createDataPlugin((StaticDataPlugin) dataPlugin);
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
                    httpServletRequest.setAttribute(HttpParameters.URL_MAPPING, onePatternUrlMapping.getUrlMapping());
                    return onePatternUrlMapping.getDataPlugins();
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

}
