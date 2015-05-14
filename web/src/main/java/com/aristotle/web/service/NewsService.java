package com.aristotle.web.service;

import java.util.List;

import com.aristotle.web.dto.ContentTweetDto;
import com.aristotle.web.dto.NewsDto;
import com.aristotle.web.dto.PostLocationType;

public interface NewsService {

    NewsDto saveNews(NewsDto newsDto, List<ContentTweetDto> contentTweetDtos, PostLocationType locationType, Long locationId);

    NewsDto publishNews(Long newsId);

    NewsDto rejectNews(Long newsId, String reason);

    NewsDto getNewsByOriginalUrl(String originalUrl);

    NewsDto getNewsById(Long newsId);

    List<NewsDto> getNews(PostLocationType locationType, Long locationId);

    List<NewsDto> getAllPublishedNews();
}
