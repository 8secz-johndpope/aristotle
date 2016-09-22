package com.aristotle.web.plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.aristotle.core.persistance.UrlMapping;

public class PatternUrlMapping {
    private final UrlMapping urlMapping;
    private Pattern pattern;
    private final List<String> parameters;
    private final List<String> aliases;
    private final List<WebDataPlugin> dataPlugins;
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public PatternUrlMapping(UrlMapping urlMapping, List<WebDataPlugin> dataPlugins) {
        this.dataPlugins = dataPlugins;
        this.urlMapping = urlMapping;
        parameters = new ArrayList<String>();
        aliases = new ArrayList<String>();
        if (urlMapping.getAliases() != null && urlMapping.getAliases().length() > 0) {
            String[] aliasesArray = StringUtils.commaDelimitedListToStringArray(urlMapping.getAliases());
            aliases.addAll(Arrays.asList(aliasesArray));
        }

        pattern = null;
        // Build parameters and pattern
        char[] charArray = urlMapping.getUrlPattern().toCharArray();
        StringBuilder sb = new StringBuilder();
        boolean paramStarted = false;
        StringBuilder paramNameBuilder = new StringBuilder();
        for (char oneChar : charArray) {
            if (oneChar == '{') {
                paramStarted = true;
                paramNameBuilder = new StringBuilder();
                continue;
            }
            if (oneChar == '}') {
                paramStarted = false;
                sb.append("(.*)");
                parameters.add(paramNameBuilder.toString());
                continue;
            }
            if (paramStarted) {
                paramNameBuilder.append(oneChar);
                continue;
            }
            sb.append(oneChar);
        }
        if (!parameters.isEmpty()) {
        	logger.info("pattern = {}" , sb.toString());
            pattern = Pattern.compile(sb.toString());
        }
    }

    public UrlMapping getUrlMapping() {
        return urlMapping;
    }

    @Override
    public String toString() {
        return "CustomUrlMapping [urlMapping=" + urlMapping + ", pattern=" + pattern + ", parameters=" + parameters + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((urlMapping.getUrlPattern() == null) ? 0 : urlMapping.getUrlPattern().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PatternUrlMapping other = (PatternUrlMapping) obj;
        if (urlMapping.getUrlPattern() == null) {
            if (other.urlMapping.getUrlPattern() != null)
                return false;
        } else if (!urlMapping.getUrlPattern().equals(other.urlMapping.getUrlPattern()))
            return false;
        return true;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public List<WebDataPlugin> getDataPlugins() {
        return dataPlugins;
    }

    public List<String> getAliases() {
        return aliases;
    }

}
