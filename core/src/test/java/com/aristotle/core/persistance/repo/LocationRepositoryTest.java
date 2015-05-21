package com.aristotle.core.persistance.repo;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.aristotle.core.persistance.Location;

//@DatabaseSetup({ LocationRepositoryTest.COMMON_USER_SET })
//@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { LocationRepositoryTest.COMMON_USER_SET })
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
