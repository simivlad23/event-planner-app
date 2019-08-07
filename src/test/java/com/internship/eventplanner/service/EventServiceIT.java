package com.internship.eventplanner.service;

import com.internship.eventplanner.EventPlannerApp;
import com.internship.eventplanner.domain.*;
import com.internship.eventplanner.repository.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = EventPlannerApp.class)
@Transactional
public class EventServiceIT {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventCategoryRepository eventCategoryRepository;

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;


    private Event event;

    private UserGroup userGroup;

    private EventCategory eventCategory;

    private User user;

    @BeforeEach
    void init() {

        user = new User();
        user.setLogin("johndoe");
        user.setPassword(RandomStringUtils.random(60));
        user.setActivated(true);
        user.setEmail("johndoe@localhost");
        user.setFirstName("john");
        user.setLastName("doe");
        user.setImageUrl("http://placehold.it/50x50");
        user.setLangKey("dummy");
        userRepository.save(user);

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
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        zonedDateTime = zonedDateTime.plusDays(1);
        event.setDateTime(zonedDateTime);
        event.setUserGroup(userGroup);
        event.setEventCategory(eventCategory);

    }

    @Test
    @Transactional
    void testAddEvent() {
        int sizeBefore = eventRepository.findAll().size();
        Event savedEvent = eventService.addEvent(event);
        int sizeAfter = eventRepository.findAll().size();

        assertThat(savedEvent).isEqualTo(event);
        assertThat(sizeAfter - 1).isEqualTo(sizeBefore);
        eventRepository.deleteAll();
    }

    @Test
    @Transactional
    void testRemoveEvent() {
        eventRepository.save(event);

        int sizeBefore = eventRepository.findAll().size();
        eventService.removeEvent(event.getId());
        int sizeAfter = eventRepository.findAll().size();

        assertThat(sizeAfter + 1).isEqualTo(sizeBefore);
        eventRepository.deleteAll();
    }

    @Test
    @Transactional
    void testGetEvent() {
        eventService.addEvent(event);

        int sizeBefore = eventRepository.findAll().size();
        eventService.removeEvent(event.getId());
        int sizeAfter = eventRepository.findAll().size();

        assertThat(sizeAfter + 1).isEqualTo(sizeBefore);
        eventRepository.deleteAll();
    }

    @Test
    @Transactional
    void testgetAllEventsForTomorow() {


        eventService.addEvent(event);
        UserGroup userGroup = userGroupRepository.findAll().get(0);
        List<User> users = new ArrayList<>();
        users.add(user);
        userGroup.setUsers(new HashSet<>(users));
        List<Event> events = eventRepository.findAll();
        userGroup.setEvents(new HashSet<>(events));
        userGroupRepository.save(userGroup);

        User system = new User();
        system.setLogin("system");
        system.setPassword(RandomStringUtils.random(60));
        system.setActivated(true);
        system.setEmail("system@gmail.com");
        system.setFirstName("System");
        system.setLastName("System");
        userRepository.saveAndFlush(system);

        eventService.addNotificationIfEventTakesPlace();

        List<Notification> notifications = notificationRepository.findAll();

        assert (notifications.size() == 1);

        Notification notification = notifications.get(0);
        assert (notification.getEvent() == event);
        assert (notification.getType() == NotificationType.REMINDER);
        assert (notification.getSentTo() == user);
    }
}
