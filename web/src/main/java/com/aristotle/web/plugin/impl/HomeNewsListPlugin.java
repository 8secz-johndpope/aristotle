package com.aristotle.web.plugin.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.aristotle.core.persistance.News;
import com.aristotle.core.service.NewsService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HomeNewsListPlugin extends AbstractDataPlugin {

    @Autowired
    private NewsService newsService;



    public HomeNewsListPlugin() {
    }

    public HomeNewsListPlugin(String pluginName) {
        super(pluginName);
    }

    @Override
    public void applyPlugin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv) {
        logger.info("Applying {} plugin", name);
        try {
            JsonObject context = (JsonObject) mv.getModel().get("context");
            int totalNews = getIntSettingPramater("home.total_news", 6);
            int totalNewsPerFrame = getIntSettingPramater("home.news_per_frame", 2);
            List<News> newsList = newsService.getAllPublishedNews(totalNews);
            JsonArray jsonArray = new JsonArray();
            JsonArray oneFrameJsonArray = new JsonArray();
            int count = 0;
            for (News oneNews : newsList) {
                oneFrameJsonArray.add(convertNews(oneNews));
                count++;
                if (count % totalNewsPerFrame == 0) {
                    jsonArray.add(oneFrameJsonArray);
                    oneFrameJsonArray = new JsonArray();
                }
            }

            context.add(name, jsonArray);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
