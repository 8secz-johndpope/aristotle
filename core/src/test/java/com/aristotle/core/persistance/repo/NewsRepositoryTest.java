package com.aristotle.core.persistance.repo;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.aristotle.core.DatabaseConfigForTest;
import com.aristotle.core.persistance.News;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

/*
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DatabaseConfigForTest.class)
@DirtiesContext
@Transactional
@DatabaseSetup(NewsRepositoryTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { NewsRepositoryTest.DATASET })
*/
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
