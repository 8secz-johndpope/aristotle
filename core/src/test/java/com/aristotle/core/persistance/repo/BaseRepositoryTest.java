package com.aristotle.core.persistance.repo;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.aristotle.core.DatabaseConfigForTest;
import com.aristotle.core.persistance.BaseEntity;
import com.aristotle.core.persistance.Domain;
import com.aristotle.core.persistance.DomainPageTemplate;
import com.aristotle.core.persistance.DomainTemplate;
import com.aristotle.core.persistance.UrlMapping;
import com.aristotle.core.persistance.User;
import com.github.springtestdbunit.DbUnitTestExecutionListener;

@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DatabaseConfigForTest.class)
@DirtiesContext
@Transactional
public class BaseRepositoryTest {
    protected static final String COMMON_USER_SET = "classpath:common/user.xml";

    @Autowired
    private UserRepository userRepository;


    @Before
    public void init() {
        User user = userRepository.findOne(1L);
        System.out.println("Setting user in Application Context " + user);
        //Authentication authentication = new PreAuthenticatedAuthenticationToken(user, null, null);
        //SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    protected void checkAuditFields(BaseEntity baseEntity) {
        Assert.assertNotNull(baseEntity.getCreatorId());
        Assert.assertNotNull(baseEntity.getModifierId());
        Assert.assertNotNull(baseEntity.getDateCreated());
        Assert.assertNotNull(baseEntity.getDateModified());
        Assert.assertNotNull(baseEntity.getVer());
        Assert.assertNotNull(baseEntity.getId());
    }
    protected UrlMapping createUrlMapping(UrlMappingRepository urlMappingRepository, boolean active, String urlPattern) {
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setActive(active);
        urlMapping.setUrlPattern(urlPattern);
        urlMapping = urlMappingRepository.save(urlMapping);
        return urlMapping;
    }

    protected DomainTemplate createDomainTemplate(DomainTemplateRepository domainTemplateRepository, Domain domain, Long templateId, String name) {
        DomainTemplate domainTemplate = new DomainTemplate();
        domainTemplate.setDomain(domain);
        domainTemplate.setName(name);
        domainTemplate.setTemplateId(templateId);
        domainTemplate = domainTemplateRepository.save(domainTemplate);
        return domainTemplate;
    }

    protected Domain createDomain(DomainRepository domainRepository, boolean active, String domainName) {
        Domain domain = new Domain();
        domain.setActive(active);
        domain.setName(domainName);
        domain = domainRepository.save(domain);
        return domain;
    }

    protected void assertBaseEquals(BaseEntity expected, BaseEntity actual) {
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getVer(), actual.getVer());
        Assert.assertEquals(expected.getCreatorId(), actual.getCreatorId());
        Assert.assertEquals(expected.getModifierId(), actual.getModifierId());
        Assert.assertEquals(expected.getDateCreated(), actual.getDateCreated());
        Assert.assertEquals(expected.getDateModified(), actual.getDateModified());
    }

    protected void assertEquals(Domain expected, Domain actual) {
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.isActive(), actual.isActive());
        assertBaseEquals(expected, actual);
    }

    protected void assertEquals(DomainPageTemplate expected, DomainPageTemplate actual) {
        Assert.assertEquals(expected.getHtmlContent(), actual.getHtmlContent());
        assertEquals(expected.getDomainTemplate(), actual.getDomainTemplate());
        assertEquals(expected.getUrlMapping(), actual.getUrlMapping());
        assertBaseEquals(expected, actual);
    }

    protected void assertEquals(DomainTemplate expected, DomainTemplate actual) {
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getDomain(), actual.getDomain());
        Assert.assertEquals(expected.getTemplateId(), actual.getTemplateId());
        assertBaseEquals(expected, actual);
    }

    protected void assertEquals(UrlMapping expected, UrlMapping actual) {
        Assert.assertEquals(expected.getUrlPattern(), actual.getUrlPattern());
        Assert.assertEquals(expected.isActive(), actual.isActive());
        assertBaseEquals(expected, actual);
    }

}
