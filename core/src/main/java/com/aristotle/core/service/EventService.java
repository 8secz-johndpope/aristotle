package com.aristotle.core.service;

import java.util.List;

import com.aristotle.core.persistance.Event;
import com.aristotle.core.persistance.Location;

public interface EventService {

    Event saveEvent(Event event, Location location);

    List<Event> getLocationEvents(Location location);

    Event getEventById(Long eventId);

}
