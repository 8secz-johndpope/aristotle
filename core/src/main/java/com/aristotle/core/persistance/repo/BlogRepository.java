package com.aristotle.core.persistance.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aristotle.core.persistance.Blog;

public interface BlogRepository extends JpaRepository<Blog, Long> {

    @Query("select blog from Blog blog where blog.global=true order by dateCreated desc")
    public List<Blog> getGlobalBlog(Pageable pageable);

    @Query("select blog from Blog blog join blog.locations locations where locations.id in ?1 and blog.contentStatus='Published' order by blog.dateCreated desc")
    public List<Blog> getLocationPublishedBlog(Set<Long> locationIds, Pageable pageable);

    @Query("select blog from Blog blog where blog.global=true  and blog.contentStatus='Published' order by blog.dateCreated desc")
    public List<Blog> getGlobalPublishdBlog(Pageable pageable);

    /*
     * @Query("select blog from Blog blog join blog.states states where states.id = ?1") public List<Blog> getStateBlog(Long stateId);
     * 
     * @Query("select blog from Blog blog join blog.districts districts where districts.id = ?1") public List<Blog> getDistrictBlog(Long districtId);
     * 
     * @Query("select blog from Blog blog join blog.parliamentConstituencies parliamentConstituencies where parliamentConstituencies.id = ?1") public List<Blog> getParliamentConstituencyBlog(Long
     * parliamentConstituencyId);
     * 
     * @Query("select blog from Blog blog join blog.assemblyConstituencies assemblyConstituencies where assemblyConstituencies.id = ?1") public List<Blog> getAssemblyConstituencyBlog(Long
     * assemblyConstituencyId);
     * 
     * @Query("select blog from Blog blog join blog.countries countries where countries.id = ?1") public List<Blog> getCountryBlog(Long countryId);
     * 
     * @Query("select blog from Blog blog join blog.countryRegions countryRegions where countryRegions.id = ?1") public List<Blog> getCountryRegionBlog(Long countryRegionId);
     * 
     * @Query("select blog from Blog blog join blog.countryRegionsAreas countryRegionsAreas where countryRegionsAreas.id = ?1") public List<Blog> getCountryRegionAreaBlog(Long countryRegionsAreaId);
     * 
     * // public List<Blog> getBlogItemsAfterId(long blogId);
     * 
     * @Query("select bloglist.blogId from ((select blog_id as blogId from blog_ac where ac_id = :acId) union (select blog_id as blogId from blog_district where district_id= :districtId) " +
     * "union (select blog_id as blogId from blog_state where state_id= :stateId) union (select id as blogId from blogs where global_allowed= true)) bloglist order by bloglist.blogId desc") public
     * abstract List<Long> getBlogByLocation(long acId, long districtId, long stateId);
     * 
     * @Query("select bloglist.blogId from ((select blog_id as blogId from blog_pc where pc_id = :pcId) union (select blog_id as blogId from blog_state where state_id= :stateId) " +
     * "union (select id as blogId from blogs where global_allowed= true)) bloglist order by bloglist.blogId desc") public abstract List<Long> getBlogByLocation(long pcId, long stateId);
     * 
     * public abstract List<Blog> getGlobalBlog();
     * 
     * public abstract List<Blog> getStateBlog(Long stateId);
     * 
     * public abstract List<Blog> getDistrictBlog(Long districtId);
     * 
     * public abstract List<Blog> getAcBlog(Long acId);
     * 
     * public abstract List<Blog> getPcBlog(Long pcId);
     * 
     * public abstract List<Blog> getCountryBlog(Long countryId);
     * 
     * public abstract List<Blog> getCountryRegionBlog(Long countryRegionId);
     * 
     * public abstract List<Blog> getCountryRegionAreaBlog(Long countryRegionAreaId);
     * 
     * 
     * public abstract List<Long> getAllBlogByAc(long acId);
     * 
     * public abstract List<Long> getAllBlogByPc(long pcId);
     * 
     * public abstract List<Long> getAllBlogByDistrict(long districtId);
     * 
     * public abstract List<Long> getAllBlogByState(long stateId);
     * 
     * public abstract List<Long> getAllBlogByCountry(long countryId);
     * 
     * public abstract List<Long> getAllBlogByCountryRegion(long countryRegionId);
     * 
     * 
     * public abstract Map<Long, List<Long>> getBlogItemsOfAllAc();
     * 
     * public abstract Map<Long, List<Long>> getBlogItemsOfAllPc();
     * 
     * public abstract Map<Long, List<Long>> getBlogItemsOfAllDistrict();
     * 
     * public abstract Map<Long, List<Long>> getBlogItemsOfAllState();
     * 
     * public abstract Map<Long, List<Long>> getBlogItemsOfAllCountry();
     * 
     * public abstract Map<Long, List<Long>> getBlogItemsOfAllCountryRegion();
     */
}
