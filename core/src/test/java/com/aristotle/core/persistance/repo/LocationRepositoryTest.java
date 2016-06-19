package com.aristotle.core.persistance.repo;

import java.util.List;

import javax.transaction.Transactional;

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
import com.aristotle.core.persistance.Location;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
/*
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DatabaseConfigForTest.class)
@DirtiesContext
@Transactional
*/
//@DatabaseSetup({ LocationRepositoryTest.COMMON_USER_SET })
//@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { LocationRepositoryTest.COMMON_USER_SET })
@Ignore
public class LocationRepositoryTest extends BaseRepositoryTest {
    // protected static final String DATASET = "classpath:common/user.xml";

    @Autowired
    private LocationRepository locationRepository;


    @Test
    public void test01() {
        List<Location> allAdminLocations = locationRepository.getAdminLocationsOfUser(2L);
        System.out.println("allAdminLocations = " + allAdminLocations);
        System.out.println("allAdminLocations = " + allAdminLocations.size());
    }


}
