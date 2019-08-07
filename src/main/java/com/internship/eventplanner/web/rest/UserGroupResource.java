package com.internship.eventplanner.web.rest;

import com.internship.eventplanner.domain.User;
import com.internship.eventplanner.domain.UserGroup;
import com.internship.eventplanner.repository.UserGroupRepository;
import com.internship.eventplanner.repository.UserRepository;
import com.internship.eventplanner.service.UserGroupService;
import com.internship.eventplanner.service.dto.UserDTO;
import com.internship.eventplanner.service.dto.UserGroupDTO;
import com.internship.eventplanner.service.dto.UserIdDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserGroupResource {

    private final Logger log = LoggerFactory.getLogger(UserGroupResource.class);

    private final UserGroupRepository userGroupRepository;

    private final UserRepository userRepository;

    private final UserGroupService userGroupService;

    private final AccountResource accountResource;

    @Autowired
    public UserGroupResource(UserGroupRepository userGroupRepository, UserGroupService userGroupService, UserRepository userRepository, AccountResource accountResource) {
        this.userGroupRepository = userGroupRepository;
        this.userGroupService = userGroupService;
        this.accountResource = accountResource;
        this.userRepository = userRepository;
    }

    @PostMapping("/userGroups")
    public ResponseEntity<UserGroupDTO> createGroup(@RequestBody UserGroupDTO groupDTO) {
        log.debug("Rest request to create group:{}", groupDTO);
        final UserDTO loggedUser = accountResource.getAccount();
        UserGroupDTO result = userGroupService.create(groupDTO, loggedUser);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/userGroups")
    public ResponseEntity<List<UserGroup>> getAllUserGroups() {
        if (log.isDebugEnabled()) {
            log.debug("REST request to get all groups");
        }
        List<UserGroup> userGroupList = userGroupRepository.findAll();
        return new ResponseEntity<>(userGroupList, HttpStatus.OK);
    }

    @GetMapping("/userGroups/page")
    public ResponseEntity<Page<UserGroup>> getAllUserGroups(
        @RequestParam(defaultValue = "0") Integer pageNo,
        @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<UserGroup> page = userGroupService.getAllGroupsByPage(pageNo, pageSize);
        return new ResponseEntity<Page<UserGroup>>(page, HttpStatus.OK);
    }




    @GetMapping("/userGroups/users/{groupId}")
    public ResponseEntity<Set<User>> getAllUserForGroup(@PathVariable Long groupId) {
        UserGroup userGroup = userGroupRepository.findUserGroupById(groupId);
        if (userGroup == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(userGroup.getUsers(), HttpStatus.OK);
    }

    @GetMapping("/userGroups/{groupId}")
    public ResponseEntity<UserGroup> getUserGroupByID(@PathVariable Long groupId) {
        UserGroup userGroup = userGroupRepository.findUserGroupById(groupId);
        if (userGroup == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(userGroup, HttpStatus.OK);
    }

    @PutMapping("/userGroups/users/add/")
    public ResponseEntity addUsersToGroup(@RequestBody UserIdDTO userIdDTO) {
        if (userIdDTO.getUserId().isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        final UserDTO loggedUser = accountResource.getAccount();
        final Optional<User> sendBy = userRepository.findById(loggedUser.getId());
        UserGroup userGroup = userGroupRepository.findUserGroupById(userIdDTO.getGroupId());

        List<User> usersToAdd = userIdDTO.getUserId().stream().map(userToAdd -> {
            Optional<User> user = userRepository.findById(userToAdd);
            if (user.isPresent())
                return user.get();
            return new User();
        }).collect(Collectors.toList());
        if (sendBy.isPresent())
            this.userGroupService.notificationsForNewAddedUsers(userGroup.getName(), sendBy.get(), usersToAdd);

        userGroup.getUsers().addAll(usersToAdd);
        userGroupRepository.save(userGroup);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/userGroups/users/not/{groupId}")
    public ResponseEntity<List<User>> getUsersNotFromGroup(@PathVariable Long groupId) {
        UserGroup userGroup = userGroupRepository.findUserGroupById(groupId);
        List<User> users = userRepository.findAllByIdIsNotIn(
            userGroup.getUsers().stream()
                .map(User::getId)
                .collect(Collectors.toList()));
        return new ResponseEntity(users, HttpStatus.OK);
    }

    @GetMapping("/userGroups/groupName/{groupName}")
    public Long checkUniqueGroupName(@PathVariable String groupName) {
        return this.userGroupRepository.countByName(groupName);
    }
}
