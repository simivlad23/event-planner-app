package com.internship.eventplanner.web.rest;

import com.internship.eventplanner.domain.Notification;
import com.internship.eventplanner.repository.NotificationRepository;
import com.internship.eventplanner.service.NotificationService;
import com.internship.eventplanner.service.dto.JoinRequestNotificationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class NotificationResource {

    private final Logger logger = LoggerFactory.getLogger(EventResource.class);

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationService notificationService;

    public NotificationResource(NotificationRepository notificationRepository, NotificationService notificationService) {
        this.notificationRepository = notificationRepository;
        this.notificationService = notificationService;
    }

    @GetMapping("/notifications/{userId}")
    public ResponseEntity<List<Notification>> findAllNotificationsForUser(@PathVariable Long userId) {
        if (logger.isDebugEnabled()) {
            logger.debug("Find all notifications for user with id {}", userId);
        }
        List<Notification> notifications = notificationRepository.findAllBySentToId(userId);
        Collections.sort(notifications, (o1, o2) -> {
            if (o1.getDateTime() == null || o2.getDateTime() == null)
                return 0;
            return o2.getDateTime().compareTo(o1.getDateTime());
        });
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    @Transactional
    @PutMapping("/notifications/acceptRequest/{notificationId}")
    public ResponseEntity<Void> acceptGroupRequest(@PathVariable Long notificationId) {
        Optional<Notification> notificationOptional = notificationRepository.findById(notificationId);
        Notification notification = notificationOptional.get();
        if (logger.isDebugEnabled()) {
            logger.debug("Trying to accept join request");
        }
        if (!notification.isValid()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        Long userId = notification.getSentBy().getId();
        Long userGroupId = notification.getUserGroup().getId();
        Boolean wasJoinRequestAccepted = notificationService.isJoinGroupRequestAccepted(userId, userGroupId);
        if (logger.isDebugEnabled()) {
            logger.debug("Accepting join request");
        }
        if (!wasJoinRequestAccepted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        notification.setValid(false);
        notification.setRead(true);
        notificationRepository.save(notification);
        this.notificationService.invalidAllNotifications(notification.getSentBy().getId(), notification.getUserGroup().getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @PutMapping("/notifications/rejectRequest/{notificationId}")
    public ResponseEntity<Void> rejectGroupRequest(@PathVariable Long notificationId) {
        if (logger.isDebugEnabled()) {
            logger.debug("Trying to reject join request.");
        }
        Optional<Notification> notificationOptional = notificationRepository.findById(notificationId);
        Notification notification = notificationOptional.get();

        if (notification.isValid()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Join request was already rejected.");
            }
            notification.setValid(false);
            notification.setRead(true);
            notificationRepository.save(notification);
            this.notificationService.invalidAllNotifications(notification.getSentBy().getId(), notification.getUserGroup().getId());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Transactional
    @PutMapping("/notifications/markAsRead/{notificationId}")
    public void markNotificationAsRead(@PathVariable Long notificationId) {
        if (logger.isDebugEnabled()) {
            logger.debug("Mark notification with id {} as read", notificationId);
        }
        Notification notification = notificationRepository.getOne(notificationId);
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @PostMapping("/notification/sentJoinRequest")
    public ResponseEntity<JoinRequestNotificationDTO> createJoinRequest(@RequestBody JoinRequestNotificationDTO joinReq) {
        if (logger.isDebugEnabled()) {
            logger.debug(joinReq.getJoinMessage() + " " + joinReq.getRequesterId() + " " + joinReq.getGroupId());
        }
        notificationService.createJoinNotification(joinReq.getRequesterId(), joinReq.getGroupId(), joinReq.getJoinMessage());
        return new ResponseEntity(new JoinRequestNotificationDTO(), HttpStatus.CREATED);
    }

    @PostMapping("/notifications/sendJoinResponse")
    public void sendJoinResponse(@RequestBody Notification response) {
        response.setDateTime(ZonedDateTime.now());
        notificationRepository.save(response);
    }

    @GetMapping("/notifications/getJoinRequest/{userId}")
    public ResponseEntity<List<JoinRequestNotificationDTO>> findUsersJoinGroupRequest(@PathVariable Long userId) {
        List<JoinRequestNotificationDTO> joinRequestList = notificationService.findAllUsersJoinGroupRequest(userId);
        logger.debug("Get all join request: " + userId);
        return new ResponseEntity<>(joinRequestList, HttpStatus.OK);
    }

    @GetMapping("/notifications/read/{userId}")
    public ResponseEntity<List<Notification>> findAllReadNotifications(@PathVariable Long userId) {
        if (logger.isDebugEnabled()) {
            logger.debug("Find all read notifications for user with id {}", userId);
        }
        List<Notification> readNotifications = notificationRepository.findAllBySentToIdAndIsRead(userId, true);
        Collections.sort(readNotifications, (o1, o2) -> {
            if (o1.getDateTime() == null || o2.getDateTime() == null)
                return 0;
            return o2.getDateTime().compareTo(o1.getDateTime());
        });
        return new ResponseEntity<>(readNotifications, HttpStatus.OK);
    }

    @GetMapping("/notifications/unread/{userId}")
    public ResponseEntity<List<Notification>> findAllUnreadNotifications(@PathVariable Long userId) {
        if (logger.isDebugEnabled()) {
            logger.debug("Find all unread notifications for user with id {}", userId);
        }
        List<Notification> unreadNotifications = notificationRepository.findAllBySentToIdAndIsRead(userId, false);
        Collections.sort(unreadNotifications, (o1, o2) -> {
            if (o1.getDateTime() == null || o2.getDateTime() == null)
                return 0;
            return o2.getDateTime().compareTo(o1.getDateTime());
        });
        return new ResponseEntity<>(unreadNotifications, HttpStatus.OK);
    }
}
