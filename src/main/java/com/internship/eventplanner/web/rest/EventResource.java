package com.internship.eventplanner.web.rest;

import com.internship.eventplanner.domain.Event;
import com.internship.eventplanner.repository.EventRepository;
import com.internship.eventplanner.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class EventResource {

    private final Logger logger = LoggerFactory.getLogger(EventResource.class);

    private EventService eventService;

    private EventRepository eventRepository;

    public EventResource(EventService eventService, EventRepository eventRepository) {
        this.eventRepository = eventRepository;
        this.eventService = eventService;
    }

    @GetMapping("/events")
    public ResponseEntity<List<Event>> findAll() {
        if (logger.isDebugEnabled()) {
            logger.debug("Rest request to find all events");
        }
        return new ResponseEntity<List<Event>>(eventRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<Event> findOne(@PathVariable("id") long id) {
        if (logger.isDebugEnabled()) {
            logger.debug("Rest request to find event by id:{}", id);
        }
        Optional<Event> result = eventRepository.findById(id);
        if (result.isPresent()) {
            Event actualEvent = result.get();
            return new ResponseEntity<Event>(actualEvent, HttpStatus.OK);
        }
        return new ResponseEntity<Event>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/events")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) throws URISyntaxException {
        if (logger.isDebugEnabled()) {
            logger.debug("Rest request to create event:{}", event);
        }
        Event result = eventService.addEvent(event);
        return new ResponseEntity<Event>(result, HttpStatus.CREATED);
    }

    @DeleteMapping("/events/{id}")
    public ResponseEntity<Void> removeEvent(@PathVariable("id") long id) {
        if (logger.isDebugEnabled()) {
            logger.debug("Rest request to remove event:{}", id);
        }
        eventService.removeEvent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/events")
    public ResponseEntity<Event> updateEvent(@RequestBody Event event) throws URISyntaxException {
        if (logger.isDebugEnabled()) {
            logger.debug("Rest request to create event:{}", event);
        }
        boolean result = eventService.updateEvent(event);
        if (result) {
            return new ResponseEntity<Event>(HttpStatus.OK);
        }
        return new ResponseEntity<Event>(HttpStatus.CONFLICT);
    }


    @RequestMapping("/events/usergroups/{userGroupID}")
    public ResponseEntity<List<Event>> getAllEventsForGroup(@PathVariable Long userGroupID) {
        if (logger.isDebugEnabled()) {
            logger.debug("REST request to get all events for group with id {}", userGroupID);
        }
        List<Event> eventList = eventRepository.findAllByUserGroupIdOrderByDateTimeDesc(userGroupID);
        return new ResponseEntity<>(eventList, HttpStatus.OK);
    }
}
