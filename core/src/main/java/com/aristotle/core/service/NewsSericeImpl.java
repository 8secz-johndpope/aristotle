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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<News> getNews(PostLocationType locationType, Long locationId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<News> getAllPublishedNews() {
        // TODO Auto-generated method stub
        return null;
    }

}
