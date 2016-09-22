package com.aristotle.web.plugin.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.aristotle.core.persistance.Event;
import com.aristotle.core.service.EventService;
import com.aristotle.web.parameters.HttpParameters;
import com.google.gson.JsonObject;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SingleEventPlugin extends AbstractDataPlugin {

    private final String EVENT_ID_PARAMETER = "eventId";
    @Autowired
    private EventService eventService;

    public SingleEventPlugin(String pluginName) {
        super(pluginName);
    }

    public SingleEventPlugin() {
    }

    @Override
    public void applyPlugin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv) {
        logger.info("Applying {} plugin", name);
        try {
            JsonObject context = (JsonObject) mv.getModel().get("context");
            Long newsId = getEventId(httpServletRequest);
            if (newsId == null) {
                context.addProperty(name, "{\"error\":\"No Event Id Specified\"}");
                return;
            }

            Event event = eventService.getEventById(newsId);
            JsonObject eventJsonObject = convertEvent(event);
            logger.info("event = {}" , event);

            context.add(name, eventJsonObject);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Long getEventId(HttpServletRequest httpServletRequest) {
        Map<String, String> pathParams = (Map<String, String>) httpServletRequest.getAttribute(HttpParameters.PATH_PARAMETER_PARAM);
        String newsIdStr = pathParams.get(EVENT_ID_PARAMETER);
        if (newsIdStr == null) {
            newsIdStr = httpServletRequest.getParameter(EVENT_ID_PARAMETER);
        }
        try{
            return Long.parseLong(newsIdStr);
        }catch(Exception ex){
            return null;
        }
    }

}
