package com.aristotle.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristotle.core.enums.PostLocationType;
import com.aristotle.core.persistance.News;
import com.aristotle.core.persistance.repo.NewsRepository;

@Service
public class NewsSericeImpl implements NewsService {

    @Autowired
    private NewsRepository newsRepository;

    @Override
    public News publishNews(Long newsId) {
        newsRepository.findOne(newsId);
        return null;
    }

    @Override
    public News rejectNews(Long newsId, String reason) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public News getNewsByOriginalUrl(String originalUrl) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public News getNewsById(Long newsId) {
        return newsRepository.findOne(newsId);
    }

    @Override
    public List<News> getNews(PostLocationType locationType, Long locationId) {
        List<News> news = null;
        switch (locationType) {
        case Global:
        case NA:
            news = newsRepository.getGlobalNews();
            break;
        case STATE:
            news = newsRepository.getStateNews(locationId);
            break;
        case DISTRICT:
            news = newsRepository.getDistrictNews(locationId);
            break;
        case AC:
            news = newsRepository.getAssemblyConstituencyNews(locationId);
            break;
        case PC:
            news = newsRepository.getParliamentConstituencyNews(locationId);
            break;
        case COUNTRY:
            news = newsRepository.getCountryNews(locationId);
            break;
        case REGION:
            news = newsRepository.getCountryRegionNews(locationId);
            break;
        case AREA:
            news = newsRepository.getCountryRegionAreaNews(locationId);
            break;
        }
        return news;
    }

    @Override
    public List<News> getAllPublishedNews() {
        return newsRepository.getGlobalNews();
    }

}
