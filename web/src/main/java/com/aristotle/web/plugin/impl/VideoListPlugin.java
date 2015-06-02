package com.aristotle.web.plugin.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.aristotle.core.persistance.Video;
import com.aristotle.core.service.VideoService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VideoListPlugin extends AbstractDataPlugin {

    @Autowired
    private VideoService videoService;
    public VideoListPlugin(String pluginName) {
        super(pluginName);
    }

    public VideoListPlugin() {
    }

    @Override
    public void applyPlugin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv) {
        int size = getIntSettingPramater("videos.size", 10);
        List<Video> videos = videoService.getLocationVideos(null, size);
        JsonArray videoJsonArray = convertVideoList(videos);
        JsonObject context = (JsonObject) mv.getModel().get("context");
        context.add(name, videoJsonArray);
    }

}
