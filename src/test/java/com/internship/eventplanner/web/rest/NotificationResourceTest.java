package com.internship.eventplanner.web.rest;

import com.internship.eventplanner.EventPlannerApp;
import com.internship.eventplanner.domain.Notification;
import com.internship.eventplanner.domain.User;
import com.internship.eventplanner.domain.UserGroup;
import com.internship.eventplanner.repository.NotificationRepository;
import com.internship.eventplanner.repository.UserGroupRepository;
import com.internship.eventplanner.repository.UserRepository;
import com.internship.eventplanner.service.NotificationService;
import com.internship.eventplanner.web.rest.errors.ExceptionTranslator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = EventPlannerApp.class)
class NotificationResourceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationResource notificationResource;

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restNotificationMockMvc;

    @BeforeEach
    void setUp() {
        notificationResource = new NotificationResource(notificationRepository, notificationService);

        this.restNotificationMockMvc = MockMvcBuilders.standaloneSetup(notificationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter)
            .build();
    }

    @AfterEach
    void tearDown() {
        notificationRepository.deleteAll();
        userGroupRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findAllNotificationsForUser() throws Exception {
        User user = new User();
        user.setFirstName("user");
        user.setLogin("user");
        user.setPassword(RandomStringUtils.random(60));
        userRepository.saveAndFlush(user);

        User user1 = new User();
        user1.setFirstName("user1");
        user1.setLogin("user1");
        user1.setPassword(RandomStringUtils.random(60));
        userRepository.saveAndFlush(user1);

        UserGroup group = new UserGroup();
        group.setUsers(new HashSet<>());
        userGroupRepository.saveAndFlush(group);

        Notification notification = new Notification();
        notification.setUserGroup(group);
        notification.setSentBy(user);
        notification.setSentTo(user1);
        notificationRepository.saveAndFlush(notification);

        restNotificationMockMvc.perform(get("/api/notifications/" + user.getId().toString())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));

        restNotificationMockMvc.perform(get("/api/notifications/" + user1.getId().toString())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void acceptGroupRequest() throws Exception {
        User user = new User();
        user.setFirstName("user");
        user.setLogin("user");
        user.setPassword(RandomStringUtils.random(60));
        userRepository.saveAndFlush(user);

        User user1 = new User();
        user1.setFirstName("user1");
        user1.setLogin("user1");
        user1.setPassword(RandomStringUtils.random(60));
        userRepository.saveAndFlush(user1);

        User user2 = new User();
        user2.setFirstName("user2");
        user2.setLogin("user2");
        user2.setPassword(RandomStringUtils.random(60));
        userRepository.saveAndFlush(user2);

        UserGroup group = new UserGroup();
        group.setUsers(new HashSet<>());
        userGroupRepository.saveAndFlush(group);

        Notification notification = new Notification();
        notification.setUserGroup(group);
        notification.setSentBy(user);
        notification.setSentTo(user1);
        notification.setValid(true);
        notification.setMessage("Notification");
        notificationRepository.saveAndFlush(notification);

        Notification notification1 = new Notification();
        notification1.setUserGroup(group);
        notification1.setSentBy(user);
        notification1.setSentTo(user2);
        notification1.setValid(true);
        notification1.setMessage("Notification");
        notification1 = notificationRepository.saveAndFlush(notification);

        restNotificationMockMvc.perform(put("/api/notifications/acceptRequest/" + notification.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        restNotificationMockMvc.perform(put("/api/notifications/acceptRequest/" + notification1.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }

    @Test
    void rejectGroupRequest() throws Exception {
        User user = new User();
        user.setFirstName("user");
        user.setLogin("user");
        user.setPassword(RandomStringUtils.random(60));
        userRepository.saveAndFlush(user);

        User user1 = new User();
        user1.setFirstName("user1");
        user1.setLogin("user1");
        user1.setPassword(RandomStringUtils.random(60));
        userRepository.saveAndFlush(user1);

        User user2 = new User();
        user2.setFirstName("user2");
        user2.setLogin("user2");
        user2.setPassword(RandomStringUtils.random(60));
        userRepository.saveAndFlush(user2);

        UserGroup group = new UserGroup();
        group.setUsers(new HashSet<>());
        userGroupRepository.saveAndFlush(group);

        Notification notification = new Notification();
        notification.setUserGroup(group);
        notification.setSentBy(user);
        notification.setSentTo(user1);
        notification.setValid(true);
        notification.setMessage("Notification");
        notificationRepository.saveAndFlush(notification);

        Notification notification1 = new Notification();
        notification1.setUserGroup(group);
        notification1.setSentBy(user);
        notification1.setSentTo(user2);
        notification1.setValid(true);
        notification1.setMessage("Notification");
        notification1 = notificationRepository.saveAndFlush(notification);

        restNotificationMockMvc.perform(put("/api/notifications/rejectRequest/" + notification.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        restNotificationMockMvc.perform(put("/api/notifications/rejectRequest/" + notification1.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }
}
