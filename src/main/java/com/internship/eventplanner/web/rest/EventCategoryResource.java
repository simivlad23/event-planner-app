package com.internship.eventplanner.web.rest;

import com.internship.eventplanner.domain.EventCategory;
import com.internship.eventplanner.repository.EventCategoryRepository;
import com.internship.eventplanner.service.dto.EventCategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class EventCategoryResource {
    @Autowired
    private EventCategoryRepository repoEventCategories;


    @PostMapping("/event-categories")
    public ResponseEntity<Void> createEventCategory(@RequestBody EventCategoryDTO eventCategoryDTO) {

        if (eventCategoryDTO.getName() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        EventCategory eventCategory = repoEventCategories.findByName(eventCategoryDTO.getName());
        if (eventCategory != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        eventCategory = new EventCategory();
        eventCategory.setName(eventCategoryDTO.getName());

        repoEventCategories.save(eventCategory);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/event-categories")
    public ResponseEntity<List<EventCategory>> getAllEventCategory() {
        List<EventCategory> allEventCategories = repoEventCategories.findAll();
        if (allEventCategories.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(allEventCategories, HttpStatus.OK);
    }
}
