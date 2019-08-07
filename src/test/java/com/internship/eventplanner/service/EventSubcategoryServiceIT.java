package com.internship.eventplanner.service;

import com.internship.eventplanner.EventPlannerApp;
import com.internship.eventplanner.domain.EventCategory;
import com.internship.eventplanner.domain.EventSubcategory;
import com.internship.eventplanner.repository.EventCategoryRepository;
import com.internship.eventplanner.repository.EventSubcategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(classes = EventPlannerApp.class)
@Transactional
public class EventSubcategoryServiceIT {

    @Autowired
    private EventSubcategoryService eventSubcategoryService;

    @Autowired
    private EventCategoryRepository eventCategoryRepository;

    @Autowired
    private EventSubcategoryRepository eventSubcategoryRepository;

    private EventSubcategory eventSubcategory;

    private EventCategory eventCategory;

    @BeforeEach
    void init() {
        eventCategory = new EventCategory();
        eventCategory.setName("test category");
        eventCategoryRepository.save(this.eventCategory);

        eventSubcategory = new EventSubcategory();
        eventSubcategory.setName("test subcategory");
        eventSubcategory.setEventCategory(eventCategory);
    }

    @Test
    void testAddSubcategory() {
        int sizeBefore = this.eventSubcategoryRepository.findAllByEventCategory(eventCategory).size();
        this.eventSubcategoryService.addEventSubcategory(eventSubcategory);
        int sizeAfter = this.eventSubcategoryRepository.findAllByEventCategory(eventCategory).size();

        assertThat(sizeAfter - 1).isEqualTo(sizeBefore);
    }

    @Test
    void testDeleteSubcategory() {
        this.eventSubcategoryRepository.save(eventSubcategory);

        int sizeBefore = this.eventSubcategoryRepository.findAllByEventCategory(eventCategory).size();
        this.eventSubcategoryService.deleteEventSubcategory(eventSubcategory.getId());
        int sizeAfter = this.eventSubcategoryRepository.findAllByEventCategory(eventCategory).size();

        assertThat(sizeAfter + 1).isEqualTo(sizeBefore);
    }

}
