package com.internship.eventplanner.service;

import com.internship.eventplanner.EventPlannerApp;
import com.internship.eventplanner.domain.Comment;
import com.internship.eventplanner.domain.Event;
import com.internship.eventplanner.domain.LocationType;
import com.internship.eventplanner.domain.User;
import com.internship.eventplanner.repository.CommentRepository;
import com.internship.eventplanner.repository.EventRepository;
import com.internship.eventplanner.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = EventPlannerApp.class)
public class CommentServiceIT {
    @Autowired
    private CommentService commentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CommentRepository commentRepository;

    private Event event;

    private User user;

    private Comment comment;

    @BeforeEach
    void init() {
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
        User user = new User();
        user.setLogin("user");
        user.setPassword(RandomStringUtils.random(60));
        user.setActivated(true);
        user.setEmail("user@gmail.com");
        user.setFirstName("User");
        user.setLastName("User");
        userRepository.save(user);

        commentRepository.deleteAll();
        comment = new Comment();
        comment.setUser(user);
        comment.setEvent(event);
        comment.setText("text");
        comment.setDateTime(ZonedDateTime.now());
    }

    @Test
    void testComment() {
        commentRepository.deleteAll();
        int sizeBefore = this.commentRepository.findAllByEvent(event).size();
        this.commentService.addComment(comment);
        int sizeAfter = this.commentRepository.findAllByEvent(event).size();

        assertThat(sizeAfter - 1).isEqualTo(sizeBefore);
        commentRepository.deleteAll();
    }

}
