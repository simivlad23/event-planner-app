package com.internship.eventplanner.web.rest;

import com.internship.eventplanner.EventPlannerApp;
import com.internship.eventplanner.domain.Comment;
import com.internship.eventplanner.domain.Event;
import com.internship.eventplanner.domain.LocationType;
import com.internship.eventplanner.domain.User;
import com.internship.eventplanner.repository.CommentRepository;
import com.internship.eventplanner.repository.EventRepository;
import com.internship.eventplanner.repository.UserRepository;
import com.internship.eventplanner.service.CommentService;
import com.internship.eventplanner.web.rest.errors.ExceptionTranslator;
import org.apache.commons.lang3.RandomStringUtils;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = EventPlannerApp.class)
public class CommentResourceIT {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    private MockMvc restCommentMockMvc;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private Event event;

    private User user;

    private Comment comment;

    @BeforeEach
    void init() {
        CommentResource commentResource = new CommentResource(commentService, commentRepository);

        this.restCommentMockMvc = MockMvcBuilders.standaloneSetup(commentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter)
            .build();

        eventRepository.deleteAll();
        event = new Event();
        event.setTitle("Title");
        event.setDescription("Description of the event");
        event.setLocationType(LocationType.ADDRESS);
        event.setLocation("Location");
        event.setDateTime(ZonedDateTime.of(2019, 07, 20, 12, 0, 0, 0, ZoneId.of("Europe/Paris")));
        event.setUserGroup(null);
        event.setEventCategory(null);
        eventRepository.save(event);

        userRepository.deleteAll();
        user = new User();
        user.setLogin(null);
        user.setPassword(RandomStringUtils.random(60));
        user.setActivated(true);
        user.setEmail(null);
        user.setFirstName("User");
        user.setLastName("User");
        user.setImageUrl("http://placehold.it/50x50");
        user.setLangKey("en");

        comment = new Comment();
        comment.setUser(user);
        comment.setEvent(event);
        comment.setText("text");
        comment.setDateTime(ZonedDateTime.now());

    }

    @Test
    @Transactional
    void testFindAll() throws Exception {
        this.commentRepository.save(comment);

        restCommentMockMvc.perform(get("/api/comments/event/" + comment.getId())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(comment)))
            .andExpect(status().isOk());

    }

    @Test
    @Transactional
    void testCreateComment() throws Exception {
        restCommentMockMvc.perform(post("/api/comments/")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(comment)))
            .andExpect(status().isInternalServerError());
    }

    @Test
    @Transactional
    void testDeleteComment() throws Exception {
        restCommentMockMvc.perform(delete("/api/comments/" + comment.getId())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(comment)))
            .andExpect(status().isBadRequest());
    }
}
