package com.internship.eventplanner.web.rest;

import com.internship.eventplanner.EventPlannerApp;
import com.internship.eventplanner.domain.EventCategory;
import com.internship.eventplanner.repository.EventCategoryRepository;
import com.internship.eventplanner.service.dto.EventCategoryDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = EventPlannerApp.class)
class EventCategoryResourceIT {

    private final String DEFAULT_NAME = "Category";
    @Autowired
    private EventCategoryRepository evenetCategoryRepo;
    @Autowired
    private EventCategoryResource rest;
    private MockMvc mocMvc;
    private EventCategory eventCategory;

    @BeforeEach
    void setUp() {
        eventCategory = new EventCategory();
        eventCategory.setId(1L);
        eventCategory.setName(DEFAULT_NAME);


        MockitoAnnotations.initMocks(this);
        EventCategoryResourceIT resource = new EventCategoryResourceIT();
        mocMvc = MockMvcBuilders
            .standaloneSetup(rest)
            .build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createEventCategory() throws Exception {
        EventCategoryDTO ev = new EventCategoryDTO(DEFAULT_NAME);

        mocMvc.perform(post("/api/event-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ev)))
            .andExpect(status().isCreated());

        evenetCategoryRepo.deleteAll();
    }

    @Test
    void createNullEventCategory() throws Exception {
        EventCategoryDTO ev = new EventCategoryDTO(null);

        mocMvc.perform(post("/api/event-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ev)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void createExistingNameEventCategory() throws Exception {
        EventCategoryDTO ev = new EventCategoryDTO(DEFAULT_NAME);

        eventCategory = evenetCategoryRepo.save(eventCategory);

        mocMvc.perform(post("/api/event-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ev)))
            .andExpect(status().isConflict());
        evenetCategoryRepo.delete(eventCategory);
    }

    @Test
    void getAllEventCategory() throws Exception {
        EventCategoryDTO ev = new EventCategoryDTO(DEFAULT_NAME);

        eventCategory = evenetCategoryRepo.save(eventCategory);

        mocMvc.perform(get("/api/event-categories")
        )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].name").value((hasItem(DEFAULT_NAME))));
        evenetCategoryRepo.delete(eventCategory);
    }

    @Test
    void getAllEventCategoryNoContent() throws Exception {
        EventCategoryDTO ev = new EventCategoryDTO(DEFAULT_NAME);

        mocMvc.perform(get("/api/event-categories")
        )
            .andExpect(status().isNoContent());
    }
}
