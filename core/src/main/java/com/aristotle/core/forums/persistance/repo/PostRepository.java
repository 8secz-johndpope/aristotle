package com.aristotle.core.forums.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aristotle.core.forums.persistance.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    public Post findById(Long id);
    
    public List<Post> findByThreadId(Long threadId);
    
    public List<Post> findByUserId(Long userId);
}
