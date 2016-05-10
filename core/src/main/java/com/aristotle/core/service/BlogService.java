package com.aristotle.core.service;

import java.util.List;
import java.util.Set;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Blog;
import com.aristotle.core.persistance.ContentTweet;

public interface BlogService {

    //

    Blog saveBlog(Blog blog, List<ContentTweet> contentTweetDtos, Long locationId);

    Blog publishBlog(Long blogId) throws AppException;

    Blog rejectBlog(Long blogId, String reason) throws AppException;

    Blog getBlogByOriginalUrl(String originalUrl) throws AppException;

    Blog getBlogById(Long blogId) throws AppException;

    List<Blog> getAllPublishedBlog(int totalBlog) throws AppException;

    List<Blog> getAllGlobalBlog() throws AppException;

    List<Blog> getAllLocationPublishedBlog(Set<Long> locationIds, int pageNumber, int pageSize) throws AppException;

    List<ContentTweet> getBlogContentTweets(Long blogId) throws AppException;

}
