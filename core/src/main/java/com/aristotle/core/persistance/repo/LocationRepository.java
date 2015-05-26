package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aristotle.core.persistance.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {

    Location getLocationByNameUpAndLocationTypeId(String name, Long locationTypeId);

    @Query("select distinct loc from Location loc, User user join user.locationRoles lr where  user.id=?1 and lr.locationId=loc.id")
    List<Location> getAdminLocationsOfUser(Long userId);

    List<Location> getLocationsByLocationTypeId(Long locationTypeId);

    List<Location> getLocationsByLocationTypeIdAndParentLocationId(Long locationTypeId, long parentLocationId);

    List<Location> getLocationsByParentLocationId(long parentLocationId);

}
