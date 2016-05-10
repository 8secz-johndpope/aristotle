package com.aristotle.web.plugin.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.aristotle.core.persistance.Event;
import com.aristotle.core.service.EventService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EventListPlugin extends AbstractDataPlugin {

    @Autowired
    private EventService eventService;
    public EventListPlugin(String pluginName) {
        super(pluginName);
    }

    public EventListPlugin() {
    }

    @Override
    public void applyPlugin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv) {
        int size = getIntSettingPramater("events.size", 3);
        List<Event> events = eventService.getLocationEvents(null, size, true);
        JsonArray eventJsonArray = convertEventList(events);
        JsonObject context = (JsonObject) mv.getModel().get("context");
        context.add(name, eventJsonArray);
    }

}
