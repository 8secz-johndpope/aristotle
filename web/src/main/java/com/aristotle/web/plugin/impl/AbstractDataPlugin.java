package com.aristotle.web.plugin.impl;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.aristotle.core.persistance.Blog;
import com.aristotle.core.persistance.Event;
import com.aristotle.core.persistance.Location;
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

    public AbstractDataPlugin() {
        this("NoName");
    }

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

    protected JsonObject convertBlog(Blog blog) {
        JsonObject blogJsonObject = new JsonObject();
        blogJsonObject.addProperty("id", blog.getId());
        blogJsonObject.addProperty("author", blog.getAuthor());
        blogJsonObject.addProperty("content", blog.getContent());
        blogJsonObject.addProperty("imageUrl", blog.getImageUrl());
        blogJsonObject.addProperty("source", blog.getSource());
        blogJsonObject.addProperty("title", blog.getTitle());

        String contentWithOutHtml = blog.getContent().replaceAll("\\<[^>]*>", "");
        blogJsonObject.addProperty("contentSummary", contentWithOutHtml);

        addDateField(blogJsonObject, "publishDate", blog.getPublishDate());
        return blogJsonObject;
    }

    protected JsonArray convertBlogList(Collection<Blog> blogList) {
        JsonArray jsonArray = new JsonArray();
        if (blogList == null) {
            return jsonArray;
        }
        for (Blog oneNews : blogList) {
            JsonObject newsJsonObject = convertBlog(oneNews);
            jsonArray.add(newsJsonObject);
        }
        return jsonArray;
    }

    protected JsonObject convertLocation(Location location) {
        JsonObject locationJsonObject = new JsonObject();
        locationJsonObject.addProperty("id", location.getId());
        if (location.getIsdCode() != null) {
            locationJsonObject.addProperty("isdCode", location.getIsdCode());
        }
        locationJsonObject.addProperty("name", location.getName());
        return locationJsonObject;

    }

    protected JsonArray convertLocationList(Collection<Location> locationList) {
        JsonArray jsonArray = new JsonArray();
        if (locationList == null) {
            return jsonArray;
        }
        for (Location oneNews : locationList) {
            JsonObject newsJsonObject = convertLocation(oneNews);
            jsonArray.add(newsJsonObject);
        }
        return jsonArray;
    }

    protected Long getLongParameterFromPathOrParams(HttpServletRequest httpServletRequest, String paramName) {
        String paramIdStr = getStringParameterFromPathOrParams(httpServletRequest, paramName);
        try {
            return Long.parseLong(paramIdStr);
        } catch (Exception ex) {
            return null;
        }
    }

    protected String getStringParameterFromPathOrParams(HttpServletRequest httpServletRequest, String paramName) {
        Map<String, String> pathParams = (Map<String, String>) httpServletRequest.getAttribute(HttpParameters.PATH_PARAMETER_PARAM);
        
        String paramStr = pathParams.get(paramName);
        if (paramStr == null) {
            paramStr = httpServletRequest.getParameter(paramName);
        }
        return paramStr;
    }

    protected JsonObject convertEvent(Event event) {
        JsonObject eventJsonObject = new JsonObject();
        eventJsonObject.addProperty("id", event.getId());
        eventJsonObject.addProperty("title", event.getTitle());
        eventJsonObject.addProperty("description", event.getDescription());
        eventJsonObject.addProperty("address", event.getAddress());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(event.getStartDate());

        eventJsonObject.addProperty("month", calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.UK));
        eventJsonObject.addProperty("date", calendar.get(Calendar.DATE));
        StringBuilder sb = new StringBuilder();
        sb.append(calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + " " + calendar.getDisplayName(Calendar.AM_PM, Calendar.SHORT, Locale.UK).toLowerCase());
        sb.append(" - ");
        calendar.setTime(event.getEndDate());
        sb.append(calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + " " + calendar.getDisplayName(Calendar.AM_PM, Calendar.SHORT, Locale.UK).toLowerCase());
        addDateField(eventJsonObject, "startDate", event.getStartDate());
        addDateField(eventJsonObject, "endDate", event.getEndDate());
        eventJsonObject.addProperty("time", sb.toString());
        return eventJsonObject;

    }

    protected JsonArray convertEventList(Collection<Event> eventList) {
        JsonArray jsonArray = new JsonArray();
        if (eventList == null) {
            return jsonArray;
        }
        for (Event oneNews : eventList) {
            JsonObject newsJsonObject = convertEvent(oneNews);
            jsonArray.add(newsJsonObject);
        }
        return jsonArray;
    }
}
