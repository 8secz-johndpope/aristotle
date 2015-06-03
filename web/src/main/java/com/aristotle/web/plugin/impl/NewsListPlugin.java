package com.aristotle.web.plugin.impl;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.aristotle.core.persistance.News;
import com.aristotle.core.service.NewsService;
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
            Pageable pageRequest = getPageRequest(httpServletRequest);
            int totalNews = getIntSettingPramater("news.size", 2);
            List<News> newsList = newsService.getAllPublishedNews(totalNews);
            JsonArray jsonArray = convertNewsList(newsList);
            context.add(name, jsonArray);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
