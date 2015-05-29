package com.aristotle.web.controller;

import java.io.IOException;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.aristotle.web.plugin.PluginManager;
import com.aristotle.web.ui.template.UiTemplateManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.JsonNodeValueResolver;
import com.github.jknack.handlebars.Template;
import com.google.gson.JsonObject;

@Controller
public class ContentController {

    @Autowired
    private PluginManager pluginManager;

    @Autowired
    private UiTemplateManager uiTemplateManager;

    @Autowired
    private HandleBarManager handleBarManager;

    @ExceptionHandler({ Exception.class })
    public String handleException(Exception ex) {
        ex.printStackTrace();
        return ex.getMessage();
    }

    // @RequestMapping("/content/**")
    public ModelAndView defaultMethod(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) {

        JsonObject context = new JsonObject();
        modelAndView.getModel().put("context", context);
        pluginManager.applyAllPluginsForUrl(httpServletRequest, httpServletResponse, modelAndView, false);

        String template = uiTemplateManager.getTemplate(httpServletRequest);
        modelAndView.getModel().put("template", template);

        modelAndView.setViewName("handlebar");
        return modelAndView;
    }

    @RequestMapping(value = { "/content/**", "/", "/index.html", "/**" })
    @ResponseBody
    public String serverSideHandler(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) throws IOException {

        
        JsonObject jsonContext = new JsonObject();
        modelAndView.getModel().put("context", jsonContext);
        pluginManager.applyAllPluginsForUrl(httpServletRequest, httpServletResponse, modelAndView, true);

        String stringTemplate = uiTemplateManager.getTemplate(httpServletRequest);
        modelAndView.getModel().put("template", stringTemplate);

        Handlebars handlebars = handleBarManager.getHandlebars();

        System.out.println("jsonContext.toString() = " + jsonContext.toString());
        Template template = handlebars.compileInline(stringTemplate);

        JsonNode rootNode = convertDataToJackSon(jsonContext);
        Context context = Context.newBuilder(rootNode).resolver(JsonNodeValueResolver.INSTANCE).build();
        for (Entry<String, Object> oneEntry : context.propertySet()) {
            System.out.println("oneEntry = " + oneEntry.getKey());
        }

        String result = template.apply(context);
        return result;
    }

    private ObjectMapper mapper = new ObjectMapper();

    private JsonNode convertDataToJackSon(JsonObject jsonObject) throws JsonProcessingException, IOException {
        JsonNode rootNode = mapper.readTree(jsonObject.toString());
        return rootNode;
    }

    @ResponseBody
    @RequestMapping("/api/content/**")
    public String defaultContentApiMethod(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) {
        System.out.println("content defaultMethod called");
        JsonObject context = new JsonObject();
        modelAndView.getModel().put("context", context);
        pluginManager.applyAllPluginsForUrl(httpServletRequest, httpServletResponse, modelAndView, true);
        return context.toString();
    }

    @ResponseBody
    @RequestMapping("/api/**")
    public String defaultApiMethod(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) {
        System.out.println("defaultMethod called");
        JsonObject context = new JsonObject();
        modelAndView.getModel().put("context", context);
        pluginManager.applyAllPluginsForUrl(httpServletRequest, httpServletResponse, modelAndView, true);
        return context.toString();
    }
}
