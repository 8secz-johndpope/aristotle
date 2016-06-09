package com.aristotle.core.service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aristotle.core.enums.ContentStatus;
import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Blog;
import com.aristotle.core.persistance.ContentTweet;
import com.aristotle.core.persistance.Location;
import com.aristotle.core.persistance.repo.BlogRepository;
import com.aristotle.core.persistance.repo.LocationRepository;

@Service
@Transactional(rollbackFor=Exception.class)
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public Blog publishBlog(Long blogId) {
        Blog blog = blogRepository.findOne(blogId);
        blog.setContentStatus(ContentStatus.Published);
        if (blog.getPublishDate() == null) {
            blog.setPublishDate(new Date());
        }
        blog = blogRepository.save(blog);
        return blog;
    }

    @Override
    public Blog rejectBlog(Long blogId, String reason) {
        Blog blog = blogRepository.findOne(blogId);
        blog.setContentStatus(ContentStatus.Rejected);
        blog = blogRepository.save(blog);
        return blog;
    }

    @Override
    public Blog getBlogByOriginalUrl(String originalUrl) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Blog getBlogById(Long blogId) {
        return blogRepository.findOne(blogId);
    }

    @Override
    public List<Blog> getAllPublishedBlog(int totalBlog) {
        Pageable pageable = new PageRequest(0, totalBlog);
        return blogRepository.getGlobalBlog(pageable);
    }

    @Override
    public Blog saveBlog(Blog blog, List<ContentTweet> contentTweetDtos, Long locationId) {
        blog = blogRepository.save(blog);

        addLocationToBlog(blog, locationId);
        return blog;
    }

    private void addLocationToBlog(Blog blog, Long locationId) {
        if (locationId == null || locationId <= 0) {
            return;
        }
        Location location = locationRepository.findOne(locationId);
        if (location == null) {
            return;
        }
        if (blog.getLocations() == null) {
            blog.setLocations(new HashSet<Location>());
        }
        blog.getLocations().add(location);
    }

    @Override
    public List<Blog> getAllGlobalBlog() throws AppException {
        Pageable pageable = new PageRequest(0, 100);
        return blogRepository.getGlobalBlog(pageable);
    }

    @Override
    public List<ContentTweet> getBlogContentTweets(Long blogId) throws AppException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Cacheable("blogs")
    public List<Blog> getAllLocationPublishedBlog(Set<Long> locationIds, int pageNumber, int pageSize) throws AppException {
        System.out.println("Getting blog for " + locationIds + ", page number = " + pageNumber + ", pageSize=" + pageSize);
        Pageable pageable = new PageRequest(pageNumber, pageSize);
        if (locationIds == null || locationIds.isEmpty()) {
            return blogRepository.getGlobalPublishdBlog(pageable);
        }
        return blogRepository.getLocationPublishedBlog(locationIds, pageable);
    }

}
