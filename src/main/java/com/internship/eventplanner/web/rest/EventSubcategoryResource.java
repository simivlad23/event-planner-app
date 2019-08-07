package com.internship.eventplanner.web.rest;

import com.internship.eventplanner.domain.EventSubcategory;
import com.internship.eventplanner.service.EventSubcategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EventSubcategoryResource {

    private Logger logger = LoggerFactory.getLogger(EventSubcategoryResource.class);

    private EventSubcategoryService eventSubcategoryService;

    public EventSubcategoryResource(EventSubcategoryService eventSubcategoryService) {
        this.eventSubcategoryService = eventSubcategoryService;
    }

    @GetMapping("/event-subcategory/get/{id}")
    public ResponseEntity<List<EventSubcategory>> getAllByEventCategory(@PathVariable("id") long id) {
        if (logger.isDebugEnabled()) {
            logger.debug("Rest request to get all event-subcategories by event-category: {}", id);
        }
        List<EventSubcategory> list = eventSubcategoryService.findAllByEventCategory(id);
        if (list.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<List<EventSubcategory>>(list, HttpStatus.OK);
        }
    }

    @PostMapping("/event-subcategory")
    public ResponseEntity<Void> addEventSubcategory(@RequestBody EventSubcategory eventSubcategory) {
        if (logger.isDebugEnabled()) {
            logger.debug("Rest request to add event-subcategory: {}", eventSubcategory);
        }
        boolean wasCreated = eventSubcategoryService.addEventSubcategory(eventSubcategory);
        if (wasCreated) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/event-subcategory/delete/{id}")
    public ResponseEntity<Void> deleteEventCategory(@PathVariable("id") long id) {
        if (logger.isDebugEnabled()) {
            logger.debug("Rest request to delete event-category with id: {}", id);
        }
        eventSubcategoryService.deleteEventSubcategory(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
