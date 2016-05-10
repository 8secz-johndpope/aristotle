package com.aristotle.core.persistance.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aristotle.core.persistance.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select distinct event from Event event where event.national=true and event.published=true order by event.startDate")
    public abstract List<Event> getAllNationalEvents(Pageable pageable);

    @Query("select distinct event from Event event where event.national=true and event.startDate > CURRENT_TIMESTAMP order by event.startDate")
    public abstract List<Event> getAllNationalUpComingEvents(Pageable pageable);

    @Query("select distinct event from Event event where event.national=true and event.published=true and event.startDate > CURRENT_TIMESTAMP order by event.startDate")
    public abstract List<Event> getAllNationalUpComingPublishedEvents(Pageable pageable);

    @Query("select distinct event from Event event join event.locations locations where locations.id=?1 and event.published=true order by event.dateCreated desc")
    public abstract List<Event> getLocationEvents(Long locationId);

    @Query("select distinct event from Event event join event.locations locations where locations.id=?1 and event.startDate > CURRENT_TIMESTAMP order by event.startDate")
    public abstract List<Event> getLocationUpcomingEvents(Long locationId, Pageable pageable);

    @Query("select distinct event from Event event join event.locations locations where locations.id=?1 and event.startDate > CURRENT_TIMESTAMP and event.published=true order by event.startDate")
    public abstract List<Event> getLocationUpcomingPublishedEvents(Long locationId, Pageable pageable);

    @Query("select distinct event from Event event join event.locations locations where locations.id=?1 and event.published=true order by event.dateCreated desc")
    public abstract List<Event> getLocationsEvents(List<Long> locationIds);

    /*
	public abstract List<Event> getAllFutureEvents();
	
	public abstract List<Event> getAllNationalEvents();
	
	public abstract List<Event> getStateEvents(Long stateId);
	
	public abstract List<Event> getDistrictEvents(Long district);
	
	public abstract List<Event> getAcEvents(Long acId);
	
	public abstract List<Event> getPcEvents(Long pcId);
	
	public abstract List<Event> getCountryEvents(Long pcId);
	
	public abstract List<Event> getCountryRegionEvents(Long pcId);
	
	public abstract List<Event> getCountryRegionAreaEvents(Long pcId);
	*/
}