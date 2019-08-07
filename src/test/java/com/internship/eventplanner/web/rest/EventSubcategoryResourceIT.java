package com.internship.eventplanner.web.rest;

import com.internship.eventplanner.EventPlannerApp;
import com.internship.eventplanner.domain.EventCategory;
import com.internship.eventplanner.domain.EventSubcategory;
import com.internship.eventplanner.repository.EventCategoryRepository;
import com.internship.eventplanner.repository.EventSubcategoryRepository;
import com.internship.eventplanner.service.EventSubcategoryService;
import com.internship.eventplanner.web.rest.errors.ExceptionTranslator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = EventPlannerApp.class)
public class EventSubcategoryResourceIT {

    @Autowired
    private EventSubcategoryService eventSubcategoryService;

    @Autowired
    private EventSubcategoryRepository eventSubcategoryRepository;

    @Autowired
    private EventCategoryRepository eventCategoryRepository;

    private MockMvc restSubcategoryMockMvc;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private EventSubcategory eventSubcategory;

    private EventCategory eventCategory;

    @BeforeEach
    void init() {
        EventSubcategoryResource subcategoryResource = new EventSubcategoryResource(eventSubcategoryService);

        this.restSubcategoryMockMvc = MockMvcBuilders.standaloneSetup(subcategoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter)
            .build();

        eventCategory = new EventCategory();
        eventCategory.setName("test category");
        eventCategoryRepository.save(this.eventCategory);

        eventSubcategory = new EventSubcategory();
        eventSubcategory.setName("test subcategory");
        eventSubcategory.setEventCategory(eventCategory);


    }

    @Test
    @Transactional
    void testFindAll() throws Exception {
        this.eventSubcategoryRepository.deleteAll();
        this.eventSubcategoryRepository.save(eventSubcategory);

        restSubcategoryMockMvc.perform(get("/api/event-subcategory/get/" + eventCategory.getId())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventSubcategory)))
            .andExpect(status().isOk());

        this.eventSubcategoryRepository.deleteAll();
    }

    @Test
    @Transactional
    void testCreateSubcategory() throws Exception {
        this.eventSubcategoryRepository.deleteAll();

        int sizeBefore = this.eventSubcategoryRepository.findAllByEventCategory(eventCategory).size();

        restSubcategoryMockMvc.perform(post("/api/event-subcategory/")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventSubcategory)))
            .andExpect(status().isCreated());

        int sizeAfter = this.eventSubcategoryRepository.findAllByEventCategory(eventCategory).size();

        assertThat(sizeAfter - 1).isEqualTo(sizeBefore);

        this.eventSubcategoryRepository.deleteAll();
    }

    @Test
    @Transactional
    void testDeleteSubcategory() throws Exception {
        this.eventSubcategoryRepository.deleteAll();
        this.eventSubcategoryRepository.save(eventSubcategory);

        int sizeBefore = this.eventSubcategoryRepository.findAllByEventCategory(eventCategory).size();

        restSubcategoryMockMvc.perform(delete("/api/event-subcategory/delete/" + eventSubcategory.getId())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventSubcategory)))
            .andExpect(status().isOk());

        int sizeAfter = this.eventSubcategoryRepository.findAllByEventCategory(eventCategory).size();

        assertThat(sizeAfter + 1).isEqualTo(sizeBefore);

        this.eventSubcategoryRepository.deleteAll();
    }
}
