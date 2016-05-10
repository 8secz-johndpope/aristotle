package com.aristotle.web.plugin.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.aristotle.core.persistance.Blog;
import com.aristotle.core.service.BlogService;
import com.aristotle.web.parameters.HttpParameters;
import com.google.gson.JsonObject;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SingleBlogPlugin extends AbstractDataPlugin {

    @Autowired
    private BlogService blogService;

    public SingleBlogPlugin(String pluginName) {
        super(pluginName);
    }

    public SingleBlogPlugin() {
    }

    @Override
    public void applyPlugin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv) {
        logger.info("Applying {} plugin", name);
        try {
            JsonObject context = (JsonObject) mv.getModel().get("context");
            Long newsId = getBlogId(httpServletRequest);

            Blog blog = blogService.getBlogById(newsId);
            JsonObject newsJsonObject = convertBlog(blog);

            context.add(name, newsJsonObject);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Long getBlogId(HttpServletRequest httpServletRequest) {
        Map<String, String> pathParams = (Map<String, String>) httpServletRequest.getAttribute(HttpParameters.PATH_PARAMETER_PARAM);
        String newsIdStr = pathParams.get("blogId");
        if (newsIdStr == null) {
            newsIdStr = httpServletRequest.getParameter("blogId");
        }
        try{
            return Long.parseLong(newsIdStr);
        }catch(Exception ex){
            return null;
        }
    }

}
