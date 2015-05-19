package com.aristotle.core.persistance.repo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.aristotle.core.persistance.News;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
@DatabaseSetup(NewsRepositoryTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { NewsRepositoryTest.DATASET })
public class NewsRepositoryTest extends BaseRepositoryTest {
    protected static final String DATASET = "classpath:NewsRepository/test01.xml";

    @Autowired
    private NewsRepository newsRepository;

    @Test
    public void test01() {
        System.out.println("Test");
        News news = newsRepository.findOne(1L);
        System.out.println("news=" + news);
    }
}
