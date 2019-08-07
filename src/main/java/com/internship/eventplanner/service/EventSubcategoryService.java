package com.internship.eventplanner.service;

import com.internship.eventplanner.domain.EventCategory;
import com.internship.eventplanner.domain.EventSubcategory;
import com.internship.eventplanner.repository.EventCategoryRepository;
import com.internship.eventplanner.repository.EventSubcategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EventSubcategoryService {

    private Logger logger = LoggerFactory.getLogger(EventSubcategoryService.class);

    private EventCategoryRepository eventCategoryRepository;

    private EventSubcategoryRepository eventSubcategoryRepository;


    public EventSubcategoryService(EventCategoryRepository eventCategoryRepository, EventSubcategoryRepository eventSubcategoryRepository) {
        this.eventCategoryRepository = eventCategoryRepository;
        this.eventSubcategoryRepository = eventSubcategoryRepository;
    }

    public List<EventSubcategory> findAllByEventCategory(long id) {
        if (logger.isDebugEnabled()) {
            logger.debug("Service request to get all event-subcategories by event-category: {}", id);
        }
        Optional<EventCategory> eventCategory = eventCategoryRepository.findById(id);
        if (eventCategory.isPresent()) {
            return eventSubcategoryRepository.findAllByEventCategory(eventCategory.get());
        } else {
            List<EventSubcategory> emptyList = new ArrayList<>();
            return emptyList;
        }
    }

    public boolean addEventSubcategory(EventSubcategory eventSubcategory) {
        if (logger.isDebugEnabled()) {
            logger.debug("Service request to add event-subcategory: {}", eventSubcategory);
        }
        Optional<EventSubcategory> existingEventSubcategory = eventSubcategoryRepository.findByName(eventSubcategory.getName());
        if (existingEventSubcategory.isPresent()) {
            return false;
        }

        Optional<EventCategory> existingEventCategory = eventCategoryRepository.findById(eventSubcategory.getEventCategory().getId());
        if (existingEventCategory.isPresent()) {
            eventSubcategory.setEventCategory(existingEventCategory.get());
            eventSubcategoryRepository.save(eventSubcategory);
            return true;
        }

        return false;
    }

    public void deleteEventSubcategory(long id) {
        if (logger.isDebugEnabled()) {
            logger.debug("Service request to delete event-subcategory with id: {}", id);
        }
        eventSubcategoryRepository.deleteById(id);
    }
}
