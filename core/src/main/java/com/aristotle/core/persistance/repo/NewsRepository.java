package com.aristotle.core.persistance.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aristotle.core.persistance.News;

public interface NewsRepository extends JpaRepository<News, Long> {

    @Query("select news from News news where news.global=true order by dateCreated desc")
    public List<News> getGlobalNews(Pageable pageable);

    @Query("select news from News news join news.locations locations where locations.id in ?1 and news.contentStatus='Published' order by dateCreated desc")
    public List<News> getLocationPublishedNews(Set<Long> locationIds, Pageable pageable);

    @Query("select news from News news where news.global=true  and news.contentStatus='Published' order by dateCreated desc")
    public List<News> getGlobalPublishdNews(Pageable pageable);

    @Query("select news from News news join news.states states where states.id = ?1")
    public List<News> getStateNews(Long stateId);

    @Query("select news from News news join news.districts districts where districts.id = ?1")
    public List<News> getDistrictNews(Long districtId);

    @Query("select news from News news join news.parliamentConstituencies parliamentConstituencies where parliamentConstituencies.id = ?1")
    public List<News> getParliamentConstituencyNews(Long parliamentConstituencyId);

    @Query("select news from News news join news.assemblyConstituencies assemblyConstituencies where assemblyConstituencies.id = ?1")
    public List<News> getAssemblyConstituencyNews(Long assemblyConstituencyId);

    @Query("select news from News news join news.countries countries where countries.id = ?1")
    public List<News> getCountryNews(Long countryId);

    @Query("select news from News news join news.countryRegions countryRegions where countryRegions.id = ?1")
    public List<News> getCountryRegionNews(Long countryRegionId);

    @Query("select news from News news join news.countryRegionsAreas countryRegionsAreas where countryRegionsAreas.id = ?1")
    public List<News> getCountryRegionAreaNews(Long countryRegionsAreaId);

    /*
    public List<News> getAllNewss(int totalItems, int pageNumber);
	
    public List<News> getAllNewss();
	
    public List<News> getAllPublishedNewss();
	
    public News getNewsByWebUrl(String webUrl);
	
    public News getNewsByOriginalUrl(String originalUrl);
	
    public long getLastNewsId();
	
    public List<News> getNewsItemsAfterId(long newsId);
	
    public List<Long> getAllNewsByLocation(long acId, long districtId, long stateId);
	
    public List<Long> getNewsByLocation(long pcId, long stateId);
	
	
    
	
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

	*/
}
