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

import com.aristotle.core.persistance.Blog;
import com.aristotle.core.service.BlogService;
import com.aristotle.web.parameters.HttpParameters;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BlogListPlugin extends LocationAwareDataPlugin {

    @Autowired
    private BlogService blogService;

    public BlogListPlugin() {
    }

    public BlogListPlugin(String pluginName) {
        super(pluginName);
    }

    @Override
    public void applyPluginForLocation(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv, Set<Long> locations) {
        try {
            JsonObject context = (JsonObject) mv.getModel().get("context");
            int pageNumber = getIntPramater(httpServletRequest, HttpParameters.PAGE_NUMBER_PARAM, HttpParameters.PAGE_NUMBER_DEFAULT_VALUE);
            int pageSize = getIntSettingPramater("blog.size", 6);
            System.out.println("Getting blog for " + locations + ", page number = " + pageNumber + ", pageSize=" + pageSize);
            List<Blog> blogList = blogService.getAllLocationPublishedBlog(locations, pageNumber, pageSize);
            JsonArray jsonArray = convertBlogList(blogList);
            context.add(name, jsonArray);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
