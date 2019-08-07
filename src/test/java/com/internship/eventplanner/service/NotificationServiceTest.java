package com.internship.eventplanner.service;

import com.internship.eventplanner.EventPlannerApp;
import com.internship.eventplanner.domain.Notification;
import com.internship.eventplanner.domain.NotificationType;
import com.internship.eventplanner.domain.User;
import com.internship.eventplanner.domain.UserGroup;
import com.internship.eventplanner.repository.UserGroupRepository;
import com.internship.eventplanner.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = EventPlannerApp.class)
@Transactional
class NotificationServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Autowired
    private NotificationService notificationService;

    @Test
    void createNotification() {

        String joinMessage = "message";
        UserGroup userGroup = new UserGroup();
        User sentBy = new User();
        User reciverUser = new User();

        Notification joinNotification = new Notification();
        joinNotification.setMessage(joinMessage);
        joinNotification.setType(NotificationType.GROUP_REQUEST);
        joinNotification.setValid(true);
        joinNotification.setUserGroup(userGroup);
        joinNotification.setSentBy(sentBy);
        joinNotification.setSentTo(reciverUser);

        Notification testNotification = notificationService.createNotification(joinMessage, userGroup, sentBy, reciverUser);

        assertThat(joinNotification.isValid()).isEqualTo(testNotification.isValid());
        assertThat(joinNotification.getUserGroup()).isEqualTo(testNotification.getUserGroup());
        assertThat(joinNotification.getMessage()).isEqualTo(testNotification.getMessage());
        assertThat(joinNotification.getSentBy()).isEqualTo(testNotification.getSentBy());
        assertThat(joinNotification.getSentTo()).isEqualTo(testNotification.getSentTo());

    }

    @Test
    void createJoinNotification() {

        User sentBy = new User();
        sentBy.setSentNotifications(new HashSet<>());
        sentBy.setReceivedNotifications(new HashSet<>());
        Long sentById = userRepository.save(sentBy).getId();

        UserGroup group = new UserGroup();

        User user1 = new User();
        user1.setSentNotifications(new HashSet<>());
        user1.setReceivedNotifications(new HashSet<>());
        User user2 = new User();
        user2.setSentNotifications(new HashSet<>());
        user2.setReceivedNotifications(new HashSet<>());
        User user3 = new User();
        user3.setSentNotifications(new HashSet<>());
        user3.setReceivedNotifications(new HashSet<>());

        User userDB1 = userRepository.save(user1);
        User userDB2 = userRepository.save(user2);
        User userDB3 = userRepository.save(user3);

        Set<User> users = new HashSet<>();
        users.add(userDB1);
        users.add(userDB2);
        users.add(userDB3);
        group.setUsers(users);

        Long userGroupId = userGroupRepository.save(group).getId();
        String joinMessage = "ceva";
        notificationService.createJoinNotification(sentById, userGroupId, joinMessage);
        assertThat(notificationService.findAllUsersJoinGroupRequest(sentById).size()).isEqualTo(1);
    }

    @Test
    @Transactional
    void isJoinGroupRequestAccepted() {
        userRepository.deleteAll();
        User user = new User();
        user.setFirstName("user");
        user.setLogin("user");
        user.setPassword(RandomStringUtils.random(60));

        User user1 = new User();
        user1.setFirstName("user1");
        user1.setLogin("user1");
        user1.setPassword(RandomStringUtils.random(60));

        UserGroup group = new UserGroup();
        Set<User> users = new HashSet<>();
        users.add(user1);
        group.setUsers(users);

        Set<UserGroup> userGroupsList = new HashSet<>();
        userGroupsList.add(group);
        user1.setUserGroups(userGroupsList);

        userRepository.saveAndFlush(user1);
        userGroupRepository.saveAndFlush(group);

        assertThat(userRepository.findAll().size()).isEqualTo(1);
        assertThat(userGroupRepository.findAll().size()).isEqualTo(1);

        userRepository.saveAndFlush(user);
        boolean isAccepted = notificationService.isJoinGroupRequestAccepted(user.getId(), group.getId());
        assertThat(isAccepted).isTrue();

        isAccepted = notificationService.isJoinGroupRequestAccepted(user.getId(), group.getId());
        assertThat(isAccepted).isFalse();
    }
}
