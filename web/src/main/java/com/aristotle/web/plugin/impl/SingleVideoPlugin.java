package com.aristotle.web.plugin.impl;

import java.util.Map;

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
import com.google.gson.JsonObject;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SingleVideoPlugin extends AbstractDataPlugin {

    @Autowired
    private VideoService videoService;

    public SingleVideoPlugin(String pluginName) {
        super(pluginName);
    }

    public SingleVideoPlugin() {
    }

    @Override
    public void applyPlugin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv) {
        logger.info("Applying {} plugin", name);
        try {
            JsonObject context = (JsonObject) mv.getModel().get("context");
            Long videoId = getVideoId(httpServletRequest);

            Video video = videoService.getVideoById(videoId);
            JsonObject newsJsonObject = convertVideo(video);

            context.add(name, newsJsonObject);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Long getVideoId(HttpServletRequest httpServletRequest) {
        Map<String, String> pathParams = (Map<String, String>) httpServletRequest.getAttribute(HttpParameters.PATH_PARAMETER_PARAM);
        String newsIdStr = pathParams.get(HttpParameters.VIDEO_ID_PARAM);
        if (newsIdStr == null) {
            newsIdStr = httpServletRequest.getParameter(HttpParameters.VIDEO_ID_PARAM);
        }
        try{
            return Long.parseLong(newsIdStr);
        }catch(Exception ex){
            return null;
        }
    }

}
