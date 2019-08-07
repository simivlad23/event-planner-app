package com.internship.eventplanner.repository;

import com.internship.eventplanner.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllBySentToId(Long userId);

    List<Notification> findAllBySentToIdAndIsRead(Long userId, Boolean isRead);

    List<Notification> findAllBySentByIdAndUserGroupId(Long userId, Long userGroupId);

}
