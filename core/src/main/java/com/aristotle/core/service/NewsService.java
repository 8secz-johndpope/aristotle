package com.aristotle.core.service;

import java.util.List;

import com.aristotle.core.enums.PostLocationType;
import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.News;

public interface NewsService {

    // News saveNews(News News, List<ContentTweetDto> contentTweetDtos, PostLocationType locationType, Long locationId);

    News publishNews(Long newsId) throws AppException;

    News rejectNews(Long newsId, String reason) throws AppException;

    News getNewsByOriginalUrl(String originalUrl) throws AppException;

    News getNewsById(Long newsId) throws AppException;

    List<News> getNews(PostLocationType locationType, Long locationId) throws AppException;

    List<News> getAllPublishedNews() throws AppException;
}
