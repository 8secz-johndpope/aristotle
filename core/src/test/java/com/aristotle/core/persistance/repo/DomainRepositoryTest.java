package com.aristotle.core.persistance.repo;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Ignore;
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
import com.aristotle.core.persistance.Domain;
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
@DatabaseSetup({ DomainRepositoryTest.COMMON_USER_SET })
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { DomainRepositoryTest.COMMON_USER_SET })
*/
@Ignore
public class DomainRepositoryTest extends BaseRepositoryTest {
    // protected static final String DATASET = "classpath:common/user.xml";

    @Autowired
    private DomainRepository domainRepository;


    @Test
    public void test01() {
        Domain domain = new Domain();
        domain.setActive(true);
        domain.setName("www.sa.org");
        domain = domainRepository.save(domain);
        checkAuditFields(domain);
        checkFindOne(domain);
        checkFindAll(domain);

    }

    private void checkFindOne(Domain savedDomain) {
        Domain domain = domainRepository.findOne(savedDomain.getId());
        assertEquals(savedDomain, domain);
    }

    private void checkFindAll(Domain savedDomain) {
        List<Domain> domains = domainRepository.findAll();
        Assert.assertEquals(1, domains.size());
        assertEquals(savedDomain, domains.get(0));
    }
}
