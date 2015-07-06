package com.aristotle.web.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aristotle.core.persistance.Video;
import com.aristotle.core.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private VideoService videoService;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @ExceptionHandler({ Exception.class })
    public String handleException(Exception ex) {
        ex.printStackTrace();
        return ex.getMessage();
    }

    @RequestMapping(value = { "/sitemap/index.xml" })
    @ResponseBody
    public String serverSideHandler(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) throws IOException {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);

        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<sitemapindex xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");
        sb.append("<sitemap>");
        sb.append("<loc>http://www.swarajabhiyan.org/sitemap/news.xml</loc>");
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
    @RequestMapping(value = { "/sitemap/news.xml" })
    public String defaultContentApiMethod(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) throws AppException {
        List<News> allNews = newsService.getAllGlobalNews();
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        // sb.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" xmlns:news=\"http://www.google.com/schemas/sitemap-news/0.9\">");
        sb.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");

        for (News oneNews : allNews) {
            sb.append("<url>");
            sb.append("<loc>http://www.swarajabhiyan.org/content/news/"+oneNews.getId()+"</loc>");
            if(oneNews.getPublishDate() != null){
                //sb.append("   <news:publication_date>"+sdf.format(oneNews.getPublishDate())+"</news:publication_date>");
                sb.append("<lastmod>"+sdf.format(oneNews.getPublishDate())+"</lastmod>");
            }else if(oneNews.getDateModified() != null){
                //sb.append("   <news:publication_date>"+sdf.format(oneNews.getDateModified())+"</news:publication_date>");
                sb.append("<lastmod>"+sdf.format(oneNews.getDateModified())+"</lastmod>");
            }
            sb.append("<changefreq>weekly</changefreq>");
            sb.append("<priority>0.8</priority>");
            /*
            sb.append("<news:news>");
            sb.append("   <news:publication>");
            sb.append("   <news:name>Swaraj Abhiyan</news:name>");
            sb.append("   <news:language>en</news:language>");
            sb.append("   </news:publication>");
            sb.append("   <news:access>Subscription</news:access>");
            sb.append("   <news:genres>PressRelease</news:genres>");

            sb.append("   <news:title>" + StringEscapeUtils.escapeXml(oneNews.getTitle()) + "</news:title>");
            String keyWords = oneNews.getTitle().replaceAll(" ", ",");
            sb.append("   <news:keywords>" + StringEscapeUtils.escapeXml(keyWords) + "</news:keywords>");
            sb.append("   </news:news>");
            */
            sb.append("</url>");
        }
        sb.append("</urlset>");

        return sb.toString();
    }

    @ResponseBody
    @RequestMapping(value = { "/sitemap/custom.xml" })
    public String customSiteMap(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) throws AppException {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");
        addOneUrlToSiteMap(sb, "http://www.swarajabhiyan.org/", new Date(), "daily", "1.0");
        addOneUrlToSiteMap(sb, "http://www.swarajabhiyan.org/organisation/vision", new Date(), "monthly", "0.9");
        addOneUrlToSiteMap(sb, "http://www.swarajabhiyan.org/organisation/constitution", new Date(), "monthly", "0.9");
        addOneUrlToSiteMap(sb, "http://www.swarajabhiyan.org/organisation/nwc", new Date(), "monthly", "0.9");
        addOneUrlToSiteMap(sb, "http://www.swarajabhiyan.org/organisation/nsc", new Date(), "monthly", "0.9");
        addOneUrlToSiteMap(sb, "http://www.swarajabhiyan.org/organisation/contactus", new Date(), "weekly", "0.9");
        addOneUrlToSiteMap(sb, "http://www.swarajabhiyan.org/organisation/minutes_of_meetings", new Date(), "daily", "0.9");
        sb.append("</urlset>");

        return sb.toString();
    }

    @ResponseBody
    @RequestMapping(value = { "/sitemap/videos.xml" })
    public String videoSiteMap(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView modelAndView) throws AppException {
        List<Video> allVideos = videoService.getLocationVideos(null, 0, 10000);
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        // sb.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" xmlns:news=\"http://www.google.com/schemas/sitemap-news/0.9\">");
        sb.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");

        for (Video oneNews : allVideos) {
            sb.append("<url>");
            sb.append("<loc>http://www.swarajabhiyan.org/content/video/"+oneNews.getId()+"</loc>");
            try{
                if(oneNews.getPublishDate() != null){
                    sb.append("<lastmod>"+sdf.format(oneNews.getPublishDate())+"</lastmod>");
                }else if(oneNews.getDateModified() != null){
                    sb.append("<lastmod>"+sdf.format(oneNews.getDateModified())+"</lastmod>");
                }

            }catch(Exception ex){
            }
            sb.append("<changefreq>monthly</changefreq>");
            sb.append("<priority>0.9</priority>");
            sb.append("</url>");
        }
        sb.append("</urlset>");

        return sb.toString();
    }


    private void addOneUrlToSiteMap(StringBuilder sb, String url, Date publishDate, String changefreq, String priority) {
        sb.append("<url>");
        sb.append("<loc>" + url + "</loc>");
        sb.append("<lastmod>" + sdf.format(publishDate) + "</lastmod>");
        sb.append("<changefreq>" + changefreq + "</changefreq>");
        sb.append("<priority>" + priority + "</priority>");
        sb.append("</url>");
    }

}
