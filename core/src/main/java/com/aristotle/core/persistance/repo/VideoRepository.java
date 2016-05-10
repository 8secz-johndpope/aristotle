package com.aristotle.core.persistance.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aristotle.core.persistance.Video;

public interface VideoRepository extends JpaRepository<Video, Long> {

    Video getVideoByYoutubeVideoId(String videoId);

    @Query("select distinct video from Video video where video.global=true order by publishDate desc")
    public abstract List<Video> getGloablVideos(Pageable pageable);

    @Query("select count(video) from Video video where video.global=true")
    public abstract long getGloablVideoCount();

    @Query("select distinct video from Video video join video.locations locations where locations.id in ?1 order by video.publishDate desc")
    public abstract List<Video> getLocationVideos(Set<Long> locationId, Pageable pageable);

    @Query("select count(video) from Video video join video.locations locations where locations.id in ?1")
    public long getLocationVideoCount(Set<Long> locationId);

    @Query("select distinct video from Video video join video.locations locations where locations.id=?1 order by video.publishDate desc")
    public abstract List<Video> getLocationsVideos(List<Long> locationIds);

    /*
	public abstract List<Video> getAllVideos(int totalItems, int pageNumber);
	
	public abstract List<Video> getAllVideos();
	
	public abstract List<Video> getAllPublishedVideos();
	
	public abstract Video getVideoByWebUrl(String webUrl);
	
	
	
	public abstract long getLastVideoId();
	
	public abstract List<Video> getVideoItemsAfterId(long videoId);

	public abstract List<Long> getVideoByLocation(long acId, long districtId, long stateId);
	
	public abstract List<Long> getVideoByLocation(long pcId, long stateId);

	
	public abstract List<Long> getAllVideoByAc(long acId);
	
	public abstract List<Long> getAllVideoByPc(long pcId);
	
	public abstract List<Long> getAllVideoByDistrict(long districtId);
	
	public abstract List<Long> getAllVideoByState(long stateId);
	
	public abstract List<Long> getAllVideoByCountry(long countryId);
	
	public abstract List<Long> getAllVideoByCountryRegion(long countryRegionId);
	
	
	public abstract Map<Long, List<Long>> getVideoItemsOfAllAc();
	
	public abstract Map<Long, List<Long>> getVideoItemsOfAllPc();
	
	public abstract Map<Long, List<Long>> getVideoItemsOfAllDistrict();
	
	public abstract Map<Long, List<Long>> getVideoItemsOfAllState();
	
	public abstract Map<Long, List<Long>> getVideoItemsOfAllCountry();
	
	public abstract Map<Long, List<Long>> getVideoItemsOfAllCountryRegion();
	*/
}
