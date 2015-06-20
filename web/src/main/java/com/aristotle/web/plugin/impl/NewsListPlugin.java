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
            System.out.println("Getting news for " + locations + ", page number = " + pageNumber + ", pageSize=" + pageSize);
            List<News> newsList = newsService.getAllLocationPublishedNews(locations, pageNumber, pageSize);
            long totalNews = newsService.getAllLocationPublishedNewsCount(locations);
            if (newsList == null || newsList.isEmpty()) {
                newsList = newsService.getAllLocationPublishedNews(null, pageNumber, pageSize);
                totalNews = newsService.getAllLocationPublishedNewsCount(null);
            }
            JsonArray jsonArray = convertNewsList(newsList);
            context.add(name, jsonArray);
            addPaginInformation(pageNumber, pageSize, totalNews, context);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addPaginInformation(int currentPage, int pageSize, long totalRecords, JsonObject context) {
        JsonObject pageJsonObject = new JsonObject();
        int totalPages = (int) totalRecords / pageSize;
        if (totalRecords % pageSize > 0) {
            totalPages++;
        }
        if (currentPage > 1) {
            pageJsonObject.addProperty("previous", true);
            pageJsonObject.addProperty("previousPage", (currentPage - 1));
        }
        if (currentPage < totalPages) {
            pageJsonObject.addProperty("next", true);
            pageJsonObject.addProperty("nextPage", (currentPage + 1));
        }
        pageJsonObject.addProperty("lastPage", totalPages);
        // 1 2 3
        int pageListStart = 1;
        int pageListEnd = totalPages;
        if (currentPage + 2 <= totalPages) {
            if (currentPage > 2) {
                pageListStart = currentPage - 2;
            }
            pageListEnd = pageListStart + 4;
        } else {
            pageListEnd = totalPages;
            pageListStart = pageListEnd - 4;
            if (pageListStart <= 0) {
                pageListStart = 1;
            }
        }
        JsonArray pageListJsonArray = new JsonArray();
        for (int count = pageListStart; count <= pageListEnd; count++) {
            JsonObject onePage = new JsonObject();
            onePage.addProperty("pageNumber", count);
            if (count == currentPage) {
                onePage.addProperty("selected", true);
            }
            pageListJsonArray.add(onePage);
        }
        pageJsonObject.add("pages", pageListJsonArray);
        context.add("pageInfo", pageJsonObject);
    }

}
