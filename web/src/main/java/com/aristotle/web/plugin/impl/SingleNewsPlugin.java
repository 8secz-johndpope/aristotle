package com.aristotle.web.plugin.impl;

import java.util.Map;

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
import com.google.gson.JsonObject;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SingleNewsPlugin extends AbstractDataPlugin {

    @Autowired
    private NewsService newsService;

    public SingleNewsPlugin(String pluginName) {
        super(pluginName);
    }

    public SingleNewsPlugin() {
    }

    @Override
    public void applyPlugin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv) {
        logger.info("Applying {} plugin", name);
        try {
            JsonObject context = (JsonObject) mv.getModel().get("context");
            Long newsId = getNewsId(httpServletRequest);

            News news = newsService.getNewsById(newsId);
            JsonObject newsJsonObject = convertNews(news);

            context.add(name, newsJsonObject);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Long getNewsId(HttpServletRequest httpServletRequest) {
        Map<String, String> pathParams = (Map<String, String>) httpServletRequest.getAttribute(HttpParameters.PATH_PARAMETER_PARAM);
        String newsIdStr = pathParams.get("newsId");
        if (newsIdStr == null) {
            newsIdStr = httpServletRequest.getParameter("newsId");
        }
        try{
            return Long.parseLong(newsIdStr);
        }catch(Exception ex){
            return null;
        }
    }

}
