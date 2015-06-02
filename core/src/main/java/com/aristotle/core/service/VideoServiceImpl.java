package com.aristotle.core.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.aristotle.core.persistance.Location;
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
    public List<Video> getLocationVideos(Location location, int size) {
        System.out.println("Getting Data From Database");
        Pageable pageable = new PageRequest(0, size);
        if(location == null){
            return videoRepository.getAllGloablVideos(pageable);
        }
        return videoRepository.getLocationVideos(location.getId(), pageable);
    }

    @Override
    public Video getVideoById(Long videoId) {
        return videoRepository.findOne(videoId);
    }

}
