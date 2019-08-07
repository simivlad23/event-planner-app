package com.internship.eventplanner.service;

import com.internship.eventplanner.domain.Notification;
import com.internship.eventplanner.domain.NotificationType;
import com.internship.eventplanner.domain.User;
import com.internship.eventplanner.domain.UserGroup;
import com.internship.eventplanner.repository.NotificationRepository;
import com.internship.eventplanner.repository.UserGroupRepository;
import com.internship.eventplanner.repository.UserRepository;
import com.internship.eventplanner.service.dto.JoinRequestNotificationDTO;
import com.internship.eventplanner.web.rest.EventResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class NotificationService {


    private final Logger logger = LoggerFactory.getLogger(EventResource.class);
    @PersistenceContext
    public EntityManager entityManager;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification createNotification(String joinMessage, UserGroup group, User user, User reciverUser) {

        Notification joinNotification = new Notification();
        joinNotification.setMessage(joinMessage);
        joinNotification.setType(NotificationType.GROUP_REQUEST);
        joinNotification.setValid(true);
        joinNotification.setRead(false);
        joinNotification.setUserGroup(group);
        joinNotification.setSentBy(user);
        joinNotification.setSentTo(reciverUser);
        joinNotification.setDateTime(ZonedDateTime.now());
        return joinNotification;
    }

    @Transactional
    public void createJoinNotification(Long requesterUserId, Long groupId, String joinMessage) {

        User user = entityManager.find(User.class, requesterUserId);
        UserGroup group = entityManager.find(UserGroup.class, groupId);
        Set<User> usersGroup = group.getUsers();

        for (final User receiverUser : usersGroup) {
            final Notification joinNotification = createNotification(joinMessage, group, user, receiverUser);
            user.getSentNotifications().add(joinNotification);
            receiverUser.getReceivedNotifications().add(joinNotification);
            entityManager.persist(user);
            entityManager.persist(receiverUser);
            notificationRepository.save(joinNotification);
        }
    }

    @Transactional
    public List<JoinRequestNotificationDTO> findAllUsersJoinGroupRequest(Long userId) {

        final User user = entityManager.find(User.class, userId);
        final Set<Notification> notificationsSentByUser = user.getSentNotifications();
        final Set<Long> joinGroups = new HashSet<>();

        for (final Notification notification : notificationsSentByUser) {
            if (notification.getType().equals(NotificationType.GROUP_REQUEST)) {
                if (joinGroups.contains(notification.getUserGroup().getId())) {
                    continue;
                }
                if (notification.isValid()) {
                    joinGroups.add(notification.getUserGroup().getId());
                }
            }
        }

        List<JoinRequestNotificationDTO> joinRequests = new ArrayList<>();
        for (Long groupId : joinGroups) {
            JoinRequestNotificationDTO joinRequest = new JoinRequestNotificationDTO();
            joinRequest.setGroupId(groupId);
            joinRequests.add(joinRequest);
        }

        return joinRequests;
    }

    @Transactional
    public boolean isJoinGroupRequestAccepted(Long userId, Long userGroupId) {
        final User user = entityManager.find(User.class, userId);
        final UserGroup group = entityManager.find(UserGroup.class, userGroupId);

        final Set<User> groupUsersList = group.getUsers();
        if (groupUsersList.contains(user)) {
            if (logger.isDebugEnabled()) {
                logger.debug("User with id {} was already added", user.getId());
            }
            return false;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Add user with id {} to group", user.getId());
        }
        groupUsersList.add(user);
        group.setUsers(groupUsersList);

        Set<UserGroup> userGroupsList = user.getUserGroups();
        userGroupsList.add(group);
        user.setUserGroups(userGroupsList);

        userGroupRepository.save(group);
        userRepository.save(user);
        return true;
    }

    @Transactional
    public void invalidAllNotifications(Long sentById, Long userGroupId) {
        if (logger.isDebugEnabled()) {
            logger.debug("Invalid all notifications sent by user with id {}", sentById);
        }
        List<Notification> notifications = notificationRepository.findAllBySentByIdAndUserGroupId(sentById, userGroupId);
        for (Notification notification : notifications) {
            notification.setValid(false);
            notificationRepository.saveAndFlush(notification);
        }
    }
}
