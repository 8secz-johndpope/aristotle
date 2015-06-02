package com.aristotle.core.service;

import java.util.List;

import com.aristotle.core.persistance.Location;
import com.aristotle.core.persistance.Video;

public interface VideoService {

    List<Video> getLocationVideos(Location location, int size);

    Video getVideoById(Long videoId);

}
