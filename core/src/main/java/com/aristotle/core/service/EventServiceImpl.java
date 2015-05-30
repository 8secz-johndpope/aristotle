package com.aristotle.core.service;

import java.util.HashSet;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristotle.core.persistance.Event;
import com.aristotle.core.persistance.Location;
import com.aristotle.core.persistance.repo.EventRepository;
import com.aristotle.core.persistance.repo.LocationRepository;

@Service
@Transactional
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private LocationRepository locationRepository;

    @Override
    public Event saveEvent(Event event, Location location) {
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
    public List<Event> getLocationEvents(Location location) {
        if(location == null){
            return eventRepository.getAllNationalEvents();
        }
        return eventRepository.getLocationEvents(location.getId());
    }

    @Override
    public Event getEventById(Long eventId) {
        return eventRepository.findOne(eventId);
    }

}
