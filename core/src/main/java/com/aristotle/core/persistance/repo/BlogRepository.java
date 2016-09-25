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

}
