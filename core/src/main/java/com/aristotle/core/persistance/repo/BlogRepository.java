package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aristotle.core.persistance.Blog;

public interface BlogRepository extends JpaRepository<Blog, Long> {

    @Query("select b from Blog b where b.contentStatus = 'Published'")
    public List<Blog> getAllPublishedBlogs();
	
    // public List<Blog> getBlogItemsAfterId(long blogId);
    /*
    @Query("select bloglist.blogId from ((select blog_id as blogId from blog_ac where ac_id = :acId) union (select blog_id as blogId from blog_district where district_id= :districtId) "
            + "union (select blog_id as blogId from blog_state where state_id= :stateId) union (select id as blogId from blogs where global_allowed= true)) bloglist order by bloglist.blogId desc")
	public abstract List<Long> getBlogByLocation(long acId, long districtId, long stateId);
	
    @Query("select bloglist.blogId from ((select blog_id as blogId from blog_pc where pc_id = :pcId) union (select blog_id as blogId from blog_state where state_id= :stateId) "
            + "union (select id as blogId from blogs where global_allowed= true)) bloglist order by bloglist.blogId desc")
	public abstract List<Long> getBlogByLocation(long pcId, long stateId);

	public abstract List<Blog> getGlobalBlog();
	
	public abstract List<Blog> getStateBlog(Long stateId);
	
	public abstract List<Blog> getDistrictBlog(Long districtId);
	
	public abstract List<Blog> getAcBlog(Long acId);
	
	public abstract List<Blog> getPcBlog(Long pcId);
	
	public abstract List<Blog> getCountryBlog(Long countryId);
	
	public abstract List<Blog> getCountryRegionBlog(Long countryRegionId);
	
	public abstract List<Blog> getCountryRegionAreaBlog(Long countryRegionAreaId);

	
	public abstract List<Long> getAllBlogByAc(long acId);
	
	public abstract List<Long> getAllBlogByPc(long pcId);
	
	public abstract List<Long> getAllBlogByDistrict(long districtId);
	
	public abstract List<Long> getAllBlogByState(long stateId);
	
	public abstract List<Long> getAllBlogByCountry(long countryId);
	
	public abstract List<Long> getAllBlogByCountryRegion(long countryRegionId);
	
	
	public abstract Map<Long, List<Long>> getBlogItemsOfAllAc();
	
	public abstract Map<Long, List<Long>> getBlogItemsOfAllPc();
	
	public abstract Map<Long, List<Long>> getBlogItemsOfAllDistrict();
	
	public abstract Map<Long, List<Long>> getBlogItemsOfAllState();
	
	public abstract Map<Long, List<Long>> getBlogItemsOfAllCountry();
	
	public abstract Map<Long, List<Long>> getBlogItemsOfAllCountryRegion();
	*/
}
