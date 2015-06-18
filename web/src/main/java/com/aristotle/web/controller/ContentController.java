package com.aristotle.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.aristotle.web.exception.NotLoggedInException;
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

    @RequestMapping(value = { "/content/**", "/", "/index.html", "/**" }, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String serverSideHandler(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) throws IOException {

        
        JsonObject jsonContext = new JsonObject();
        modelAndView.getModel().put("context", jsonContext);
        try {
            pluginManager.applyAllPluginsForUrl(httpServletRequest, httpServletResponse, modelAndView, true, true);
            addPageAttributes(httpServletRequest, httpServletResponse, modelAndView);
        } catch (NotLoggedInException e) {
            return "User not logged In";
        }

        String stringTemplate = uiTemplateManager.getTemplate(httpServletRequest);
        modelAndView.getModel().put("template", stringTemplate);

        Handlebars handlebars = handleBarManager.getHandlebars();

        Template template = handlebars.compileInline(stringTemplate);

        JsonNode rootNode = convertDataToJackSon(jsonContext);
        Context context = Context.newBuilder(rootNode).resolver(JsonNodeValueResolver.INSTANCE).build();

        String result = template.apply(context);
        if (httpServletRequest.getRequestURI().contains("content")) {
            httpServletResponse.setHeader("Cache-Control", "max-age=300");
        }
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
        JsonObject context = new JsonObject();
        modelAndView.getModel().put("context", context);
        try {
            pluginManager.applyAllPluginsForUrl(httpServletRequest, httpServletResponse, modelAndView, true, true);
        } catch (NotLoggedInException e) {
            e.printStackTrace();
        }
        return context.toString();
    }

    @ResponseBody
    @RequestMapping("/api/**")
    public String defaultApiMethod(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) {
        JsonObject context = new JsonObject();
        modelAndView.getModel().put("context", context);
        try {
            pluginManager.applyAllPluginsForUrl(httpServletRequest, httpServletResponse, modelAndView, true, false);
        } catch (NotLoggedInException e) {
            e.printStackTrace();
        }
        addCorsHeaders(httpServletResponse);
        return context.toString();
    }

    private void addCorsHeaders(HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET");
        httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "x-requested-with");

    }

    private void addPageAttributes(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) {
        JsonObject jsonContext = (JsonObject) modelAndView.getModel().get("context");
        JsonObject pageObject = new JsonObject();
        jsonContext.add("WebPage", pageObject);
        String requestedUrl = httpServletRequest.getRequestURI();
        if (requestedUrl.startsWith("/content/news/")) {
            addNewsItemTitleDescription(pageObject, jsonContext);
            return;
        }
        if (requestedUrl.startsWith("/content/news")) {
            addNewsListDescription(pageObject, jsonContext);
            return;
        }
    }

    private void addNewsItemTitleDescription(JsonObject pageObject, JsonObject jsonContext) {
        try {
            String newsDescription = jsonContext.get("SingleNewsPlugin").getAsJsonObject().get("contentSummary").getAsString();
            String title = jsonContext.get("SingleNewsPlugin").getAsJsonObject().get("title").getAsString();
            pageObject.addProperty("description", newsDescription);
            pageObject.addProperty("title", title + " | Swaraj Abhiyan News");
        } catch (Exception ex) {
            pageObject.addProperty("description", "Swaraj Abhiyan News");
            pageObject.addProperty("title", "Swaraj Abhiyan News");
        }
    }
    private void addNewsListDescription(JsonObject pageObject, JsonObject jsonContext){
        try{
            pageObject.addProperty("title", "Swaraj Abhiyan Latest News");
            String firstNewsDescription = jsonContext.get("NewsListPlugin").getAsJsonArray().get(0).getAsJsonObject().get("contentSummary").getAsString();
            pageObject.addProperty("description", firstNewsDescription);
        } catch (Exception ex) {
            pageObject.addProperty("description", "All Latest Swaraj Abhiyan News");
        }
    }
}
