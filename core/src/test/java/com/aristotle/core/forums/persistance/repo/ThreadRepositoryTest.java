package com.aristotle.core.forums.persistance.repo;




import org.junit.Assert;

import org.junit.Test;

import com.aristotle.core.forums.persistance.Thread;
import com.aristotle.core.persistance.repo.*;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
@DatabaseSetup(ThreadRepositoryTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { ThreadRepositoryTest.DATASET })

public class ThreadRepositoryTest extends BaseRepositoryTest {
    protected static final String DATASET = "classpath:ThreadRepository/test01.xml";

    @Autowired
    private ThreadRepository threadRepository;

    @Test
    public void testfindThreadbyId() {
        System.out.println("Test");
        Thread thread = threadRepository.findById(1L);
        Assert.assertEquals(1L, thread.getId());
        System.out.println("Thread=" + thread);
    }
}
