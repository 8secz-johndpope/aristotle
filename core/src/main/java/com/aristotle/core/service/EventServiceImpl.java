package com.aristotle.core.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aristotle.core.persistance.Event;
import com.aristotle.core.persistance.Location;
import com.aristotle.core.persistance.repo.EventRepository;
import com.aristotle.core.persistance.repo.LocationRepository;

@Service
@Transactional(rollbackFor={Throwable.class})
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private LocationRepository locationRepository;

    @Override
    public Event saveEvent(Event event, Location location) {
        System.out.println("Saving Event : " + event);
        System.out.println("Saving Event : " + event.getVer());
        System.out.println("Saving Event : " + event.getId());
        event = eventRepository.save(event);
        if (location == null) {
            event.setNational(true);
        }else{
            location = locationRepository.findOne(location.getId());
            if (event.getLocations() == null) {
                event.setLocations(new HashSet<Location>());
            }
            event.getLocations().add(location);
        }

        return event;
    }

    @Override
    @Cacheable(value = "events")
    public List<Event> getLocationEvents(Location location, int size, boolean publishedOnly) {
        System.out.println("Getting Data From Database");
        Pageable pageRequest = new PageRequest(0, size);
        if(location == null){
            if (publishedOnly) {
                return eventRepository.getAllNationalUpComingPublishedEvents(pageRequest);
            }
            return eventRepository.getAllNationalUpComingEvents(pageRequest);
        }
        if (publishedOnly) {
            return eventRepository.getLocationUpcomingPublishedEvents(location.getId(), pageRequest);
        }
        return eventRepository.getLocationUpcomingEvents(location.getId(), pageRequest);
    }

    @Override
    public Event getEventById(Long eventId) {
        return eventRepository.findOne(eventId);
    }

}
