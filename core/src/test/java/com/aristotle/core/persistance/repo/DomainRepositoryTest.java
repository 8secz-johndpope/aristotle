package com.aristotle.core.persistance.repo;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.aristotle.core.persistance.Domain;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

@DatabaseSetup({ DomainRepositoryTest.COMMON_USER_SET })
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { DomainRepositoryTest.COMMON_USER_SET })
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
