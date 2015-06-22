package com.aristotle.core.service;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.aristotle.core.persistance.Video;
import com.aristotle.core.persistance.repo.LocationRepository;
import com.aristotle.core.persistance.repo.VideoRepository;

@Service
@Transactional
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private LocationRepository locationRepository;

    @Override
    @Cacheable(value = "videos")
    public List<Video> getLocationVideos(Set<Long> locationIds, int pageNumber, int size) {
        System.out.println("Getting Data From Database");
        Pageable pageable = new PageRequest(pageNumber, size);
        if (locationIds == null || locationIds.isEmpty()) {
            return videoRepository.getGloablVideos(pageable);
        }
        return videoRepository.getLocationVideos(locationIds, pageable);
    }

    @Override
    public Video getVideoById(Long videoId) {
        return videoRepository.findOne(videoId);
    }

    @Override
    public long getLocationVideosCount(Set<Long> locationIds) {
        if (locationIds == null || locationIds.isEmpty()) {
            return videoRepository.getGloablVideoCount();
        }
        return videoRepository.getLocationVideoCount(locationIds);
    }

}
