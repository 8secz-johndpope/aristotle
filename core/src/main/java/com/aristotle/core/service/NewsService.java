package com.aristotle.core.service;

import java.util.List;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.ContentTweet;
import com.aristotle.core.persistance.News;

public interface NewsService {

    //

    News saveNews(News news, List<ContentTweet> contentTweetDtos, Long locationId);

    News publishNews(Long newsId) throws AppException;

    News rejectNews(Long newsId, String reason) throws AppException;

    News getNewsByOriginalUrl(String originalUrl) throws AppException;

    News getNewsById(Long newsId) throws AppException;

    List<News> getAllPublishedNews(int totalNews) throws AppException;

    List<News> getAllGlobalNews() throws AppException;

    List<News> getAllLocationNews(Long locationId) throws AppException;

    List<ContentTweet> getNewsContentTweets(Long newsId) throws AppException;

}
