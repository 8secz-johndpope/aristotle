package com.aristotle.core.persistance.repo;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.News;

public interface NewsRepository extends JpaRepository<News, Long> {

    public List<News> getAllNewss(int totalItems, int pageNumber);
	
    public List<News> getAllNewss();
	
    public List<News> getAllPublishedNewss();
	
    public News getNewsByWebUrl(String webUrl);
	
    public News getNewsByOriginalUrl(String originalUrl);
	
    public long getLastNewsId();
	
    public List<News> getNewsItemsAfterId(long newsId);
	
    public List<Long> getAllNewsByLocation(long acId, long districtId, long stateId);
	
    public List<Long> getNewsByLocation(long pcId, long stateId);
	
	
    public List<News> getGlobalNews();
	
    public List<News> getStateNews(Long stateId);
	
    public List<News> getDistrictNews(Long districtId);
	
    public List<News> getAcNews(Long acId);
	
    public List<News> getPcNews(Long pcId);
	
    public List<News> getCountryNews(Long countryId);
	
    public List<News> getCountryRegionNews(Long countryRegionId);
	
    public List<News> getCountryRegionAreaNews(Long countryRegionAreaId);
	
    public List<Long> getAllNewsByAc(long acId);
	
    public List<Long> getAllNewsByPc(long pcId);
	
    public List<Long> getAllNewsByDistrict(long districtId);
	
    public List<Long> getAllNewsByState(long stateId);
	
    public List<Long> getAllNewsByCountry(long countryId);
	
    public List<Long> getAllNewsByCountryRegion(long countryRegionId);
	
	
    public Map<Long, List<Long>> getNewsItemsOfAllAc();
	
    public Map<Long, List<Long>> getNewsItemsOfAllPc();
	
    public Map<Long, List<Long>> getNewsItemsOfAllDistrict();
	
    public Map<Long, List<Long>> getNewsItemsOfAllState();
	
    public Map<Long, List<Long>> getNewsItemsOfAllCountry();
	
    public Map<Long, List<Long>> getNewsItemsOfAllCountryRegion();

	
}
