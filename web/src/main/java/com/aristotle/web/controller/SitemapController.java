package com.aristotle.web.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.News;
import com.aristotle.core.service.NewsService;

@Controller
public class SitemapController {

    @Autowired
    private HandleBarManager handleBarManager;
    @Autowired
    private NewsService newsService;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @ExceptionHandler({ Exception.class })
    public String handleException(Exception ex) {
        ex.printStackTrace();
        return ex.getMessage();
    }

    @RequestMapping(value = { "/sitemap/index.xml" }, produces = MediaType.TEXT_XML_VALUE)
    @ResponseBody
    public String serverSideHandler(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) throws IOException {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);

        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<sitemapindex xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");
        sb.append("<sitemap>");
        sb.append("<loc>http://www.swarajabhiyan.org.com/sitemap/news.xml</loc>");
        sb.append("<changeFreq>daily</changeFreq>");
        sb.append("<lastmod>" + sdf.format(calendar.getTime()) + "</lastmod>");
        sb.append("</sitemap>");
        /*
        sb.append("<sitemap>");
        sb.append("<loc>http://www.swarajabhiyan.org.com/sitemap/events.xml</loc>");
        sb.append("<lastmod>" + sdf.format(calendar.getTime()) + "</lastmod>");
        sb.append("</sitemap>");
        */
        sb.append("</sitemapindex>");
        return sb.toString();
    }

    @ResponseBody
    @RequestMapping(value = { "/sitemap/news.xml" }, produces = MediaType.TEXT_XML_VALUE)
    public String defaultContentApiMethod(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) throws AppException {
        List<News> allNews = newsService.getAllGlobalNews();
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<sitemapindex xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" xmlns:news=\"http://www.google.com/schemas/sitemap-news/0.9\">");

        sb.append("<sitemap>");
        for (News oneNews : allNews) {
            sb.append("<url>");
            sb.append("<loc>http://www.swarajabhiyan.org/content/news"+oneNews.getId()+"</loc>");
            sb.append("<news:news>");
            sb.append("   <news:publication>");
            sb.append("   <news:name>Swaraj Abhiyan</news:name>");
            sb.append("   <news:language>en</news:language>");
            sb.append("   </news:publication>");
            sb.append("   <news:access>Subscription</news:access>");
            sb.append("   <news:genres>PressRelease, Swaraj Abhiyan, News</news:genres>");
            if(oneNews.getPublishDate() != null){
                sb.append("   <news:publication_date>"+sdf.format(oneNews.getPublishDate())+"</news:publication_date>");    
            }else if(oneNews.getDateModified() != null){
                sb.append("   <news:publication_date>"+sdf.format(oneNews.getDateModified())+"</news:publication_date>");
            }
            
            sb.append("   <news:title>"+oneNews.getTitle()+"</news:title>");
            String keyWords = oneNews.getTitle().replaceAll(" ", ",");
            sb.append("   <news:keywords>"+keyWords+"</news:keywords>");
            sb.append("   </news:news>");
            sb.append("</url>");
        }
        sb.append("</sitemap>");
        return sb.toString();
    }

}
