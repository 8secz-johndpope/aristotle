package com.aristotle.web.plugin.impl;

import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.aristotle.core.persistance.News;
import com.aristotle.web.parameters.HttpParameters;
import com.aristotle.web.plugin.WebDataPlugin;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public abstract class AbstractDataPlugin implements WebDataPlugin {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected JsonObject settingJsonObject;
    protected final String name;

    public AbstractDataPlugin(String pluginName) {
        this.name = pluginName;
    }

    protected Pageable getPageRequest(HttpServletRequest httpServletRequest) {
        int page = getIntPramater(httpServletRequest, HttpParameters.PAGE_NUMBER_PARAM, HttpParameters.PAGE_NUMBER_DEFAULT_VALUE);
        int size = getIntPramater(httpServletRequest, HttpParameters.PAGE_SIZE_PARAM, HttpParameters.PAGE_SIZE_DEFAULT_VALUE);
        Pageable pageable = new PageRequest(page, size);
        return pageable;
    }

    @Override
    public void setSettings(String settings) {
        JsonParser jsonParser = new JsonParser();
        try {
            settingJsonObject = (JsonObject) jsonParser.parse(settings);
        } catch (Exception ex) {
            logger.error("Invalid Setting for Plugin {}", name);
            settingJsonObject = new JsonObject();
        }

    }

    protected int getIntPramater(HttpServletRequest httpServletRequest, String paramName, int defaultValue) {
        try {
            return Integer.parseInt(httpServletRequest.getParameter(paramName));
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    protected long getLongPramater(HttpServletRequest httpServletRequest, String paramName, int defaultValue) {
        try {
            return Long.parseLong(httpServletRequest.getParameter(paramName));
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    public String getName() {
        return name;
    }

    public void addDateField(JsonObject jsonObject, String fieldName, Date fieldValue) {
        if (fieldValue == null) {
            jsonObject.addProperty(fieldName, "");
            return;
        }
        DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
        jsonObject.addProperty(fieldName, fmt.print(fieldValue.getTime()));
    }

    protected JsonObject convertNews(News news) {
        JsonObject newsJsonObject = new JsonObject();
        newsJsonObject.addProperty("id", news.getId());
        newsJsonObject.addProperty("author", news.getAuthor());
        newsJsonObject.addProperty("content", news.getContent());
        newsJsonObject.addProperty("imageUrl", news.getImageUrl());
        newsJsonObject.addProperty("source", news.getSource());
        newsJsonObject.addProperty("title", news.getTitle());


        String contentWithOutHtml = news.getContent().replaceAll("\\<[^>]*>", "");
        newsJsonObject.addProperty("contentSummary", contentWithOutHtml);

        addDateField(newsJsonObject, "publishDate", news.getPublishDate());
        return newsJsonObject;
    }

    protected JsonArray convertNewsList(Collection<News> newsList) {
        JsonArray jsonArray = new JsonArray();
        if (newsList == null) {
            return jsonArray;
        }
        for (News oneNews : newsList) {
            JsonObject newsJsonObject = convertNews(oneNews);
            jsonArray.add(newsJsonObject);
        }
        return jsonArray;
    }
}
