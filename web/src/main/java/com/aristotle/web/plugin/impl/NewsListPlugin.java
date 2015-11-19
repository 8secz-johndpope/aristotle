package com.aristotle.web.plugin.impl;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.aristotle.core.persistance.News;
import com.aristotle.core.service.NewsService;
import com.aristotle.web.parameters.HttpParameters;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NewsListPlugin extends LocationAwareDataPlugin {

    @Autowired
    private NewsService newsService;



    public NewsListPlugin() {
    }

    public NewsListPlugin(String pluginName) {
        super(pluginName);
    }

    @Override
    public void applyPluginForLocation(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv, Set<Long> locations) {
        try {
            JsonObject context = (JsonObject) mv.getModel().get("context");
            int pageNumber = getIntPramater(httpServletRequest, HttpParameters.PAGE_NUMBER_PARAM, HttpParameters.PAGE_NUMBER_DEFAULT_VALUE);
            int pageSize = getIntSettingPramater("news.size", 6);
            List<News> newsList = newsService.getAllLocationPublishedNews(locations, (pageNumber - 1), pageSize);
            long totalNews = newsService.getAllLocationPublishedNewsCount(locations);
            if (newsList == null || newsList.isEmpty()) {
                newsList = newsService.getAllLocationPublishedNews(null, (pageNumber - 1), pageSize);
                totalNews = newsService.getAllLocationPublishedNewsCount(null);
            }
            JsonArray jsonArray = convertNewsList(newsList);
            context.add(name, jsonArray);
            addPaginInformation(pageNumber, pageSize, totalNews, context);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



}
