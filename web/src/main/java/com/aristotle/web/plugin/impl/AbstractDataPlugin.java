package com.aristotle.web.plugin.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

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
import com.aristotle.core.persistance.User;
import com.aristotle.core.persistance.Video;
import com.aristotle.web.parameters.HttpParameters;
import com.aristotle.web.plugin.WebDataPlugin;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public abstract class AbstractDataPlugin implements WebDataPlugin {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    private DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
    protected SimpleDateFormat ddMMMyyyyFormat = new SimpleDateFormat("dd-MMM-yyyy");
    protected SimpleDateFormat ddMMyyyyFormat = new SimpleDateFormat("dd-MM-yyyy");
    private Gson gson = new Gson();

    Map<String, String> settingMap = new LinkedHashMap<String, String>();
    protected final String name;

    public AbstractDataPlugin() {
        this("NoName");
    }

    public AbstractDataPlugin(String pluginName) {
        this.name = pluginName;
    }

    protected Pageable getPageRequest(HttpServletRequest httpServletRequest) {
        int page = getIntPramater(httpServletRequest, HttpParameters.PAGE_NUMBER_PARAM, HttpParameters.PAGE_NUMBER_DEFAULT_VALUE);
        int size = getIntSettingPramater("news.size", 2);// getIntPramater(httpServletRequest, HttpParameters.PAGE_SIZE_PARAM, HttpParameters.PAGE_SIZE_DEFAULT_VALUE);
        Pageable pageable = new PageRequest(page, size);
        return pageable;
    }

    @Override
    public void setSettings(String settings) {
        JsonParser jsonParser = new JsonParser();
        try {
            JsonObject settingJsonObject = (JsonObject) jsonParser.parse(settings);
            addSettingToMap(settingJsonObject, null, settingMap);

        } catch (Exception ex) {
            logger.error("Invalid Setting for Plugin {}", name);
        }

    }

    private void addSettingToMap(JsonObject jsonObject, String prefix, Map<String, String> settingMap) {
        for(Entry<String, JsonElement> oneEntry : jsonObject.entrySet()){
            if (oneEntry.getValue().isJsonPrimitive()) {
                String propertyName;
                if (prefix == null) {
                    propertyName = oneEntry.getKey();
                } else {
                    propertyName = prefix + "." + oneEntry.getKey();
                }
                settingMap.put(propertyName, oneEntry.getValue().getAsString());
            } else {
                if (prefix == null) {
                    addSettingToMap(oneEntry.getValue().getAsJsonObject(), oneEntry.getKey(), settingMap);
                } else {
                    addSettingToMap(oneEntry.getValue().getAsJsonObject(), prefix + "." + oneEntry.getKey(), settingMap);
                }

            }
        }
    }

    protected int getIntSettingPramater(String paramName, int defaultValue) {
        try {
            return Integer.parseInt(settingMap.get(paramName));
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    protected Long getLongSettingPramater(String paramName, Long defaultValue) {
        try {
            return Long.parseLong(settingMap.get(paramName));
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    protected String getStringSettingPramater(String paramName, String defaultValue) {
        String paramValue = settingMap.get(paramName);
        if (paramValue == null) {
            paramValue = defaultValue;
        }
        return paramValue;
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
        if (news.getPublishDate() == null) {
            newsJsonObject.addProperty("publishDate_ddMMMyyyy", "");
        } else {
            newsJsonObject.addProperty("publishDate_ddMMMyyyy", ddMMMyyyyFormat.format(news.getPublishDate()));
        }

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
        if (blog.getPublishDate() == null) {
            blogJsonObject.addProperty("publishDate_ddMMMyyyy", "");
        } else {
            blogJsonObject.addProperty("publishDate_ddMMMyyyy", ddMMMyyyyFormat.format(blog.getPublishDate()));
        }
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
        eventJsonObject.addProperty("lattitude", event.getLattitude());
        eventJsonObject.addProperty("longitude", event.getLongitude());
        eventJsonObject.addProperty("address", event.getAddress());
        eventJsonObject.addProperty("contactNumber1", event.getContactNumber1());
        eventJsonObject.addProperty("contactNumber2", event.getContactNumber2());
        eventJsonObject.addProperty("contactNumber3", event.getContactNumber3());
        eventJsonObject.addProperty("contactNumber4", event.getContactNumber4());
        eventJsonObject.addProperty("contactEmail", event.getContactEmail());
        eventJsonObject.addProperty("fbEventId", event.getFbEventId());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(event.getStartDate());

        eventJsonObject.addProperty("month", calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.UK).toUpperCase());
        eventJsonObject.addProperty("date", calendar.get(Calendar.DATE));
        eventJsonObject.addProperty("year", calendar.get(Calendar.YEAR));
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

    protected JsonObject convertVideo(Video event) {
        JsonObject eventJsonObject = new JsonObject();
        eventJsonObject.addProperty("id", event.getId());
        eventJsonObject.addProperty("title", event.getTitle());
        eventJsonObject.addProperty("description", event.getDescription());
        eventJsonObject.addProperty("channelId", event.getChannelId());
        eventJsonObject.addProperty("webUrl", event.getWebUrl());
        eventJsonObject.addProperty("youtubeVideoId", event.getYoutubeVideoId());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(event.getPublishDate());

        eventJsonObject.addProperty("month", calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.UK).toUpperCase());
        eventJsonObject.addProperty("date", calendar.get(Calendar.DATE));
        eventJsonObject.addProperty("year", calendar.get(Calendar.YEAR));

        return eventJsonObject;

    }

    protected JsonArray convertVideoList(Collection<Video> videoList) {
        JsonArray jsonArray = new JsonArray();
        if (videoList == null) {
            return jsonArray;
        }
        for (Video oneVideo : videoList) {
            JsonObject newsJsonObject = convertVideo(oneVideo);
            jsonArray.add(newsJsonObject);
        }
        return jsonArray;
    }

    protected JsonObject convertUser(User user) {
        JsonObject userJsonObject = new JsonObject();
        userJsonObject.addProperty("id", user.getId());
        userJsonObject.addProperty("name", user.getName());
        userJsonObject.addProperty("profilePic", user.getProfilePic());
        return userJsonObject;
    }

}
