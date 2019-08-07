package com.internship.eventplanner.service;

import com.internship.eventplanner.domain.Notification;
import com.internship.eventplanner.domain.NotificationType;
import com.internship.eventplanner.domain.User;
import com.internship.eventplanner.domain.UserGroup;
import com.internship.eventplanner.repository.NotificationRepository;
import com.internship.eventplanner.repository.UserGroupRepository;
import com.internship.eventplanner.repository.UserRepository;
import com.internship.eventplanner.service.dto.UserDTO;
import com.internship.eventplanner.service.dto.UserGroupDTO;
import com.internship.eventplanner.service.mapper.GroupMapper;
import com.internship.eventplanner.web.rest.UserGroupResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserGroupService {

    private final Logger log = LoggerFactory.getLogger(UserGroupResource.class);

    private UserRepository userRepository;

    private UserGroupRepository userGroupRepository;

    private GroupMapper groupMapper;

    private NotificationRepository notificationRepository;

    @Autowired
    private EntityManager entityManager;

    public UserGroupService(UserRepository userRepository, UserGroupRepository userGroupRepository, GroupMapper groupMapper, NotificationRepository notificationRepository) {
        this.userRepository = userRepository;
        this.userGroupRepository = userGroupRepository;
        this.groupMapper = groupMapper;
        this.notificationRepository = notificationRepository;
    }

    public void createNotification(User sentTo, User groupCreator, String groupName) {

        Notification notification = new Notification();
        notification.setMessage("You were added to group " + '"' + groupName + '"');
        notification.setType(NotificationType.INFO);
        notification.setValid(true);
        notification.setSentBy(groupCreator);
        notification.setSentTo(sentTo);
        notification.setDateTime(ZonedDateTime.now());
        notification.setRead(false);

        notificationRepository.save(notification);

    }

    @Transactional
    public UserGroupDTO create(UserGroupDTO userGroupDTO, UserDTO loggedUser) {

        UserGroup userGroup = new UserGroup();
        userGroup.setName(userGroupDTO.getName());
        userGroup.setEvents(new HashSet<>());

        Set<User> users = new HashSet<>();

        for (UserDTO userDTO : userGroupDTO.getUsers()) {
            Optional<User> userDB = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
            if (userDB.isPresent()) {
                users.add(userDB.get());
                log.debug(userDB.get().getId().toString());
                userDB.get().getUserGroups().add(userGroup);
            } else {
                log.debug("User doesn't exist");
            }
        }
        userGroup.setUsers(users);

        final Set<User> sendToUsers = new HashSet<>();
        final User groupCreator = entityManager.find(User.class, loggedUser.getId());
        for (User user : users) {
            if (!user.getId().equals(loggedUser.getId())) {
                sendToUsers.add(user);
                createNotification(user, groupCreator, userGroup.getName());
            }
        }

        UserGroup userGroupDB = userGroupRepository.save(userGroup);
        userGroupDTO = groupMapper.groupToGroupDTO(userGroupDB);
        return userGroupDTO;
    }

    @Transactional
    public void notificationsForNewAddedUsers(String groupName, User loggedUser, List<User> users) {
        for (final User addedUser : users) {
            createNotification(addedUser, loggedUser, groupName);
        }
    }

    @Transactional
    public Page<UserGroup> getAllGroupsByPage(Integer pageNo, Integer pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        return userGroupRepository.findAll(paging);
    }


}
