package com.aristotle.core.service;

import java.util.List;
import java.util.Set;

import com.aristotle.core.persistance.Video;

public interface VideoService {

    List<Video> getLocationVideos(Set<Long> locationIds, int pageNumber, int size);

    long getLocationVideosCount(Set<Long> locationIds);

    Video getVideoById(Long videoId);

}
