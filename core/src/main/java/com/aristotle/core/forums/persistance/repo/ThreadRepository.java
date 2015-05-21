package com.aristotle.core.forums.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aristotle.core.forums.persistance.Thread;

@Repository
public interface ThreadRepository extends JpaRepository<Thread, Long> {

    /**
     * Find a thread by Id
     * @param id
     * @return
     */
    public Thread findById(Long id);
    
    /**
     * Find all threads for a given location
     * @param locationId
     * @return
     */
    public List<Thread> findByLocationId(Long locationId);
    
    /**
     * Find all threads for a given user
     * @param userId
     * @return
     */
    public List<Thread> findByUserId(Long userId);
    
    
    
}
