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

import com.aristotle.core.persistance.Video;
import com.aristotle.core.service.VideoService;
import com.aristotle.web.parameters.HttpParameters;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VideoListPlugin extends LocationAwareDataPlugin {

    @Autowired
    private VideoService videoService;
    public VideoListPlugin(String pluginName) {
        super(pluginName);
    }

    public VideoListPlugin() {
    }

    @Override
    public void applyPluginForLocation(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv, Set<Long> locations) {
        try {
            JsonObject context = (JsonObject) mv.getModel().get("context");
            int pageNumber = getIntPramater(httpServletRequest, HttpParameters.PAGE_NUMBER_PARAM, HttpParameters.PAGE_NUMBER_DEFAULT_VALUE);
            int pageSize = getIntSettingPramater("videos.size", 12);
            System.out.println("Getting news for " + locations + ", page number = " + pageNumber + ", pageSize=" + pageSize);
            List<Video> videoList = videoService.getLocationVideos(locations, (pageNumber - 1), pageSize);
            long totalNews = videoService.getLocationVideosCount(locations);
            if (videoList == null || videoList.isEmpty()) {
                videoList = videoService.getLocationVideos(locations, (pageNumber - 1), pageSize);
                totalNews = videoService.getLocationVideosCount(locations);
            }
            JsonArray videoJsonArray = convertVideoList(videoList);
            context.add(name, videoJsonArray);
            addPaginInformation(pageNumber, pageSize, totalNews, context);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
