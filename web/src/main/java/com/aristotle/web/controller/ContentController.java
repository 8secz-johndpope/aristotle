package com.aristotle.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

@Controller
public class ContentController {

    @Autowired
    private PluginManager pluginManager;

    @Autowired
    private UiTemplateManager uiTemplateManager;

    @Autowired
    private HandleBarManager handleBarManager;
    
    private Logger logger = LoggerFactory.getLogger(this.getClass());

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
        StopWatch stopWatch = new StopWatch();
        
        try {
        	stopWatch.start("DBTask");
            pluginManager.applyAllPluginsForUrl(httpServletRequest, httpServletResponse, modelAndView, true, true);
            addPageAttributes(httpServletRequest, httpServletResponse, modelAndView);
            stopWatch.stop();
        } catch (NotLoggedInException e) {
            return "User not logged In";
        }
        stopWatch.start("Get Template");
        logger.info("Get URL Template : {}", httpServletRequest.getRequestURI());
        Template template = uiTemplateManager.getCompiledTemplate(httpServletRequest, httpServletResponse);
        //modelAndView.getModel().put("template", stringTemplate);
        stopWatch.stop();

        //Handlebars handlebars = handleBarManager.getHandlebars();
        
        stopWatch.start("Compile Template");
        //Template template = handlebars.compileInline(stringTemplate);
        stopWatch.stop();

        stopWatch.start("Convert Data To Jackson");
        JsonNode rootNode = convertDataToJackSon(jsonContext);
        Context context = Context.newBuilder(rootNode).resolver(JsonNodeValueResolver.INSTANCE).build();
        stopWatch.stop();
        
        stopWatch.start("Apply Data");
        String result = template.apply(context);
        stopWatch.stop();
        Integer cacheTimeInSeconds = uiTemplateManager.getCacheTime(httpServletRequest);
        if (cacheTimeInSeconds == null && httpServletRequest.getRequestURI().contains("content")) {
            cacheTimeInSeconds = 300;
        }
        if (cacheTimeInSeconds != null) {
            httpServletResponse.setHeader("Cache-Control", "max-age=" + cacheTimeInSeconds);
        }
        logger.info(stopWatch.prettyPrint());

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
            addPageAttributes(httpServletRequest, httpServletResponse, modelAndView);
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
        if (requestedUrl.startsWith("/content/home") || requestedUrl.startsWith("/index.html") || requestedUrl.equals("/")) {
            addIndexPageTitleAndDescription(pageObject, jsonContext);
            return;
        }
        if (requestedUrl.startsWith("/content/video/")) {
            addVideoItemTitleDescription(pageObject, jsonContext);
            return;
        }
        if (requestedUrl.startsWith("/content/videos")) {
            addVideoListDescription(pageObject, jsonContext);
            return;
        }
        if (requestedUrl.startsWith("/organisation/nwc") || requestedUrl.startsWith("/organisation/team/national-working-committee")) {
            addNationalWorkingCommiteeTitleDescription(pageObject, jsonContext);
            return;
        }
        if (requestedUrl.startsWith("/organisation/nsc") || requestedUrl.startsWith("/organisation/team/national-steering-commitee")) {
            addNationalSteeringCommiteeTitleDescription(pageObject, jsonContext);
            return;
        }
        if (requestedUrl.startsWith("/organisation/vision")) {
            addVisionTitleDescription(pageObject, jsonContext);
            return;
        }
        if (requestedUrl.startsWith("/organisation/constitution")) {
            addConstitutionTitleDescription(pageObject, jsonContext);
            return;
        }
        if (requestedUrl.startsWith("/organisation/minutes_of_meetings")) {
            addMinutesOfMeetingTitleDescription(pageObject, jsonContext);
            return;
        }
        if (requestedUrl.startsWith("/content/hangouts")) {
        	addHangoutTitleDescription(pageObject, jsonContext);
            return;
        }
        if (requestedUrl.startsWith("/content/blogs")) {
        	addBlogListTitleDescription(pageObject, jsonContext);
            return;
        }
        if (requestedUrl.startsWith("/organisation/accounts")) {
        	addAccountsTitleDescription(pageObject, jsonContext);
            return;
        }

    }
    private void addImage(JsonObject webPageObject, String image){
    	try{
        	JsonArray images = (JsonArray)webPageObject.get("images");
        	if(images == null || images.isJsonNull()){
        		images = new JsonArray();
        		webPageObject.add("images", images);
        	}
        	JsonObject oneImage = new JsonObject();
            oneImage.addProperty("image", image);
            images.add(oneImage);
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    	
    }
    private void addAccountsTitleDescription(JsonObject pageObject, JsonObject jsonContext) {
           pageObject.addProperty("title", "Swaraj Abhiyan Accounts - Donations and Expenses");
            pageObject.addProperty("description", "Swaraj Abhiyan belives in true transparency and thats why we welcomes you to check our accounts. PLease visit this page regularly.");
            addImage(pageObject, "//static.swarajabhiyan.org/templates/prod/1/images/accounts_01.jpg");
        
    }
    private void addBlogListTitleDescription(JsonObject pageObject, JsonObject jsonContext) {
        try {
            pageObject.addProperty("title", "Swaraj Abhiyan Blogs");
            String firstNewsDescription = jsonContext.get("BlogListPlugin").getAsJsonArray().get(0).getAsJsonObject().get("contentSummary").getAsString();
            pageObject.addProperty("description", firstNewsDescription);
            addImage(pageObject, "//static.swarajabhiyan.org/templates/prod/1/images/blog_01.jpg");
        } catch (Exception ex) {
        	pageObject.addProperty("description", "Swaraj Abhiyan Blogs");
        	pageObject.addProperty("title", "Swaraj Abhiyan Blogs");
        	addImage(pageObject, "//static.swarajabhiyan.org/templates/prod/1/images/blog_01.jpg");
        }
    }

    private void addHangoutTitleDescription(JsonObject pageObject, JsonObject jsonContext) {
        try {
            pageObject.addProperty("description", "Ask your questions by filling the below form");
            pageObject.addProperty("title", "Swaraj Abhiyan Google Hangout");
            addImage(pageObject, "//static.swarajabhiyan.org/templates/prod/1/images/google_hangout_01.jpg");
        } catch (Exception ex) {
        	pageObject.addProperty("description", "Ask your questions by filling the below form");
            pageObject.addProperty("title", "Swaraj Abhiyan Google Hangout");
            addImage(pageObject, "//static.swarajabhiyan.org/templates/prod/1/images/google_hangout_01.jpg");
        }
    }
    private void addMinutesOfMeetingTitleDescription(JsonObject pageObject, JsonObject jsonContext) {
        try {
            pageObject.addProperty("description", "We do things differently, we publish all minutes of each meeting| Swaraj Abhiyan Minutes Of Meeting");
            pageObject.addProperty("title", "Swaraj Abhiyan Minutes Of meetings");
            addImage(pageObject, "//static.swarajabhiyan.org/templates/prod/1/images/minutes_01.jpg");
        } catch (Exception ex) {
            pageObject.addProperty("description", "We do things differently, we publish all minutes of each meeting| Swaraj Abhiyan Minutes Of Meeting");
            pageObject.addProperty("title", "Swaraj Abhiyan Minutes Of meetings");
            addImage(pageObject, "//static.swarajabhiyan.org/templates/prod/1/images/minutes_01.jpg");
        }
    }
    private void addNationalWorkingCommiteeTitleDescription(JsonObject pageObject, JsonObject jsonContext) {
        try {
            pageObject.addProperty("description", "List of National Working Committee(NWC) Members | Swaraj Abhiyan Video");
            pageObject.addProperty("title", "List of National Working Committee(NWC) Members | Swaraj Abhiyan Video");
            addImage(pageObject, "//static.swarajabhiyan.org/templates/prod/1/images/team_01.jpg");
        } catch (Exception ex) {
            pageObject.addProperty("description", "List of National Working Committee(NSC) Members | Swaraj Abhiyan Video");
            pageObject.addProperty("title", "List of National Working Committee(NSC) Members | Swaraj Abhiyan Video");
            addImage(pageObject, "//static.swarajabhiyan.org/templates/prod/1/images/team_01.jpg");
        }
    }

    private void addVisionTitleDescription(JsonObject pageObject, JsonObject jsonContext) {
        try {
            pageObject.addProperty("description", "Vision of Swaraj Abhiyan");
            pageObject.addProperty("title", "Vision of Swaraj Abhiyan");
        } catch (Exception ex) {
            pageObject.addProperty("description", "Vision of Swaraj Abhiyan");
            pageObject.addProperty("title", "Vision of Swaraj Abhiyan");
        }
    }

    private void addConstitutionTitleDescription(JsonObject pageObject, JsonObject jsonContext) {
        try {
            pageObject.addProperty("description", "Constitution of Swaraj Abhiyan");
            pageObject.addProperty("title", "Constitution of Swaraj Abhiyan");
        } catch (Exception ex) {
            pageObject.addProperty("description", "Constitution of Swaraj Abhiyan");
            pageObject.addProperty("title", "Constitution of Swaraj Abhiyan");
        }
    }
    private void addNationalSteeringCommiteeTitleDescription(JsonObject pageObject, JsonObject jsonContext) {
        try {
            pageObject.addProperty("description", "List of National Steering Committee(NSC) Members | Swaraj Abhiyan Video");
            pageObject.addProperty("title", "List of National Steering Committee(NSC) Members | Swaraj Abhiyan Video");
            addImage(pageObject, "//static.swarajabhiyan.org/templates/prod/1/images/team_01.jpg");
        } catch (Exception ex) {
            pageObject.addProperty("description", "List of National Steering Committee(NSC) Members | Swaraj Abhiyan Video");
            pageObject.addProperty("title", "List of National Steering Committee(NSC) Members | Swaraj Abhiyan Video");
            addImage(pageObject, "//static.swarajabhiyan.org/templates/prod/1/images/team_01.jpg");
        }
    }
    private void addVideoItemTitleDescription(JsonObject pageObject, JsonObject jsonContext) {
        try {
            String videoDescription = jsonContext.get("SingleVideoPlugin").getAsJsonObject().get("description").getAsString();
            String title = jsonContext.get("SingleVideoPlugin").getAsJsonObject().get("title").getAsString();
            String youtubeVideoId = jsonContext.get("SingleVideoPlugin").getAsJsonObject().get("youtubeVideoId").getAsString();
            JsonArray images = new JsonArray();
            JsonObject oneImage = new JsonObject();
            oneImage.addProperty("image", "//i.ytimg.com/vi/" + youtubeVideoId + "/maxresdefault.jpg");
            images.add(oneImage);
            pageObject.addProperty("description", videoDescription);
            pageObject.addProperty("title", title + " | Swaraj Abhiyan Video");
            pageObject.add("images", images);
        } catch (Exception ex) {
            pageObject.addProperty("description", "Swaraj Abhiyan News");
            pageObject.addProperty("title", "Swaraj Abhiyan News");
        }
    }

    private void addVideoListDescription(JsonObject pageObject, JsonObject jsonContext) {
        try {
            pageObject.addProperty("title", "Watch Latest videos from Swaraj Abhiyan");
            String firstNewsDescription = jsonContext.get("VideoListPlugin").getAsJsonArray().get(0).getAsJsonObject().get("description").getAsString();
            pageObject.addProperty("description", firstNewsDescription);
            JsonArray videos = jsonContext.get("VideoListPlugin").getAsJsonArray();
            JsonArray images = new JsonArray();
            JsonObject oneVideo;
            for (int i = 0; i < videos.size(); i++) {
                oneVideo = videos.get(i).getAsJsonObject();
                JsonObject oneImage = new JsonObject();
                oneImage.addProperty("image", "//i.ytimg.com/vi/" + oneVideo.get("youtubeVideoId").getAsString() + "/maxresdefault.jpg");
                images.add(oneImage);
            }
            pageObject.add("images", images);
        } catch (Exception ex) {
            pageObject.addProperty("description", "All Latest Swaraj Abhiyan News");
        }
    }
    private void addNewsItemTitleDescription(JsonObject pageObject, JsonObject jsonContext) {
        try {
            String newsDescription = jsonContext.get("SingleNewsPlugin").getAsJsonObject().get("contentSummary").getAsString();
            String title = jsonContext.get("SingleNewsPlugin").getAsJsonObject().get("title").getAsString();
            pageObject.addProperty("description", newsDescription);
            pageObject.addProperty("title", title + " | Swaraj Abhiyan News");
            addImage(pageObject, "//static.swarajabhiyan.org/templates/prod/1/images/news_01.jpg");
        } catch (Exception ex) {
            pageObject.addProperty("description", "Swaraj Abhiyan News");
            pageObject.addProperty("title", "Swaraj Abhiyan News");
            addImage(pageObject, "//static.swarajabhiyan.org/templates/prod/1/images/news_01.jpg");
        }
    }
    private void addNewsListDescription(JsonObject pageObject, JsonObject jsonContext){
        try{
            pageObject.addProperty("title", "Swaraj Abhiyan Latest News");
            String firstNewsDescription = jsonContext.get("NewsListPlugin").getAsJsonArray().get(0).getAsJsonObject().get("contentSummary").getAsString();
            pageObject.addProperty("description", firstNewsDescription);
            addImage(pageObject, "//static.swarajabhiyan.org/templates/prod/1/images/news_01.jpg");
        } catch (Exception ex) {
            pageObject.addProperty("description", "All Latest Swaraj Abhiyan News");
            addImage(pageObject, "//static.swarajabhiyan.org/templates/prod/1/images/news_01.jpg");
        }
    }
    
    private void addIndexPageTitleAndDescription(JsonObject pageObject, JsonObject jsonContext){
        try{
            pageObject.addProperty("title", "Swaraj Abhiyan Official Website");
            String firstNewsDescription = jsonContext.get("NewsListPlugin").getAsJsonArray().get(0).getAsJsonObject().get("contentSummary").getAsString();
            pageObject.addProperty("description", firstNewsDescription);
            addImage(pageObject, "//static.swarajabhiyan.org/templates/prod/1/images/general_01.jpg");
        } catch (Exception ex) {
            pageObject.addProperty("description", "All Latest Swaraj Abhiyan News");
            addImage(pageObject, "//static.swarajabhiyan.org/templates/prod/1/images/general_01.jpg");
        }
    }
}
