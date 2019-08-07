package com.internship.eventplanner.web.rest;

import com.internship.eventplanner.EventPlannerApp;
import com.internship.eventplanner.domain.Event;
import com.internship.eventplanner.domain.EventCategory;
import com.internship.eventplanner.domain.LocationType;
import com.internship.eventplanner.domain.UserGroup;
import com.internship.eventplanner.repository.EventCategoryRepository;
import com.internship.eventplanner.repository.EventRepository;
import com.internship.eventplanner.repository.UserGroupRepository;
import com.internship.eventplanner.service.EventService;
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

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = EventPlannerApp.class)
public class EventResourceIT {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventCategoryRepository eventCategoryRepository;


    @Autowired
    private UserGroupRepository userGroupRepository;

    private MockMvc restEventMockMvc;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private Event event;

    private UserGroup userGroup;

    private EventCategory eventCategory;

    @BeforeEach
    void init() {
        EventResource eventResource = new EventResource(eventService, eventRepository);

        this.restEventMockMvc = MockMvcBuilders.standaloneSetup(eventResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter)
            .build();

        userGroupRepository.deleteAll();
        userGroup = new UserGroup();
        userGroup.setName("Group Name");
        userGroup.setUsers(null);
        userGroup.setEvents(null);
        userGroupRepository.save(userGroup);

        eventCategoryRepository.deleteAll();
        eventCategory = new EventCategory();
        eventCategory.setName("Category");
        eventCategoryRepository.save(eventCategory);

        eventRepository.deleteAll();
        event = new Event();
        event.setTitle("Title");
        event.setDescription("Description of the event");
        event.setLocationType(LocationType.ADDRESS);
        event.setLocation("Location");
        event.setDateTime(ZonedDateTime.of(2019, 07, 20, 12, 0, 0, 0, ZoneId.of("Europe/Paris")));
        event.setUserGroup(userGroup);
        event.setEventCategory(eventCategory);
    }


    @Test
    @Transactional
    void testCreateEvent() throws Exception {
        eventRepository.deleteAll();
        int sizeBefore = eventRepository.findAll().size();

        restEventMockMvc.perform(post("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isCreated());

        int sizeAfter = eventRepository.findAll().size();

        assertThat(sizeAfter - 1).isEqualTo(sizeBefore);
        eventRepository.deleteAll();
    }

    @Test
    @Transactional
    void testFindAll() throws Exception {
        eventRepository.deleteAll();
        eventRepository.save(event);

        restEventMockMvc.perform(get("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isOk());

        eventRepository.deleteAll();
    }

    @Test
    @Transactional
    void testFindEvent() throws Exception {
        eventRepository.deleteAll();
        eventRepository.save(event);

        restEventMockMvc.perform(get("/api/events/" + event.getId())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(event.getId()));

        eventRepository.deleteAll();
    }

    @Test
    @Transactional
    void testFindNonexistentEvent() throws Exception {
        eventRepository.deleteAll();
        eventRepository.save(event);

        restEventMockMvc.perform(get("/api/events/" + (event.getId() + 1))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isNotFound());

        eventRepository.deleteAll();
    }

    @Test
    @Transactional
    void testDeleteEvent() throws Exception {
        eventRepository.deleteAll();
        eventRepository.save(event);

        int sizeBefore = eventRepository.findAll().size();

        restEventMockMvc.perform(delete("/api/events/" + event.getId())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isNoContent());

        int sizeAfter = eventRepository.findAll().size();

        assertThat(sizeAfter + 1).isEqualTo(sizeBefore);
        eventRepository.deleteAll();
    }

}
