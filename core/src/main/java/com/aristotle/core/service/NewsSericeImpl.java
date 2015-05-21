package com.aristotle.core.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.ContentTweet;
import com.aristotle.core.persistance.Location;
import com.aristotle.core.persistance.News;
import com.aristotle.core.persistance.repo.LocationRepository;
import com.aristotle.core.persistance.repo.NewsRepository;

@Service
public class NewsSericeImpl implements NewsService {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private LocationRepository locationRepository;

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
    public List<News> getAllPublishedNews(int totalNews) {
        Pageable pageable = new PageRequest(0, totalNews);
        return newsRepository.getGlobalNews(pageable);
    }

    @Override
    public News saveNews(News news, List<ContentTweet> contentTweetDtos, Long locationId) {
        news = newsRepository.save(news);
        addLocationToNews(news, locationId);
        return news;
    }

    private void addLocationToNews(News news, Long locationId) {
        if (locationId == null || locationId <= 0) {
            return;
        }
        Location location = locationRepository.findOne(locationId);
        if (location == null) {
            return;
        }
        if (news.getLocations() == null) {
            news.setLocations(new HashSet<Location>());
        }
        news.getLocations().add(location);
    }

    @Override
    public List<News> getAllGlobalNews() throws AppException {
        Pageable pageable = new PageRequest(0, 100);
        return newsRepository.getGlobalNews(pageable);
    }

    @Override
    public List<News> getAllLocationNews(Long locationId) throws AppException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ContentTweet> getNewsContentTweets(Long newsId) throws AppException {
        // TODO Auto-generated method stub
        return null;
    }

}
