package com.aristotle.core.persistance.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aristotle.core.persistance.CallCampaign;

public interface CallCampaignRepository extends JpaRepository<CallCampaign, Long> {
/*
    @Query("select blog from Blog blog where blog.global=true order by dateCreated desc")
    public List<Blog> getGlobalBlog(Pageable pageable);

    @Query("select blog from Blog blog join blog.locations locations where locations.id in ?1 and blog.contentStatus='Published' order by blog.dateCreated desc")
    public List<Blog> getLocationPublishedBlog(Set<Long> locationIds, Pageable pageable);

    @Query("select blog from Blog blog where blog.global=true  and blog.contentStatus='Published' order by blog.dateCreated desc")
    public List<Blog> getGlobalPublishdBlog(Pageable pageable);

*/
}
