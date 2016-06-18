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
import com.aristotle.core.persistance.DomainPageTemplate;
import com.aristotle.core.persistance.DomainTemplate;
import com.aristotle.core.persistance.UrlMapping;
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
@DatabaseSetup({ DomainPageTemplateRepositoryTest.COMMON_USER_SET })
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { DomainPageTemplateRepositoryTest.COMMON_USER_SET })
*/
@Ignore
public class DomainPageTemplateRepositoryTest extends BaseRepositoryTest {
    // protected static final String DATASET = "classpath:DomainPageTemplateRepository/test01.xml";

    @Autowired
    private DomainRepository domainRepository;
    @Autowired
    private DomainTemplateRepository domainTemplateRepository;
    @Autowired
    private UrlMappingRepository urlMappingRepository;
    @Autowired
    private DomainPageTemplateRepository domainPageTemplateRepository;



    @Test
    public void test01() {
        Domain domain = createDomain(domainRepository, true, "www.arsitotle.com");
        DomainTemplate domainTemplate = createDomainTemplate(domainTemplateRepository, domain, 1L, "My Template");
        UrlMapping urlMapping = createUrlMapping(urlMappingRepository, true, "/news/{newsId}");

        DomainPageTemplate domainPageTemplate = new DomainPageTemplate();
        domainPageTemplate.setDomainTemplate(domainTemplate);
        domainPageTemplate.setHtmlContent("<html><div>Some Html content</div></html>");
        domainPageTemplate.setPageTemplateId(1L);
        domainPageTemplate.setUrlMapping(urlMapping);

        domainPageTemplate = domainPageTemplateRepository.save(domainPageTemplate);

        checkAuditFields(domainPageTemplate);

        System.out.println("domainPageTemplate=" + domainPageTemplate);
        checkFindOne(domainPageTemplate);
        checkFindAll(domainPageTemplate);

    }



    private void checkFindOne(DomainPageTemplate savedDdomainPageTemplate) {
        DomainPageTemplate domainPageTemplate = domainPageTemplateRepository.findOne(savedDdomainPageTemplate.getId());
        assertEquals(savedDdomainPageTemplate, domainPageTemplate);
    }

    private void checkFindAll(DomainPageTemplate savedDdomainPageTemplate) {
        List<DomainPageTemplate> domainPageTemplates = domainPageTemplateRepository.findAll();
        Assert.assertEquals(1, domainPageTemplates.size());
        assertEquals(savedDdomainPageTemplate, domainPageTemplates.get(0));
    }
}
