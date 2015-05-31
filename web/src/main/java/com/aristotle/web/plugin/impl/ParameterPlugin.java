package com.aristotle.web.plugin.impl;

import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ParameterPlugin extends AbstractDataPlugin {

    private JsonParser jsonParser = new JsonParser();

    public ParameterPlugin(String pluginName) {
        super(pluginName);
    }

    public ParameterPlugin() {
    }

    @Override
    public void applyPlugin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv) {

        logger.info("Applying {} plugin", name);
        try {
            JsonObject context = (JsonObject) mv.getModel().get("context");
            JsonObject parameterJsonObject = new JsonObject();
            for (Entry<String, String[]> oneEntry : httpServletRequest.getParameterMap().entrySet()) {
                if (oneEntry.getValue() == null || oneEntry.getValue().length == 0) {
                    parameterJsonObject.addProperty(oneEntry.getKey(), "");
                } else if (oneEntry.getValue().length == 1) {
                    parameterJsonObject.addProperty(oneEntry.getKey(), oneEntry.getValue()[0]);
                } else {
                    JsonElement valueJsonElement = jsonParser.parse("[" + StringUtils.arrayToDelimitedString(oneEntry.getValue(), ",") + "]");
                    parameterJsonObject.add(oneEntry.getKey(), valueJsonElement);
                }
            }
            context.add(name, parameterJsonObject);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
