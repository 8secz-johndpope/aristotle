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

    @Query("select news from News news join news.locations locations where locations.id in ?1 and news.contentStatus='Published' order by news.dateCreated desc")
    public List<News> getLocationPublishedNews(Set<Long> locationIds, Pageable pageable);

    @Query("select news from News news join news.locations locations where locations.id in ?1 order by news.dateCreated desc")
    public List<News> getLocationNews(Set<Long> locationIds, Pageable pageable);

    @Query("select count(news) from News news join news.locations locations where locations.id in ?1 and news.contentStatus='Published'")
    public long getLocationPublishedNewsCount(Set<Long> locationIds);

    @Query("select news from News news where news.global=true  and news.contentStatus='Published' order by news.dateCreated desc")
    public List<News> getGlobalPublishdNews(Pageable pageable);

    @Query("select count(news) from News news where news.global=true  and news.contentStatus='Published'")
    public long getGlobalPublishdNewsCount();

}
